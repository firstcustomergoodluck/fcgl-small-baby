package com.fcgl.Listing;

/**
 * Interface for Generating a Feed on an e-commerce site.
 */
public interface IGenerateFeed {

    Response generateFeed() throws InterruptedException;

}
