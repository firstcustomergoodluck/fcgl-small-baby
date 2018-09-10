package com.fcgl.MessageQueue;

import com.fcgl.Listing.Vendors.ProductInformation;

import java.util.List;

public interface IMessageQueueReceiver {

    /**
     * TODO: Do we maybe want to keep the returned message somewhere??? In case something is lost?
     * Gets messages from message queue and maps them to ProductInformation objects.
     * @param message: message returned from message queue
     * @return A list of ProductInformation objects
     */
    List<ProductInformation> getProductInformation(String message);

}
