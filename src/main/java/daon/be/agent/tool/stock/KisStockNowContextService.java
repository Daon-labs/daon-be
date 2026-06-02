package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010100InquirePriceRequest;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010100InquirePriceResponse;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010200AskingPriceRequest;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010200AskingPriceResponse;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoRequest;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoResponse;
import daon.be.agent.tool.stock.dto.StockNowContextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KisStockNowContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final String TOOL_NAME = "GET_STOCK_NOW_CONTEXT";
    private static final String STOCK_MARKET_CODE = "J";
    private static final String STOCK_PRODUCT_TYPE_CODE = "300";

    private final KisQuotationApiClient quotationApiClient;

    public StockNowContextDto getStockNowContext(String stockCode) {
        String normalizedStockCode = normalizeStockCode(stockCode);
        OffsetDateTime requestedAt = now();
        List<StockNowContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime priceCalledAt = now();
        KisFhkst01010100InquirePriceResponse priceResponse = quotationApiClient.inquirePrice(
                new KisFhkst01010100InquirePriceRequest(STOCK_MARKET_CODE, normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST01010100_INQUIRE_PRICE, priceCalledAt, priceResponse.rtCd(), priceResponse.msg1()));

        OffsetDateTime askingPriceCalledAt = now();
        KisFhkst01010200AskingPriceResponse askingPriceResponse = quotationApiClient.inquireAskingPrice(
                new KisFhkst01010200AskingPriceRequest(STOCK_MARKET_CODE, normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST01010200_ASKING_PRICE, askingPriceCalledAt, askingPriceResponse.rtCd(), askingPriceResponse.msg1()));

        OffsetDateTime stockInfoCalledAt = now();
        KisCtpf1002rSearchStockInfoResponse stockInfoResponse = quotationApiClient.searchStockInfo(
                new KisCtpf1002rSearchStockInfoRequest(STOCK_PRODUCT_TYPE_CODE, normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.CTPF1002R_SEARCH_STOCK_INFO, stockInfoCalledAt, stockInfoResponse.rtCd(), stockInfoResponse.msg1()));

        validateSuccess(KisEndpoint.FHKST01010100_INQUIRE_PRICE, priceResponse.rtCd(), priceResponse.msg1());
        validateSuccess(KisEndpoint.FHKST01010200_ASKING_PRICE, askingPriceResponse.rtCd(), askingPriceResponse.msg1());
        validateSuccess(KisEndpoint.CTPF1002R_SEARCH_STOCK_INFO, stockInfoResponse.rtCd(), stockInfoResponse.msg1());

        KisFhkst01010100InquirePriceResponse.Output price = priceResponse.output();
        KisFhkst01010200AskingPriceResponse.Output1 orderbook = askingPriceResponse.output1();
        KisCtpf1002rSearchStockInfoResponse.Output stockInfo = stockInfoResponse.output();
        OffsetDateTime dataTimestamp = now();

        return new StockNowContextDto(
                new StockNowContextDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        dataTimestamp,
                        List.copyOf(apiCalls),
                        StockNowContextDto.DataFreshness.INTRADAY_SNAPSHOT,
                        List.of("국내주식 장운영정보 WebSocket 스냅샷은 아직 결합하지 않았습니다.")
                ),
                identity(normalizedStockCode, price, stockInfo),
                currentPrice(price, dataTimestamp),
                dailyRange(price),
                orderbook(orderbook),
                tradingStatus(price, stockInfo),
                valuation(price),
                interpretationHints(price, orderbook)
        );
    }

    private StockNowContextDto.StockIdentityDto identity(
            String stockCode,
            KisFhkst01010100InquirePriceResponse.Output price,
            KisCtpf1002rSearchStockInfoResponse.Output stockInfo
    ) {
        String industryCode = firstNonBlank(stockInfo.idxBztpSclsCd(), stockInfo.stdIdstClsfCd());
        String industryName = firstNonBlank(stockInfo.idxBztpSclsCdName(), stockInfo.stdIdstClsfCdName(), price.bstpKorIsnm());
        return new StockNowContextDto.StockIdentityDto(
                firstNonBlank(price.stckShrnIscd(), stockInfo.pdno(), stockCode),
                firstNonBlank(stockInfo.prdtName(), stockInfo.prdtAbrvName()),
                firstNonBlank(price.rprsMrktKorName(), stockInfo.mketIdCd()),
                stockInfo.excgDvsnCd(),
                industryCode,
                industryName,
                isEnabledCode(stockInfo.etfDvsnCd()),
                null,
                ynToBoolean(firstNonBlank(stockInfo.admnItemYn(), price.mangIssuClsCode())),
                ynToBoolean(firstNonBlank(stockInfo.trStopYn(), price.tempStopYn())),
                ynToBoolean(stockInfo.cpttTradTrPsblYn())
        );
    }

    private StockNowContextDto.PricePointDto currentPrice(
            KisFhkst01010100InquirePriceResponse.Output price,
            OffsetDateTime observedAt
    ) {
        return new StockNowContextDto.PricePointDto(
                decimal(price.stckPrpr()),
                decimal(price.prdyVrss()),
                decimal(price.prdyCtrt()),
                price.prdyVrssSign(),
                number(price.acmlVol()),
                decimal(price.acmlTrPbmn()),
                observedAt
        );
    }

    private StockNowContextDto.DailyPriceRangeDto dailyRange(KisFhkst01010100InquirePriceResponse.Output price) {
        return new StockNowContextDto.DailyPriceRangeDto(
                decimal(price.stckOprc()),
                decimal(price.stckHgpr()),
                decimal(price.stckLwpr()),
                decimal(price.stckMxpr()),
                decimal(price.stckLlam())
        );
    }

    private StockNowContextDto.OrderbookSnapshotDto orderbook(KisFhkst01010200AskingPriceResponse.Output1 output) {
        List<StockNowContextDto.OrderbookLevelDto> levels = new ArrayList<>();
        addLevel(levels, 1, output.askp1(), output.askpRsqn1(), output.bidp1(), output.bidpRsqn1());
        addLevel(levels, 2, output.askp2(), output.askpRsqn2(), output.bidp2(), output.bidpRsqn2());
        addLevel(levels, 3, output.askp3(), output.askpRsqn3(), output.bidp3(), output.bidpRsqn3());
        addLevel(levels, 4, output.askp4(), output.askpRsqn4(), output.bidp4(), output.bidpRsqn4());
        addLevel(levels, 5, output.askp5(), output.askpRsqn5(), output.bidp5(), output.bidpRsqn5());
        addLevel(levels, 6, output.askp6(), output.askpRsqn6(), output.bidp6(), output.bidpRsqn6());
        addLevel(levels, 7, output.askp7(), output.askpRsqn7(), output.bidp7(), output.bidpRsqn7());
        addLevel(levels, 8, output.askp8(), output.askpRsqn8(), output.bidp8(), output.bidpRsqn8());
        addLevel(levels, 9, output.askp9(), output.askpRsqn9(), output.bidp9(), output.bidpRsqn9());
        addLevel(levels, 10, output.askp10(), output.askpRsqn10(), output.bidp10(), output.bidpRsqn10());
        return new StockNowContextDto.OrderbookSnapshotDto(
                output.asprAcptHour(),
                List.copyOf(levels),
                number(output.totalAskpRsqn()),
                number(output.totalBidpRsqn()),
                number(output.ntbyAsprRsqn()),
                output.newMkopClsCode()
        );
    }

    private void addLevel(
            List<StockNowContextDto.OrderbookLevelDto> levels,
            int level,
            String askPrice,
            String askQuantity,
            String bidPrice,
            String bidQuantity
    ) {
        if (isBlank(askPrice) && isBlank(askQuantity) && isBlank(bidPrice) && isBlank(bidQuantity)) {
            return;
        }
        levels.add(new StockNowContextDto.OrderbookLevelDto(
                level,
                decimal(askPrice),
                number(askQuantity),
                decimal(bidPrice),
                number(bidQuantity)
        ));
    }

    private StockNowContextDto.TradingStatusDto tradingStatus(
            KisFhkst01010100InquirePriceResponse.Output price,
            KisCtpf1002rSearchStockInfoResponse.Output stockInfo
    ) {
        return new StockNowContextDto.TradingStatusDto(
                ynToBoolean(firstNonBlank(stockInfo.trStopYn(), price.tempStopYn())),
                ynToBoolean(price.tempStopYn()),
                ynToBoolean(price.invtCafulYn()),
                ynToBoolean(firstNonBlank(stockInfo.admnItemYn(), price.mangIssuClsCode())),
                price.viClsCode(),
                price.mrktWarnClsCode()
        );
    }

    private StockNowContextDto.ValuationSnapshotDto valuation(KisFhkst01010100InquirePriceResponse.Output price) {
        return new StockNowContextDto.ValuationSnapshotDto(
                decimal(price.per()),
                decimal(price.pbr()),
                decimal(price.eps()),
                decimal(price.bps())
        );
    }

    private List<String> interpretationHints(
            KisFhkst01010100InquirePriceResponse.Output price,
            KisFhkst01010200AskingPriceResponse.Output1 orderbook
    ) {
        List<String> hints = new ArrayList<>();
        if (decimal(price.prdyCtrt()) != null) {
            hints.add("현재가와 등락률은 KIS 주식현재가 시세 기준입니다.");
        }
        if (number(orderbook.totalAskpRsqn()) != null && number(orderbook.totalBidpRsqn()) != null) {
            hints.add("호가 잔량은 질문 시점의 REST 스냅샷이며 초 단위 변화 추적은 포함하지 않습니다.");
        }
        return List.copyOf(hints);
    }

    private StockNowContextDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new StockNowContextDto.KisApiCallSummary(
                endpoint.apiKoreanName(),
                endpoint.trId(),
                endpoint.url(),
                calledAt,
                success,
                success ? null : msg1
        );
    }

    private void validateSuccess(KisEndpoint endpoint, String rtCd, String msg1) {
        if (!KisResponseValidator.isSuccess(rtCd)) {
            throw new IllegalStateException(endpoint.apiKoreanName() + " KIS 응답 실패: " + msg1);
        }
    }

    private BigDecimal decimal(String value) {
        if (isBlank(value)) {
            return null;
        }
        return new BigDecimal(value.trim().replace(",", ""));
    }

    private Long number(String value) {
        if (isBlank(value)) {
            return null;
        }
        return Long.parseLong(value.trim().replace(",", ""));
    }

    private Boolean ynToBoolean(String value) {
        if (isBlank(value)) {
            return null;
        }
        return "Y".equalsIgnoreCase(value.trim());
    }

    private boolean isEnabledCode(String value) {
        return !isBlank(value) && !"0".equals(value.trim());
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (!isBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private String normalizeStockCode(String stockCode) {
        if (isBlank(stockCode)) {
            throw new IllegalArgumentException("stockCode는 필수입니다.");
        }
        return stockCode.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now(SEOUL);
    }
}
