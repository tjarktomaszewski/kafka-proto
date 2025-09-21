package Response.Body;

import java.io.DataOutputStream;
import java.io.IOException;

abstract public class AbstractResponseBody {
    abstract public void writeBody(DataOutputStream dos) throws IOException;
}
