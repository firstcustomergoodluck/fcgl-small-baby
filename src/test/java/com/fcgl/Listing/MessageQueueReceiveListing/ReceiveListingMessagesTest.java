package com.fcgl.Listing.MessageQueueReceiveListing;

import org.junit.jupiter.api.BeforeEach;

public class ReceiveListingMessagesTest {

  //TODO: Mock RabbitMQ
  ReceiveListingMessages receiveListingMessages;

  @BeforeEach
  void setUp() {
    receiveListingMessages = new ReceiveListingMessages();
  }
}

