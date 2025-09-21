package Request.Header;

abstract public class AbstractRequestHeader {
    protected int apiKey;
    protected int apiVersion;
    protected int correlationId;
    protected String clientId;

    public int getApiKey() {
        return apiKey;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public int getCorrelationId() {
        return correlationId;
    }

    public String getClientId() {
        return clientId;
    }
}
