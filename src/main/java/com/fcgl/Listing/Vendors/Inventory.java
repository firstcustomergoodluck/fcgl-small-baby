package com.fcgl.Listing.Vendors;

public class Inventory {

  int quantity;
  int fulfillLatency;
  public Inventory(int quantity, int fulfillLatency) {
    this.quantity = quantity;
    this.fulfillLatency = fulfillLatency;
  }

  public int getFulfillLatency() {
    return this.fulfillLatency;
  }

  public int getQuantity() {
    return this.quantity;
  }

}
