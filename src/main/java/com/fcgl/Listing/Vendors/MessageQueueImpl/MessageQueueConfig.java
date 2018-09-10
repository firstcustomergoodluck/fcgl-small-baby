package com.fcgl.Listing.Vendors.MessageQueueImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.springframework.amqp.core.Queue;


@Profile({"MessageQue", "work-queues"})
@Configuration
public class MessageQueueConfig {

    @Bean
    public Queue hello() {
        return new Queue("hello");
    }

    @Profile("receiver")
    private static class ReceiverConfig {

        @Bean
        public MessageQueueReceiver receiver1() {
            return new MessageQueueReceiver(1);
        }

        @Bean
        public MessageQueueReceiver receiver2() {
            return new MessageQueueReceiver(2);
        }
    }

    @Profile("sender")
    @Bean
    public MessageQueueSender sender() {
        return new MessageQueueSender();
    }


}
