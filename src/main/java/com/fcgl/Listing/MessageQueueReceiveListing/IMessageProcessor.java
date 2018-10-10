package com.fcgl.Listing.MessageQueueReceiveListing;

import com.fcgl.Listing.MessageQueueReceiveListing.Response.MessageProcessorResponse;
import com.fcgl.Listing.MessageQueueReceiveListing.Response.MessageToProductInformationResponse;
import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.IProductInformation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IMessageProcessor {

  /**
   * Decides what list a message should go to. Successful Process: If a message was able to be
   * mapped to an IProductInformation Object There are three STATES that a message can be in (The
   * state can be obtained from the MessageProcessorResponse Object) ERROR: If there was not a
   * successful process. Message will be added to the badRequest List. INSERT: If a message was
   * successfully processed and it has not been added to the requested vendor UPDATE: If a message
   * was successfully processed and it has already been added to the requested Vendor
   *
   * @return MessageProcessorResponse
   */
  MessageProcessorResponse processMessages();

  /**
   * Extracts data from a String and uses that data to create an IProductInformation Object
   *
   * @param message: Json formatted String
   * @return The IProductInformation Object that it generated from the message.
   */
  MessageToProductInformationResponse messageToProductInformation(String message);

  /**
   * HashMap of String to Integer. This HahMap keeps track of the index location for a particular
   * item for a particular vendor specific Suggesting: The Key should be a unique representation of
   * the SKU and the vendorId.
   *
   * @return HashMap
   */
  HashMap<String, Integer> getVendorSKUIndexLocation();

  /**
   * HashMap of Vendor to List of IProductInformation. These are the products that a particular
   * vendor will soon have up for sale.
   *
   * @return HashMap
   */
  HashMap<Vendor, ArrayList<IProductInformation>> getVendorProductInformation();

  /**
   * List of messages that were not able to be mapped to IProductInformation Objects. These messages
   * should be queued to an ERROR queue for further evaluation.
   *
   * @return List of messages (Strings)
   */
  List<String> getBadRequests();
}
