package org.avol.springcloud.stream.kafka.resource.producer;

import org.avol.springcloud.stream.kafka.channels.MyIOChannel;
import org.avol.springcloud.stream.kafka.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * Kafka producer, push the messages to queue <code>application.properties#</code>.
 *
 * Created by dpadal on 12/23/2016.
 */
@Path("/push")
public class KafkaProducer {

    @Autowired
    private MyIOChannel messageChannel;

    @POST
    public Response push(Tweet tweet) {
       boolean ack = messageChannel.output().send(MessageBuilder.withPayload(tweet)
               .setHeader("contentType", MediaType.APPLICATION_JSON)
               .build(), TimeUnit.SECONDS.toMillis(1));
       if (ack) {
           return Response.ok().entity("Message pushed to broker.").build();
       } else {
           return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Failed to push.").build();
       }
    }
}
