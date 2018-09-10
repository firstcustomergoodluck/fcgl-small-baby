package com.fcgl.Listing.Vendors.MessageQueueImpl;

import com.fcgl.MessageQueue.IMessageQueueSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.amqp.core.Queue;
import org.springframework.scheduling.annotation.Scheduled;



import java.util.List;

public class MessageQueueSender{

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

    int dots = 0;
    int count = 0;

//    @Override
//    public void sendMessage(String message) {
//        template.convertAndSend(queue.getName(), message);
//        System.out.println(" [x] Sent '" + message + "'");
//    }
//
//    @Override
//    public void bulkSend(List<String> messages) {
//
//    }

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder builder = new StringBuilder("Hello");
        if (dots++ == 3) {
            dots = 1;
        }
        for (int i = 0; i < dots; i++) {
            builder.append('.');
        }

        builder.append(Integer.toString(++count));
        String message = builder.toString();
        template.convertAndSend(queue.getName(), message);
        System.out.println(" [x] Sent '" + message + "'");
    }

}
