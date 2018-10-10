package com.fcgl.Listing.MessageQueueReceiveListing.Response;

import com.fcgl.Listing.MessageQueueReceiveListing.State;
import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.IProductInformation;
import java.util.Objects;

/**
 * Response for messageToProductInformation method.
 */
public class MessageToProductInformationResponse {

  private IProductInformation productInformation;
  private Vendor vendor;
  private String vendorSKUId;
  private State state;
  private Integer quantity;

  public MessageToProductInformationResponse(State state) {
    Objects.requireNonNull(state);
    this.state = state;
  }

  public MessageToProductInformationResponse(IProductInformation productInformation, Vendor vendor,
      String vendorSKUId, Integer quantity) {
    Objects.requireNonNull(productInformation);
    Objects.requireNonNull(vendor);
    Objects.requireNonNull(vendorSKUId);
    Objects.requireNonNull(quantity);
    this.productInformation = productInformation;
    this.vendor = vendor;
    this.quantity = quantity;
    this.state = State.UPDATE;
  }

  public MessageToProductInformationResponse(IProductInformation productInformation, Vendor vendor,
      String vendorSKUId, State state) {
    Objects.requireNonNull(productInformation);
    Objects.requireNonNull(vendor);
    Objects.requireNonNull(vendorSKUId);
    Objects.requireNonNull(state);
    this.productInformation = productInformation;
    this.vendor = vendor;
    this.vendorSKUId = vendorSKUId;
    this.state = state;
  }

  public IProductInformation getProductInformation() {
    return productInformation;
  }

  public Vendor getVendor() {
    return vendor;
  }

  public String getVendorSKUId() {
    return vendorSKUId;
  }

  public State getState() {
    return state;
  }

  public Integer getQuantity() {
    return quantity;
  }
}
