package com.fcgl.MessageQueue;

import java.util.List;

public interface IMessageQueueSender {

  /**
   * Inserts a message into a message queue
   *
   * @param message: The message being inserted into the message queue
   */
  void sendMessage(String message, String queueName);

  /**
   * Inserts multiple messages into a message queue
   *
   * @param messages: the messages being inserted into the message queue
   */
  void bulkSend(List<String> messages, String queueName);

}
