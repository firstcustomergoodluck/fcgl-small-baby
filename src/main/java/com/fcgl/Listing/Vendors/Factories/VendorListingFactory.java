package com.fcgl.Listing.Vendors.Factories;

import com.fcgl.Listing.Vendors.Amazon.SubmitFeed.GenerateFeedAMZ;
import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.IProductInformation;
import java.util.List;

/**
 * Generates listings to specified vendors.
 */
public class VendorListingFactory implements IVendorFactory {

  private List<IProductInformation> productInformations;

  public VendorListingFactory(List<IProductInformation> productInformations) {
    this.productInformations = productInformations;
  }

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
