package Response;

public enum ErrorCode {
    NONE(0),
    UNKNOWN_TOPIC_OR_PARTITION(3),
    UNSUPPORTED_VERSION(35);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}