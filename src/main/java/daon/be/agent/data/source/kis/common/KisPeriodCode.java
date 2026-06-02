package daon.be.agent.data.source.kis.common;

public enum KisPeriodCode {
    DAILY("D"),
    WEEKLY("W"),
    MONTHLY("M"),
    YEARLY("Y");

    private final String code;

    KisPeriodCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
