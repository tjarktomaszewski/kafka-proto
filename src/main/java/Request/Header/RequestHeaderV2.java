package Request.Header;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RequestHeaderV2 extends AbstractRequestHeader {
    private final short clientIdLength;

    public RequestHeaderV2(int apiKey, int apiVersion, int correlationId, short clientIdLength, String clientId) {
        this.apiKey = apiKey;
        this.apiVersion = apiVersion;
        this.correlationId = correlationId;
        this.clientIdLength = clientIdLength;
        this.clientId = clientId;
    }

    public static RequestHeaderV2 parse(DataInputStream din) throws IOException {
        int apiKey = din.readShort();
        int apiVersion = din.readShort();
        int correlationId = din.readInt();

        short clientIdLength = din.readShort();
        String clientId = null;
        if (clientIdLength >= 0) {
            clientId = new String(din.readNBytes(clientIdLength), StandardCharsets.UTF_8);
            System.out.println("clientId:" + clientId);
        }
        din.skipBytes(1);

        return new RequestHeaderV2(apiKey, apiVersion, correlationId, clientIdLength, clientId);
    }

    public short getClientIdLength() {
        return clientIdLength;
    }
}
