package com.fcgl.Listing;

import com.fcgl.Listing.Vendors.Amazon.ErrorHandlerAMZ;
import com.fcgl.Listing.Vendors.Amazon.SubmitFeed.SubmitFeedSucessAMZ;

import java.util.HashMap;

public interface IGenerateFeed {

    Response generateFeed() throws InterruptedException;

}
