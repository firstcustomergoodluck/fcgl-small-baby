package com.fcgl.Listing.Vendors;

public class Product {

  private ProductIdentifier productIdentifier;
  private ProductDescriptionData productDescriptionData;
  public Product(ProductIdentifier productIdentifier, ProductDescriptionData productDescriptionData) {
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
