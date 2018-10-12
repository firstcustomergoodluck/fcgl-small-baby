package com.fcgl.Listing.Vendors.model;


import java.util.Objects;

/**
 * TODO: Redesign and use this as a base for product data and add more info as amazon support
 */
public class ProductDescriptionData {

  private String title;
  private String description;

  public ProductDescriptionData(String title, String description) {
    Objects.requireNonNull(title);
    Objects.requireNonNull(description);
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
