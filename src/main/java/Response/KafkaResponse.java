package Response;

import Helper.IOHelper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class KafkaResponse {
    private final int correlationId;
    private final ErrorCode errorCode;
    private final ArrayList<ApiKeyEntry> apiKeys;

    public KafkaResponse(int correlationId, ErrorCode errorCode,  ArrayList<ApiKeyEntry> apiKeys)
    {
        this.correlationId = correlationId;
        this.errorCode = errorCode;
        this.apiKeys = apiKeys;
    }

    public void send(DataOutputStream out) throws IOException {
        out.writeInt(calculateMessageLength());
        writeHeaderAndBody(out);
        out.flush();
    }

    private void writeHeaderAndBody(DataOutputStream out) throws IOException {
        out.writeInt(correlationId);
        out.writeShort(errorCode.getCode());
        IOHelper.writeUnsignedVarInt(out, apiKeys.size() + 1);
        for (ApiKeyEntry apiKey : apiKeys) {
            out.writeShort(apiKey.getApiKey().getCode());
            out.writeShort(apiKey.getMinVersion());
            out.writeShort(apiKey.getMaxVersion());
            out.writeByte(0);
        }
        out.writeInt(0); //Throttle time
        out.writeByte(0);
    }

    private int calculateMessageLength() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream buffer = new DataOutputStream(out);

        writeHeaderAndBody(buffer);
        buffer.flush();

        byte[] data = out.toByteArray();
        return data.length;
    }
}
