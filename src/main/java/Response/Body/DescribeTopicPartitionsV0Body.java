package Response.Body;

import Helper.IOHelper;
import Request.Body.AbstractRequestBody;
import Request.Body.DescribeTopicPartitionsRequestBodyV0;
import Request.KafkaRequest;
import Response.ErrorCode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DescribeTopicPartitionsV0Body extends AbstractResponseBody {

    private final int throttleTime;
    private final List<TopicEntry> topics;



    public static class TopicEntry {
        private final ErrorCode errorCode;
        private final String topicName;
        private final UUID topicId;
        private final boolean isInternal;
        private final int topicAuthorizedOperations;

        public TopicEntry(ErrorCode errorCode, String topicName, UUID topicId, boolean isInternal, int topicAuthorizedOperations) {
            this.errorCode = errorCode;
            this.topicName = topicName;
            this.topicId = topicId;
            this.isInternal = isInternal;
            this.topicAuthorizedOperations = topicAuthorizedOperations;
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }

        public String getTopicName() {
            return topicName;
        }

        public UUID getTopicId() {
            return topicId;
        }

        public boolean isInternal() {
            return isInternal;
        }

        public int getTopicAuthorizedOperations() {
            return topicAuthorizedOperations;
        }
    }

    public DescribeTopicPartitionsV0Body(int throttleTime, List<TopicEntry> topics) {
        this.throttleTime = throttleTime;
        this.topics = topics;
    }

    @Override
    public void writeBody(DataOutputStream dos) throws IOException {
        dos.writeInt(throttleTime);

        IOHelper.writeUnsignedVarInt(dos, topics.size() + 1);
        for (TopicEntry topic : topics) {
            dos.writeShort(topic.getErrorCode().getCode()); // error code
            IOHelper.writeCompactString(dos, topic.getTopicName()); // topic name

            dos.writeLong(topic.getTopicId().getMostSignificantBits()); // UUID
            dos.writeLong(topic.getTopicId().getLeastSignificantBits()); // UUID

            dos.writeByte(topic.isInternal() ? 1 : 0); // isInternal

            IOHelper.writeUnsignedVarInt(dos, 1); //Topics array

            dos.writeInt(topic.getTopicAuthorizedOperations()); // Topic authorized operations
            dos.writeByte(0); // Tag buffer for TopicEntry
        }

        dos.writeByte(0xff); // next cursor (null)
        dos.writeByte(0); // Tag buffer for DescribeTopicPartitionsV0Body
    }
}

