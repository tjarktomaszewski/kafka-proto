package Request;

import Request.Body.DescribeTopicPartitionsRequestBodyV0;
import Response.ApiKey;
import Response.ApiKeyEntry;
import Response.Body.AbstractResponseBody;
import Response.Body.ApiVersionsV4Body;
import Response.Body.DescribeTopicPartitionsV0Body;
import Response.ErrorCode;
import Response.Header.AbstractResponseHeader;
import Response.Header.ResponseHeaderV1;
import Response.KafkaResponse;
import Service.TopicService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KafkaRequestProcessor {
    private final TopicService topicService;

    public KafkaRequestProcessor(TopicService topicService) {
        this.topicService = topicService;
    }

    public KafkaResponse process(KafkaRequest request) {
        ErrorCode errorCode = ErrorCode.NONE;
        int apiVersion = request.getRequestHeader().getApiVersion();
        if (apiVersion < 0 || apiVersion > 4) {
            errorCode = ErrorCode.UNSUPPORTED_VERSION;
        }
        AbstractResponseBody responseBody = getResponseBody(request, errorCode, apiVersion);
        AbstractResponseHeader responseHeader = getResponseHeader(request);
        return new KafkaResponse(responseHeader, responseBody);
    }

    private AbstractResponseBody getResponseBody(KafkaRequest request, ErrorCode errorCode, int apiVersion) {
        int apiKey = request.getRequestHeader().getApiKey();

        if (apiKey == ApiKey.API_VERSIONS.getCode()) {
            ArrayList<ApiKeyEntry> apiKeyEntries = new ArrayList<>();
            if (errorCode == ErrorCode.NONE) {
                apiKeyEntries = ApiKeyEntry.getSupportedApiKeys();
            }
            System.out.println("apiKeyEntries: " + apiKeyEntries);
            return new ApiVersionsV4Body(errorCode, apiKeyEntries, 0, apiVersion);
        }
        if (apiKey == ApiKey.DESCRIBE_TOPIC_PARTITIONS.getCode()) {
            if (request.getRequestBody() instanceof DescribeTopicPartitionsRequestBodyV0 requestBody) {
                List<DescribeTopicPartitionsV0Body.TopicEntry> topicEntries = new ArrayList<>();
                for (String topicName : requestBody.getTopics()) {
                    ErrorCode topicErrorCode = topicService.topicExists(topicName) ? ErrorCode.NONE : ErrorCode.UNKNOWN_TOPIC_OR_PARTITION;
                    topicEntries.add(new DescribeTopicPartitionsV0Body.TopicEntry(
                            topicErrorCode,
                            topicName,
                            UUID.randomUUID(),
                            false,
                            0x7FFFFFFF
                    ));
                }
                return new DescribeTopicPartitionsV0Body(0, topicEntries);
            }
        }

        return new ApiVersionsV4Body(ErrorCode.UNSUPPORTED_VERSION, new ArrayList<>(), 0, apiVersion);
    }

    private AbstractResponseHeader getResponseHeader(KafkaRequest request) {
        return new ResponseHeaderV1(request.getRequestHeader().getCorrelationId(), request.getRequestHeader().getApiKey());
    }
}
