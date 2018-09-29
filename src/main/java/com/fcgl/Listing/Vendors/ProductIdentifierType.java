package com.fcgl.Listing.Vendors;

public enum ProductIdentifierType {
  ISBN("ISBN");

  private String name;
  private ProductIdentifierType(String name) {
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
