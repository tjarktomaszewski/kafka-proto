package Response;

import java.util.ArrayList;

public class ApiKeyEntry {
    private final ApiKey apiKey;
    private final short minVersion;
    private final short maxVersion;

    public ApiKeyEntry(ApiKey apiKey, short minVersion, short maxVersion) {
        this.apiKey = apiKey;
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
    }

    public ApiKey getApiKey() {
        return apiKey;
    }

    public short getMinVersion() {
        return minVersion;
    }

    public short getMaxVersion() {
        return maxVersion;
    }

    public static ArrayList<ApiKeyEntry> getSupportedApiKeys() {
        ArrayList<ApiKeyEntry> apiKeyEntries = new ArrayList<>();
        apiKeyEntries.add(new ApiKeyEntry(ApiKey.fromCode((short) 18), (short) 0, (short) 4));
        apiKeyEntries.add(new ApiKeyEntry(ApiKey.fromCode((short) 75), (short) 0, (short) 0));
        return apiKeyEntries;
    }
}
