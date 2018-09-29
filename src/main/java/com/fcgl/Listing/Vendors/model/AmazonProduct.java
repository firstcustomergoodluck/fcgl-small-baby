package com.fcgl.Listing.Vendors.model;

import java.util.Objects;

public class AmazonProduct implements IProductInformation<Inventory, Product, Price> {

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

  @Override
  public String getSKU() {
    return this.SKU;
  }

  @Override
  public Inventory getInventory() {
    return this.inventory;
  }

  @Override
  public Product getProduct() {
    return this.product;
  }

  @Override
  public Price getPrice() {
    return this.price;
  }
}
