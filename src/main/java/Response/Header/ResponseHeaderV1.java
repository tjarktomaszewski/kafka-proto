package Response.Header;

import Response.ApiKey;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHeaderV1 extends AbstractResponseHeader {
    private final int apiKey;

    public ResponseHeaderV1(int correlationId, int apiKey) {
        this.correlationId = correlationId;
        this.apiKey = apiKey;
    }

    @Override
    public void writeHeader(DataOutputStream out) throws IOException {
        out.writeInt(correlationId);
        if (apiKey != ApiKey.API_VERSIONS.getCode()) {
            out.writeByte((byte) 0); // only response header v1 has tag buffer
        }
    }
}
