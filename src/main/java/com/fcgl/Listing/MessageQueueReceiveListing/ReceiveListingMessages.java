package com.fcgl.Listing.MessageQueueReceiveListing;

import com.fcgl.Listing.MessageQueueReceiveListing.Response.MessageProcessorResponse;
import com.fcgl.Listing.Response.IResponse;
import com.fcgl.Listing.Response.Response;
import com.fcgl.Listing.Vendors.Factories.VendorListingFactory;
import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.IProductInformation;
import com.fcgl.MessageQueue.IMessageQueueConfig;
import com.fcgl.MessageQueue.IMessageQueueReceiver;
import com.fcgl.MessageQueue.MessageQueueConfig;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ReceiveListingMessages: This class receives messages from RabbitMQ and processes them. Retry
 * logic is implemented in methods that throw IOExceptions
 */
public class ReceiveListingMessages implements IMessageQueueReceiver {

  //TODO: Should get queueNames from database
  private static final Integer MAX_RETRY = 3;
  private static final String[] QUEUE_NAMES = {"listing"};
  private static final String ERROR_QUEUE_NAME = "errorListing";
  private List<String> messages = new ArrayList<>();
  private String requestId;
  private IMessageQueueConfig messageQueueConfig;
  private boolean DURABLE_QUEUE = false;//TODO: Should be moved to a config

  /**
   * Initializer
   * @param requestId: Unique ID for the request
   */
  public ReceiveListingMessages(String requestId) {
    this.requestId = requestId;
    this.messageQueueConfig = new MessageQueueConfig();
  }

  /**
   * Receives and processes the messages from RabbitMQ
   */
  public IResponse processMessages() {
    if (messageQueueConfig.getIsSuccessfulConnection()) {
      receive(messageQueueConfig.getChannel(), messageQueueConfig.getConnection());
      MessageProcessor messageProcessor = new MessageProcessor(messages);
      MessageProcessorResponse messageProcessorResponse = messageProcessor.processMessages();
      List<String> badMessages = messageProcessorResponse.getBadRequests();
      HashMap<Vendor, ArrayList<IProductInformation>> vendorProductInformation = messageProcessorResponse
          .getVendorProductInformation();
      List<Vendor> vendors = messageProcessorResponse.getVendors();
      processGoodMessages(vendors, vendorProductInformation);
      processBadMessages(badMessages);
      Integer badMessageSize = badMessages.size();
      Integer totalMessages = messages.size();
      Integer successMessageSize = totalMessages - badMessageSize;
      String format = "ReceiveListingMessages with requestId: %s. Successful Messages: %d; Error Messages: %d";
      String message = String.format(format, requestId, successMessageSize, badMessageSize);
      return new Response(false, 200, requestId, message);
    } else {
      String message = "There was an error, see logs for more details";
      return new Response(true, 400, requestId, message);
    }
  }

  /**
   * Calls the vendorListingFactory for all the successfully mapped messages
   *
   * @param vendors: The keys for HashMao vendorProductInformation
   * @param vendorProductInformation: Contains IProductInformation objects that should be listed on
   * a particular vendor (e-commerce)
   */
  private void processGoodMessages(List<Vendor> vendors,
      HashMap<Vendor, ArrayList<IProductInformation>> vendorProductInformation) {
    for (Vendor vendor : vendors) {
      ArrayList<IProductInformation> productInformations = vendorProductInformation.get(vendor);
      VendorListingFactory vendorListingFactory = new VendorListingFactory(productInformations);
      vendorListingFactory.vendorFactory(vendor, requestId);
    }
  }

  /**
   * Sends messages to an errorQueue to investigate issue
   *
   * @param badMessages: List of messages that were not able to be mapped to IProductInformation
   * Objects
   */
  private void processBadMessages(List<String> badMessages) {
    Channel channel = messageQueueConfig.getChannel();

    for (String message : badMessages) {
      boolean success = false;
      int retry = MAX_RETRY;
      while (!success & retry != 0) {
        try {
          channel.basicPublish("", ERROR_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
          success = true;
        } catch (IOException e) {
          retry--;
        }
      }
      if (retry == 0) {
        //TODO: log a message
        break;
      }
    }
  }

  /**
   * Extracts messages from each queue in the queueNames list.
   */
  public void receive(Channel channel, Connection connection) {
    for (String queueName : QUEUE_NAMES) {
      boolean success = false;
      Integer retry = MAX_RETRY;
      while (!success & retry != 0) {
        try {
          DeclareOk response2 = channel.queueDeclarePassive(queueName);
          int messageCount = response2.getMessageCount();
          channel.queueDeclare(queueName, DURABLE_QUEUE, false, false, null);
          handleDelivery(messageCount, channel, queueName);
          success = true;
        } catch (IOException e) {
          retry--;
        }
      }
      if (retry == 0) {
        //TODO: log a message
        break;
      }
    }
  }


  /**
   * Receives messages from RabbitMQ and adds then to the messages list
   *
   * @param messageCount: How many messages will be received
   * @param channel: The channel getting the messages from RabbitMQ
   * @param queueName: The name of the queue receiving messages from
   */
  private void handleDelivery(int messageCount, Channel channel, String queueName) {
    for (int i = 0; i < messageCount; i++) {
      boolean success = false;
      Integer retry = MAX_RETRY;
      while (!success && retry != 0) {
        try {
          GetResponse response = channel.basicGet(queueName, false);
          String message = new String(response.getBody());
          long deliveryTag = response.getEnvelope().getDeliveryTag();
          channel.basicAck(deliveryTag, false);
          messages.add(message);
          success = true;
        } catch (IOException e) {
          retry--;
        }
      }
      if (retry == 0) {
        //TODO: Log an error
        break;
      }
    }
  }

}
