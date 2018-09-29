package com.fcgl.Listing.Vendors.model;

/**
 * ProductInformation holds the basic product data that is needed in order to make a feed on an e-commerce website
 * productMisc: Holds data specific to a particular category.
 *              Example: A book would have an author but an Electronic Device would not.
 */
public interface IProductInformation<Inventory,Product, Price> {

  public String getSKU();

  public Inventory getInventory();

  public Product getProduct();

  public Price getPrice();
}
