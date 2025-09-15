import Response.ApiKey;
import Response.ApiKeyEntry;
import Response.ErrorCode;
import Response.KafkaResponse;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream())) {
            DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());

            while (!clientSocket.isClosed() && clientSocket.isConnected()) {
                try {
                    KafkaRequest request = KafkaRequest.parse(in);
                    KafkaResponse response = processRequest(request);
                    response.send(dout);
                    System.out.println("Process request with correlationId: " + request.getCorrelationId());
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

    private KafkaResponse processRequest(KafkaRequest request) {
        ErrorCode errorCode = ErrorCode.NONE;
        if (request.getApiVersion() < 0 || request.getApiVersion() > 4) {
            errorCode = ErrorCode.UNSUPPORTED_VERSION;
        }
        ArrayList<ApiKeyEntry> apiKeyEntries = ApiKeyEntry.getSupportedApiKeys();
        return new KafkaResponse(request.getCorrelationId(), errorCode, apiKeyEntries);
    }
}
