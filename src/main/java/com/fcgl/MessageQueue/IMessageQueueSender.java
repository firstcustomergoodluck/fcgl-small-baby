package com.fcgl.MessageQueue;

import java.util.List;

public interface IMessageQueueSender {

    /**
     * Inserts a message into a message queue
     * TODO: Need to be able to defirentiate between queues... I don't like how abstracted this is
     * @param message: The message being inserted into the message queue
     */
    void sendMessage(String message);

    /**
     * Inserts multiple messages into a message queue
     * @param messages: the messages being inserted into the message queue
     */
    void bulkSend(List<String> messages);

}
