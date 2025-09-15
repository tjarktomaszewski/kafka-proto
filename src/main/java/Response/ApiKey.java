package Response;

public enum ApiKey {
    API_VERSIONS((short) 18),
    DESCRIBE_TOPIC_PARTITIONS((short) 75);

    private final short code;

    ApiKey(short code) {
        this.code = code;
    }

    public short getCode() {
        return code;
    }

    public static ApiKey fromCode(short code) {
        for (ApiKey apiKey : ApiKey.values()) {
            if (apiKey.getCode() == code) {
                return apiKey;
            }
        }
        return null;
    }
}
