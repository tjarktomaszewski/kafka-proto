package Request.Body;

import Helper.IOHelper;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DescribeTopicPartitionsRequestBodyV0 extends AbstractRequestBody {

    private final List<String> topics;
    private final int responsePartitionLimit;
    private final byte cursor;

    public DescribeTopicPartitionsRequestBodyV0(List<String> topics,  int responsePartitionLimit) {
        this.topics = topics;
        this.responsePartitionLimit = responsePartitionLimit;
        this.cursor = 0;
    }

    public static DescribeTopicPartitionsRequestBodyV0 parse(DataInputStream din) throws IOException {
        int topicCount = IOHelper.readVarInt(din) - 1;
        List<String> topics = null;
        if (topicCount >= 0) {
            topics = new ArrayList<>(topicCount);
            for (int i = 0; i < topicCount; i++) {
                topics.add(IOHelper.readCompactString(din));
                din.skipBytes(1); // Skip Topic Tag Buffer
            }
        }
        int responsePartitionLimit = din.readInt();
        byte cursor = din.readByte();
        return new DescribeTopicPartitionsRequestBodyV0(topics, responsePartitionLimit);
    }

    public int getResponsePartitionLimit() {
        return responsePartitionLimit;
    }

    public List<String> getTopics() {
        return topics;
    }

    public byte getCursor() {
        return cursor;
    }
}

