package com.fcgl.Listing.Vendors;

import java.util.Objects;

public class AmazonProduct {

  private String SKU;
  private Inventory inventory;
  private Product product;
  private Price price;

  public AmazonProduct(String SKU, Product product, Price price, Inventory inventory) {
    Objects.requireNonNull(SKU);
    Objects.requireNonNull(product);
    Objects.requireNonNull(price);
    Objects.requireNonNull(inventory);
    this.SKU = SKU;
    this.product = product;
    this.price = price;
    this.inventory = inventory;
  }

  public String getSKU() {
    return this.SKU;
  }

  public Inventory getInventory() {
    return this.inventory;
  }

  public Product getProduct() {
    return this.product;
  }

  public Price getPrice() {
    return this.price;
  }
}
