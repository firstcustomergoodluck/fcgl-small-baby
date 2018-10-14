package com.fcgl.MessageQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import java.io.IOException;
import java.util.List;

public class MessageQueueSender implements IMessageQueueSender {
  private IMessageQueueConfig messageQueueConfig;
  private int MAX_RETRY = 3;

  public MessageQueueSender (IMessageQueueConfig messageQueueConfig) {
    this.messageQueueConfig = messageQueueConfig;
  }

  @Override
  public void sendMessage(String message, String queueName) {
    Channel channel = messageQueueConfig.getChannel();
    publish(channel, queueName, message);
  }

  @Override
  public void bulkSend(List<String> messages, String queueName) {
    Channel channel = messageQueueConfig.getChannel();
    for (String message : messages) {
      if (!publish(channel, queueName, message)) {
        //TODO: log a message
        break;
      }
    }
  }

  /**
   * Publishes a message to RabbitMQ. Retries to publish if there was an error.
   * @param channel
   * @param queueName
   * @param message
   * @return
   */
  private boolean publish(Channel channel, String queueName, String message) {
    boolean success = false;
    int retry = MAX_RETRY;
    while (!success & retry != 0) {
      try {
        channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,
            message.getBytes("UTF-8"));
        success = true;
      } catch (IOException e) {
        retry--;
      }
    }
    return retry == 0;
  }

}
