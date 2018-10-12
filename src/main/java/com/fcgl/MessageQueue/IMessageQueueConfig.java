package com.fcgl.MessageQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface IMessageQueueConfig {

  Channel getChannel();

  Connection getConnection();

  Boolean getIsSuccessfulConnection();

  void closeConnection() throws IOException, TimeoutException;

}
