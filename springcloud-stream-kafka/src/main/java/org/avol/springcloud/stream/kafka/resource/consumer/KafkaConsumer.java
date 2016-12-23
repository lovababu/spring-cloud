package org.avol.springcloud.stream.kafka.resource.consumer;

import org.avol.springcloud.stream.kafka.model.Tweet;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * Created by dpadal on 12/23/2016.
 */
public class KafkaConsumer {

    @StreamListener("inputChannel")
    public void pull(@Payload Tweet tweet, @Header("contentType") String header) {
        //do your stuff.
        System.out.println("Tween Received: " + tweet + ", Header: " + header);
    }
}

