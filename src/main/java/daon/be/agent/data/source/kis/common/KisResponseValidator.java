package daon.be.agent.data.source.kis.common;

public final class KisResponseValidator {

    private KisResponseValidator() {
    }

    public static boolean isSuccess(String rtCd) {
        return "0".equals(rtCd);
    }
}
