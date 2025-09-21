package Response.Body;

import Helper.IOHelper;
import Response.ApiKeyEntry;
import Response.ErrorCode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ApiVersionsV4Body extends AbstractResponseBody{
    private final ErrorCode errorCode;
    private final ArrayList<ApiKeyEntry> apiKeys;
    private final int throttleTime;
    private final int apiVersion;

    public ApiVersionsV4Body(ErrorCode errorCode, ArrayList<ApiKeyEntry> apiKeys, int throttleTime, int apiVersion) {
        this.errorCode = errorCode;
        this.apiKeys = apiKeys;
        this.throttleTime = throttleTime;
        this.apiVersion = apiVersion;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ArrayList<ApiKeyEntry> getApiKeys() {
        return apiKeys;
    }

    public int getThrottleTime() {
        return throttleTime;
    }

    @Override
    public void writeBody(DataOutputStream out) throws IOException {
        out.writeShort(errorCode.getCode());

        // v0-v3 use standard arrays, v4+ uses compact arrays
        if (apiVersion >= 4) {
            IOHelper.writeUnsignedVarInt(out, apiKeys.size() + 1);
        } else {
            out.writeInt(apiKeys.size());
        }

        for (ApiKeyEntry apiKey : apiKeys) {
            out.writeShort(apiKey.getApiKey().getCode());
            out.writeShort(apiKey.getMinVersion());
            out.writeShort(apiKey.getMaxVersion());
            // ApiKey struct in v4+ has tagged fields
            if (apiVersion >= 4) {
                out.writeByte(0);
            }
        }

        if (apiVersion >= 3) {
            out.writeInt(throttleTime);
        }

        // Response body in v3+ has tagged fields
        if (apiVersion >= 3) {
            out.writeByte(0);
        }
    }
}
