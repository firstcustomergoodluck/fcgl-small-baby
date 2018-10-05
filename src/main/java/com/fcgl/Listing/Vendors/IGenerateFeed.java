package com.fcgl.Listing.Vendors;

import com.fcgl.Listing.Response.Response;
import com.fcgl.Listing.Vendors.model.IProductInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for Generating a Feed on an e-commerce site.
 */
public interface IGenerateFeed {

    Response generateFeed(List<IProductInformation> productInformation) throws InterruptedException;

}
