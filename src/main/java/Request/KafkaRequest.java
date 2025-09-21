package Request;

import Request.Body.AbstractRequestBody;
import Request.Body.ApiVersionsRequestV4Body;
import Request.Body.DescribeTopicPartitionsRequestBodyV0;
import Request.Header.RequestHeaderV2;
import Response.ApiKey;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class KafkaRequest {
    private final RequestHeaderV2 requestHeader;
    private final AbstractRequestBody requestBody;

    public KafkaRequest(RequestHeaderV2 requestHeader, AbstractRequestBody requestBody) {
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    public static KafkaRequest parse(BufferedInputStream in) throws IOException {
        DataInputStream din = new DataInputStream(in);
        int messageSize = din.readInt();
        RequestHeaderV2 header = RequestHeaderV2.parse(din);
        AbstractRequestBody body = null;
        System.out.println("apiversion: " + header.getApiVersion() + " apikey: " + header.getApiKey() + " correlationid: " + header.getCorrelationId());

        if (header.getApiKey() == ApiKey.API_VERSIONS.getCode() && header.getApiVersion() == 4) {
            System.out.println("Parsing ApiVersionsRequestV4Body");
            body = ApiVersionsRequestV4Body.parse(din);
        } else if (header.getApiKey() == ApiKey.DESCRIBE_TOPIC_PARTITIONS.getCode() && header.getApiVersion() == 0) {
            body = DescribeTopicPartitionsRequestBodyV0.parse(din);
        }

        return new KafkaRequest(header, body);
    }

    public AbstractRequestBody getRequestBody() {
        return requestBody;
    }

    public RequestHeaderV2 getRequestHeader() {
        return requestHeader;
    }
}

