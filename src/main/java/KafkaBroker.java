import Request.KafkaRequestProcessor;
import Service.ClientHandler;
import Service.TopicService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class KafkaBroker {
    private final int port;
    private boolean running;
    private final KafkaRequestProcessor requestProcessor;

    public KafkaBroker(int port) {
        this.port = port;
        this.running = false;
        TopicService topicService = new TopicService();
        this.requestProcessor = new KafkaRequestProcessor(topicService);
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        running = true;
        System.out.println("Kafka Broker started listening on port " + port);
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, requestProcessor)).start();
            } catch (IOException e) {
                if (running) {
                    System.out.println("Kafka Broker stopped");
                }
            }
        }
    }
}
