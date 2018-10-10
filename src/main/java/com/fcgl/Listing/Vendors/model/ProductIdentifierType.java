package com.fcgl.Listing.Vendors.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Order by alphabetical when add new enum.
 */
public enum ProductIdentifierType {
  ISBN("ISBN");

  private String name;

  private ProductIdentifierType(String name) {
    this.name = name;
  }

  private static final Map<String, ProductIdentifierType> productIdentifierTypeMap = new HashMap<>(
      ProductIdentifierType.values().length);

  static {
    for (ProductIdentifierType vendor : ProductIdentifierType.values()) {
      productIdentifierTypeMap.put(vendor.getName(), vendor);
    }
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return this.getName();
  }

  public static ProductIdentifierType getProductIdentifierType(String name) {
    return productIdentifierTypeMap.get(name);
  }
}
