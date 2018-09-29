package com.fcgl.Listing.Vendors.model;

/**
 * Order by alphabetical.
 */
public enum Currency {
  USD("USD");

  private String name;
  private Currency(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return this.getName();
  }
}
