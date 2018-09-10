//package com.fcgl.MessageQueue;
//
//import com.fcgl.App;
//import org.springframework.boot.CommandLineRunner;
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.stereotype.Component;
//
//
///**
// * Can use this to send messages.... Not sure how routingKey works excatly... It's a little weird doing this with such
// * A heavy abstraction....
// */
//@Component
//public class Runner implements CommandLineRunner {
//    private final RabbitTemplate rabbitTemplate;
//    private final Receiver receiver;
//
//    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
//        this.receiver = receiver;
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("Sending message...");
//        rabbitTemplate.convertAndSend(App.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
//        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
//    }
//
//    //Notice that the template routes the message to the exchange, with a routing key of foo.bar.baz
//    //which matches the binding.
//}
