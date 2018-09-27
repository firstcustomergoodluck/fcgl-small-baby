package com.fcgl.Listing.Vendors;

import java.util.Objects;

public class Price {

  private Currency currency;
  private double price;
  public Price(Currency currency, double price) {
    Objects.requireNonNull(currency);
    this.currency = currency;
    this.price = price;
  }

  public double getPrice() {
    return price;
  }

  public Currency getCurrency() {
    return currency;
  }
}
