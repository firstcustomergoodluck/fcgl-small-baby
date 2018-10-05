package com.fcgl.Listing.Vendors;

import com.fcgl.Listing.Vendors.Amazon.SubmitFeed.GenerateFeedAMZ;
import com.fcgl.Listing.Vendors.model.IProductInformation;

import java.util.List;

import static com.fcgl.Listing.Vendors.Vendor.AMAZON;

public class VendorFactory {
    //TODO: Should I start a thread for every vendor? It's very linear rn? Probably don't have to think about this until much later

    public void vendorListingFactory(Vendor vendor, List<IProductInformation> productInformations, String requestId) {
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
