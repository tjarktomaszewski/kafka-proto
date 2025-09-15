import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class KafkaRequest {
    private final int apiKey;
    private final int apiVersion;
    private final int correlationId;
    private final String clientId;
    private final String clientSoftwareName;
    private final String clientSoftwareVersion;

    public KafkaRequest(int apiKey, int apiVersion, int correlationId, String clientId, String clientSoftwareName, String clientSoftwareVersion) {
        this.apiKey = apiKey;
        this.apiVersion = apiVersion;
        this.correlationId = correlationId;
        this.clientId = clientId;
        this.clientSoftwareName = clientSoftwareName;
        this.clientSoftwareVersion = clientSoftwareVersion;
    }

    public KafkaRequest(int apiKey, int apiVersion, int correlationId, String clientId, String clientSoftwareName) {
        this.apiKey = apiKey;
        this.apiVersion = apiVersion;
        this.correlationId = correlationId;
        this.clientId = clientId;
        this.clientSoftwareName = clientSoftwareName;
        this.clientSoftwareVersion = null;
    }

    public KafkaRequest(int apiKey, int apiVersion, int correlationId, String clientId) {
        this.apiKey = apiKey;
        this.apiVersion = apiVersion;
        this.correlationId = correlationId;
        this.clientId = clientId;
        this.clientSoftwareName = null;
        this.clientSoftwareVersion = null;
    }

    public static KafkaRequest parse(BufferedInputStream in) throws IOException {
        DataInputStream din = new DataInputStream(in);

        int messageSize = din.readInt();
        int apiKey = din.readShort();
        int apiVersion = din.readShort();
        int correlationId = din.readInt();

        int clientIdLength = din.readShort();
        String clientId = null;
        if (clientIdLength >= 0) {
            clientId = new String(din.readNBytes(clientIdLength), StandardCharsets.UTF_8);
            System.out.println("clientId:" + clientId);
        }
        din.skipBytes(2 + clientIdLength); // Skip tag buffer + clientId as we already retrieved it
        int clientSoftwareVersionLength = din.readByte();
        String clientSoftwareVersion = new String(din.readNBytes(clientSoftwareVersionLength), StandardCharsets.UTF_8);
        System.out.println("clientSoftwareVersion:" + clientSoftwareVersion);

        return new KafkaRequest(apiKey, apiVersion, correlationId, clientId, clientSoftwareVersion);
    }

    public int getApiKey() {
        return apiKey;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public int getCorrelationId() {
        return correlationId;
    }

    public String getClientSoftwareName() {
        return clientSoftwareName;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSoftwareVersion() {
        return clientSoftwareVersion;
    }
}
