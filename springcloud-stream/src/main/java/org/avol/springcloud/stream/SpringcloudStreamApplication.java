package org.avol.springcloud.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.IntegrationComponentScan;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;

@EnableBinding(value = {Sink.class})
@IntegrationComponentScan
@SpringBootApplication
public class SpringcloudStreamApplication {

	private static Random random = new Random(1000);
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudStreamApplication.class, args);
	}

	/*@InboundChannelAdapter(Source.OUTPUT)
	public String pushMsg() {
		System.out.println("Pushing message to Queue.");
		return "Hello, RabbidMQ this is Spring cloud." + random.nextInt();
	}*/

	@StreamListener(Sink.INPUT)
	public void pullMsg(byte[] string) {
		System.out.println("Message from que: " + new String(string));
	}
}


