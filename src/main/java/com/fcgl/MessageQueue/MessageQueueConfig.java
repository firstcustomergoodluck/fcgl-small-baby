package com.fcgl.MessageQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Initialized a Connection and Channel for RabbitMQ
 */
public class MessageQueueConfig {

    private Connection connection;
    private Channel channel;
    private Boolean isSuccessfulConnection = false;

    public MessageQueueConfig() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        setChannelConnection(factory);
    }

    public Channel getChannel() {
        return channel;
    }

    public Connection getConnection() {
        return connection;
    }

    public Boolean getIsSuccessfulConnection() {
        return isSuccessfulConnection;
    }

    public void closeConnection() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    private void setChannelConnection(ConnectionFactory factory) {
        try {
            connection = factory.newConnection();
        } catch (Exception e) {
            //TODO: Log this
            connection = null;
            channel = null;
            return;
        }

        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            //TODO: Log this
            connection = null;
            channel = null;
        }

        isSuccessfulConnection = true;
    }

}
