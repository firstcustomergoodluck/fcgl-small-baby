package com.fcgl.MessageQueue;

import com.fcgl.Listing.Vendors.model.IProductInformation;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.List;

public interface IMessageQueueReceiver {

    /**
     * Processes the messages from the message queue and maps it to an Object
     */
    void processMessages();

    /**
     * Receive messages from the message queue
     */
    void receive(Channel channel, Connection connection);
}
