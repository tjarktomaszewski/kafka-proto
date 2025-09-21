package Request.Body;

import Helper.IOHelper;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ApiVersionsRequestV4Body extends AbstractRequestBody {
    private final String clientId;
    private final String clientSoftwareVersion;

    public ApiVersionsRequestV4Body(String clientId, String clientSoftwareVersion) {
        this.clientId = clientId;
        this.clientSoftwareVersion = clientSoftwareVersion;
    }

    public static ApiVersionsRequestV4Body parse(DataInputStream din) throws IOException {
        String clientId = IOHelper.readCompactString(din);
        String clientSoftwareVersion = IOHelper.readCompactString(din);
        System.out.println("clientSoftwareVersion " + clientSoftwareVersion);
        din.readByte(); // Tag Buffer
        return new ApiVersionsRequestV4Body(clientId, clientSoftwareVersion);
    }

    public String getClientSoftwareVersion() {
        return clientSoftwareVersion;
    }

    public String getClientId() {
        return clientId;
    }
}
