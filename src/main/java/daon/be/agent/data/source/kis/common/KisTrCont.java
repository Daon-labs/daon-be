package daon.be.agent.data.source.kis.common;

public enum KisTrCont {
    INITIAL(""),
    NEXT("N"),
    MORE("M");

    private final String value;

    KisTrCont(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
