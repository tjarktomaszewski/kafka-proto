package Response.Header;

import java.io.DataOutputStream;
import java.io.IOException;

abstract public class AbstractResponseHeader {
    protected int correlationId;

    public int getCorrelationId() {
        return correlationId;
    }

    abstract public void writeHeader(DataOutputStream out) throws IOException;
}
