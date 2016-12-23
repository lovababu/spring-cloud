package org.avol.springcloud.stream.kafka;

import org.avol.springcloud.stream.kafka.channels.MyIOChannel;
import org.avol.springcloud.stream.kafka.model.Tweet;
import org.avol.springcloud.stream.kafka.resource.consumer.KafkaConsumer;
import org.avol.springcloud.stream.kafka.resource.producer.KafkaProducer;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import javax.ws.rs.ApplicationPath;

@SpringBootApplication
@EnableBinding(MyIOChannel.class)
public class SpringCloudStreamKafkaApplication {

	@Bean
	public ResourceConfig resourceConfig() {
		return new RestConfig();
	}

	@Bean
	public KafkaConsumer kafkaConsumer() {
		return new KafkaConsumer();
	}

	/*@StreamListener("inputChannel")
	public void pull(@Payload Tweet tweet, @Header("Content-Type") String header) {
		//do your stuff.
		System.out.println("Tween Received: " + tweet + ", Header: " + header);
	}*/

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudStreamKafkaApplication.class, args);
	}
}

@ApplicationPath("/kafka")
class RestConfig extends ResourceConfig {
	public RestConfig() {
		registerClasses(KafkaProducer.class);
	}
}
