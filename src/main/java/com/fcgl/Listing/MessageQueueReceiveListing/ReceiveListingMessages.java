package com.fcgl.Listing.MessageQueueReceiveListing;

import com.fcgl.Listing.MessageQueueReceiveListing.Response.MessageProcessorResponse;
import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ReceiveListingMessages: This class receives messages from RabbitMQ and processes them.
 * Retry logic is implemented in methods that throw IOExceptions
 */

public class ReceiveListingMessages {
    //TODO: Should get queueNames from database
    private static final Integer MAX_RETRY = 3;
    private static final String[] queueNames = {"listing"};
    private static final String errorQueue = "errorListing";
    private HashMap<Vendor, ArrayList<IProductInformation>> vendorProductInformation = new HashMap<>();
    private HashMap<String, Integer> vendorSKUIndexLocation = new HashMap<>();
    private List<String> messages = new ArrayList<>();
    public HashMap<String, Integer> getVendorSKUIndexLocation() {
        return vendorSKUIndexLocation;
    }

    public HashMap<Vendor, ArrayList<IProductInformation>> getVendorProductInformation() {
        return vendorProductInformation;
    }

    public void processMessages() {
        MessageProcessor messageProcessor = new MessageProcessor(messages);
        MessageProcessorResponse messageProcessorResponse = messageProcessor.processMessages();
        List<String> badMessages = messageProcessorResponse.getBadRequests();
        HashMap<Vendor, ArrayList<IProductInformation>> vendorProductInformation = messageProcessorResponse.getVendorProductInformation();
        List<Vendor> vendors = messageProcessorResponse.getVendors();

        for (Vendor vendor : vendors) {
            ArrayList<IProductInformation> productInformations = vendorProductInformation.get(vendor);
            //call factory
        }
    }

    /**
     * Extracts messages from each queue in the queueNames list.
     * @param channel:
     * @param connection:
     */
    public void receive(Channel channel, Connection connection) {
        for (String queueName : queueNames) {
            Boolean success = false;
            Integer retry = 0;
            while(!success & retry < MAX_RETRY) {
                try {
                    DeclareOk response2 = channel.queueDeclarePassive(queueName);
                    int messageCount = response2.getMessageCount();
                    channel.queueDeclare(queueName, false, false, false, null);
                    handleDelivery(messageCount, channel, queueName);
                    success = true;
                } catch(IOException e) {
                    retry++;
                }
            }
            if (retry.equals(MAX_RETRY)) {
                //log a message
                break;
            }
        }
    }


    /**
     * Receives messages from RabbitMQ and adds then to the messages list
     * @param messageCount: How many messages will be received
     * @param channel: The channel getting the messages from RabbitMQ
     * @param queueName: The name of the queue receiving messages from
     */
    private void handleDelivery(int messageCount, Channel channel, String queueName) {
        for (int i = 0; i < messageCount; i++) {
            Boolean success = false;
            Integer retry = 0;
            while (!success && retry < MAX_RETRY) {
                try {
                    GetResponse response =  channel.basicGet(queueName, false);
                    String message = new String(response.getBody());
                    long deliveryTag = response.getEnvelope().getDeliveryTag();
                    channel.basicAck(deliveryTag, false);
                    messages.add(message);
                    success = true;
                } catch(IOException e) {
                    retry++;
                }
            }
            if (retry >= 3) {
                //Log an error
                break;
            }
        }
    }

}
