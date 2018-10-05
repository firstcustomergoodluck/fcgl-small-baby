package com.fcgl.MessageQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//TODO: Where should we instantiate the queue names???
//TODO: Requirements.... When a queue is too big we want to move some messages to another queue
public class MessageQueueConfig {

    private Connection connection;
    private Channel channel;

    public MessageQueueConfig() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public Channel getChannel() {
        return channel;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

}
