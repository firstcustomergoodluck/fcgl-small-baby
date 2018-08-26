package com.fcgl.Listing.Vendors;

import com.fcgl.Listing.Response.Response;

/**
 * Interface for Generating a Feed on an e-commerce site.
 */
public interface IGenerateFeed {

    Response generateFeed() throws InterruptedException;

}
