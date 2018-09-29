package com.fcgl.Listing.Vendors.model;

/**
 * Order by alphabetical when add new enum.
 */
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
