package com.fcgl.Listing.Vendors.Factories;

import com.fcgl.Listing.Vendors.Amazon.SubmitFeed.GenerateFeedAMZ;
import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.IProductInformation;
import java.util.List;

/**
 * Generates listings for a specified vendor.
 */
public class VendorListingFactory implements IVendorFactory {

  private List<IProductInformation> productInformations;

  /**
   * Initializer
   * @param productInformations: The Products being listed for a particular vendor
   */
  public VendorListingFactory(List<IProductInformation> productInformations) {
    this.productInformations = productInformations;
  }

  /**
   * Factory that generates a listing for a particular vendor
   * @param vendor: The vendor whose task will begin
   * @param requestId: A unique id for the task
   */
  public void vendorFactory(Vendor vendor, String requestId) {
    switch (vendor) {
      case AMAZON:
        GenerateFeedAMZ generateFeedAMZ = new GenerateFeedAMZ(requestId);
        generateFeedAMZ.generateFeed(productInformations);
        break;
      default:
        break;
    }
  }
}
