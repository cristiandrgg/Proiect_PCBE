package processes;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class HistoryProcess extends Thread {
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        Channel channel = null;
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            channel.queueDeclare("hello", false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            channel.basicConsume("hello", true, (consumerTag, message) -> {
                String m = new String(message.getBody(), "UTF-8");
                System.out.println("Message: " + m);
            }, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
