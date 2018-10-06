package com.fcgl.MessageQueue;

import com.fcgl.Listing.Vendors.model.IProductInformation;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.List;

public interface IMessageQueueReceiver {

    void processMessages();
    void receive(Channel channel, Connection connection);
}
