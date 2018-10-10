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

  private void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * The new total inventory quantity.
   *
   * @param increaseBy the number of quantity to be added
   */
  public int increaseQuantity(int increaseBy) {
    if (increaseBy < 0) {
      final String errorMsgTemplate = "Quantity to increase is negative : %d";
      throw new IllegalArgumentException(String.format(errorMsgTemplate, increaseBy));
    }
    this.setQuantity(this.getQuantity() + increaseBy);
    return this.getQuantity();
  }

  /**
   * The new total inventory quantity.
   *
   * @param decreaseBy the number of quantity to be taking away
   * @return the
   */
  public int decreaseQuantity(int decreaseBy) {
    if (decreaseBy < 0) {
      final String errorMsgTemplate = "Quantity to decreaseBy is negative: %d";
      throw new IllegalArgumentException(String.format(errorMsgTemplate, decreaseBy));
    }

    int temp = this.getQuantity() - decreaseBy;
    if (temp < 0) {
      final String errorMsgTemplate = "Not enough quantity, Current %d; Quantity to be decrease %d";
      throw new IllegalArgumentException(
          String.format(errorMsgTemplate, this.getQuantity(), decreaseBy));
    }
    this.setQuantity(this.getQuantity() - decreaseBy);
    return this.getQuantity();
  }

}
