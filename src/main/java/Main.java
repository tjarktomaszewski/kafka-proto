import java.io.IOException;

public class Main {


    public static void main(String[] args) {
        KafkaBroker broker = new KafkaBroker(9092);
        try {
            broker.start();
        } catch (IOException e) {
            System.out.println("Could not start broker: " + e.getMessage());
        }
    }
}