package com.fcgl.Listing.Vendors.model;

import java.util.Objects;

public class Product {

  private ProductIdentifier productIdentifier;
  private ProductDescriptionData productDescriptionData;
  public Product(ProductIdentifier productIdentifier, ProductDescriptionData productDescriptionData) {
    Objects.requireNonNull(productDescriptionData);
    Objects.requireNonNull(productIdentifier);
    this.productIdentifier = productIdentifier;
    this.productDescriptionData = productDescriptionData;
  }

  public ProductDescriptionData getProductDescriptionData() {
    return productDescriptionData;
  }

  public ProductIdentifier getProductIdentifier() {
    return productIdentifier;
  }
}
