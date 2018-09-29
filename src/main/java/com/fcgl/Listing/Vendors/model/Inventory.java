package com.fcgl.Listing.Vendors.model;

public class Inventory {

  private int quantity;
  private int fulfillLatency;
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
