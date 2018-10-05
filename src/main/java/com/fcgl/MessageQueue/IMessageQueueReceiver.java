package com.fcgl.MessageQueue;

import com.fcgl.Listing.Vendors.model.IProductInformation;

import java.util.List;

public interface IMessageQueueReceiver {

    /**
     * Gets messages from message queue and maps them to ProductInformation objects.
     * @param message: message returned from message queue
     * @return A list of ProductInformation objects
     */
    List<IProductInformation> getProductInformation(String message);

}
