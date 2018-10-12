package com.fcgl.Listing.Vendors.Factories;

import com.fcgl.Listing.Vendors.Vendor;

/**
 * Starts Vendor specific tasks
 */
public interface IVendorFactory {

  /**
   * Factory for vendor specific tasks
   *
   * @param vendor: The vendor whose task will begin
   * @param requestId: A unique id for the task
   */
  void vendorFactory(Vendor vendor, String requestId);
}
