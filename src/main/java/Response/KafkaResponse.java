package Response;

import Response.Body.AbstractResponseBody;
import Response.Header.AbstractResponseHeader;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class KafkaResponse {
    private final AbstractResponseBody abstractResponseBody;
    private final AbstractResponseHeader abstractResponseHeader;

    public KafkaResponse(AbstractResponseHeader abstractResponseHeader, AbstractResponseBody abstractResponseBody) {
        this.abstractResponseHeader = abstractResponseHeader;
        this.abstractResponseBody = abstractResponseBody;
    }

    public void send(DataOutputStream out) throws IOException {
        out.writeInt(calculateMessageLength());
        abstractResponseHeader.writeHeader(out);
        abstractResponseBody.writeBody(out);
        out.flush();
    }

    private int calculateMessageLength() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream buffer = new DataOutputStream(out);

        abstractResponseHeader.writeHeader(buffer);
        abstractResponseBody.writeBody(buffer);
        buffer.flush();

        byte[] data = out.toByteArray();
        return data.length;
    }
}
