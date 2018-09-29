package com.fcgl.Listing.Vendors;


/**
 * TODO: Redesign and use this as a base for product data and add more info as amazon support
 */
public class ProductDescriptionData {

  private String title;
  private String description;
  public ProductDescriptionData(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public String getTitle() {
    return title;
  }
}
