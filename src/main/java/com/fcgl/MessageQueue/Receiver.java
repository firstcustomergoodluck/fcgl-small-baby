//package com.fcgl.MessageQueue;
//
////With any messaging-based application, you need to create a receiver that will respond
//// to published messages
//
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.CountDownLatch;//A synchronization aid that allows one or more thread sot wait
////until a set of operations being performed in other threads complete
//
////A CountDownLatch is initialized with a given count. The await methods block until the current count reaches
////due to invocations of the countDown() method, after which all waiting threads are released and
////any subsequent invocations of await return immediately. This is a one-shot phenomenon -- the count
////cannot be reset. If you need a version that resets the count, consider using a CuclicBarrier.
//@Component
//public class Receiver {
//
//    //The Receiver is a simple POJO that defines a method for receiving messages. When you register it
//    // to receive messages, you can name it anything you want.
//
//    //For convenience, this POJO also has a CountDownLatch. THis allows it to signal that the message
//    //is received. This si something you are not likely to implement in a production application.
//    private CountDownLatch latch = new CountDownLatch(1);
//
//    public void receiveMessage(String message) {
//        System.out.println("Jean Paul Received <" + message + ">");
//        latch.countDown();
//    }
//
//    public CountDownLatch getLatch() {
//        return latch;
//    }
//}
//
//
////Register the listener and send a message
//
////Springs AMQP's RabbitTemplate provides everything you need to send and receive messages with RabbitMQ,
////Specifically, you need to configure:
//
////* A message listerner container
////* Declare the queue, the exchange, and the binding between them
////* A component to send some messages to test the listener
//
//
////Spring Boot automatically creates a connection factory and a RabbitTemplate, reducing the amount of code you have to write
//
////You'll use RabbitTemplate to send messages, and you will register a Receiver with the message listerner container
////to receive messages. The connection factory drives both, allowing them to connect to the RabbitMQ server.