package Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TopicService {
    private final Path logDir = Paths.get(System.getProperty("java.io.tmpdir"), "kraft-combined-logs");
    private final Set<String> topics;

    public TopicService() {
        System.out.println("Loading topics from: " + logDir.toAbsolutePath());
        if (!Files.exists(logDir)) {
            System.out.println("Log directory not found, creating: " + logDir.toAbsolutePath());
            try {
                Files.createDirectories(logDir);
            } catch (IOException e) {
                System.err.println("Failed to create log directory: " + e.getMessage());
            }
        }
        this.topics = loadTopicsFromDir();
        System.out.println("Found topics: " + this.topics);
    }

    public boolean topicExists(String topicName) {
        return topics.contains(topicName);
    }

    public Set<String> getTopics() {
        return topics;
    }

    private Set<String> loadTopicsFromDir() {
        try (Stream<Path> stream = Files.list(logDir)) {
            return stream
                    .filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .filter(name -> name.matches(".*-\d+$")) // Ends with -<number>
                    .map(name -> name.substring(0, name.lastIndexOf('-')))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            System.err.println("Could not load topics from disk: " + e.getMessage());
            return new HashSet<>();
        }
    }
}
