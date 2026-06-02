package daon.be.agent.data.source.kis.common;

public enum KisMarketCode {
    KRX("J"),
    NXT("NX"),
    UNIFIED("UN"),
    KOSPI("K"),
    KOSDAQ("Q"),
    ALL("0");

    private final String code;

    KisMarketCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
