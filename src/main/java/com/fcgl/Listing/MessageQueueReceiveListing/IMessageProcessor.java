package com.fcgl.Listing.MessageQueueReceiveListing;

import com.fcgl.Listing.MessageQueueReceiveListing.Response.MessageProcessorResponse;
import com.fcgl.Listing.MessageQueueReceiveListing.Response.MessageToProductInformationResponse;
import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.IProductInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IMessageProcessor {

    MessageProcessorResponse processMessages();

    /**
     * Extracts data from a String and uses that data to create an IProductInformation Object
     * @param message: Json formatted String
     * @return The IProductInformation Object that it generated from the message.
     */
    MessageToProductInformationResponse messageToProductInformation(String message);

    HashMap<String, Integer> getVendorSKUIndexLocation();
    HashMap<Vendor, ArrayList<IProductInformation>> getVendorProductInformation();
    List<String> getBadRequests();
}
