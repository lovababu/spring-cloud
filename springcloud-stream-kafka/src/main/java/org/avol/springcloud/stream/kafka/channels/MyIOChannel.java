package org.avol.springcloud.stream.kafka.channels;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by dpadal on 12/23/2016.
 */
public interface MyIOChannel {

    @Input("inputChannel")
    SubscribableChannel input();

    @Output("outputChannel")
    MessageChannel output();
}
