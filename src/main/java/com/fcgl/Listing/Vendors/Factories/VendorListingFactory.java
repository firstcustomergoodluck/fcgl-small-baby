package com.fcgl.Listing.Vendors.Factories;

import com.fcgl.Listing.Vendors.Amazon.Feed.SubmitFeed.GenerateFeedAMZ;
import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.IProductInformation;
import com.fcgl.MessageQueue.IMessageQueueSender;
import java.util.List;

/**
 * Generates listings for a specified vendor.
 */
public class VendorListingFactory implements IVendorFactory {

  private List<IProductInformation> productInformations;
  private IMessageQueueSender messageQueueSender;

  /**
   * Initializer
   * @param productInformations: The Products being listed for a particular vendor
   */
  public VendorListingFactory(List<IProductInformation> productInformations,
      IMessageQueueSender messageQueueSender) {
    this.productInformations = productInformations;
    this.messageQueueSender = messageQueueSender;
  }

  /**
   * Factory that generates a listing for a particular vendor
   * @param vendor: The vendor whose task will begin
   * @param requestId: A unique id for the task
   */
  public void vendorFactory(Vendor vendor, String requestId) {
    switch (vendor) {
      case AMAZON:
        GenerateFeedAMZ generateFeedAMZ = new GenerateFeedAMZ(requestId, messageQueueSender);
        generateFeedAMZ.generateFeed(productInformations);//TODO: I should do something with the response I get from here... At the very least log it...
        break;
      default:
        break;
    }
  }
}
