package Service;

import Request.KafkaRequest;
import Request.KafkaRequestProcessor;
import Response.KafkaResponse;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final KafkaRequestProcessor requestProcessor;

    public ClientHandler(Socket clientSocket, KafkaRequestProcessor requestProcessor) {
        this.clientSocket = clientSocket;
        this.requestProcessor = requestProcessor;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream())) {
            DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());

            while (!clientSocket.isClosed() && clientSocket.isConnected()) {
                try {
                    KafkaRequest request = KafkaRequest.parse(in);
                    KafkaResponse response = requestProcessor.process(request);
                    response.send(dout);
                    System.out.println("Processed request with correlationId: " + request.getRequestHeader().getCorrelationId());
                } catch (IOException e) {
                    System.out.println("Client disconnected");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                if (!clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
