package org.avol.springcloud.stream;

/*import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;*/

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by dpadal on 12/21/2016.
 */
public class QueueProducer {

    private static final String QUEUE_NAME = "springcloud";

    public static void main(String[] args) throws IOException, TimeoutException {
        /*ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        String message = "Hello RabbitMQ this is JAVA.!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();*/
    }
}
