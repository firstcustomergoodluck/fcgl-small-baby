package com.fcgl.Listing.MessageQueueReceiveListing.Messages;

/**
 * Data from RabbitMQ message required to build an IProductInformation object.
 */
public class ProductInformationMessage {
  private Integer vendorId;
  private String sku;
  private Integer quantity;
  private String title;
  private String description;
  private String currency;
  private String barcode;
  private String barcodeType;
  private Double price;
  private Integer latency;

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public void setBarcodeType(String barcodeType) {
    this.barcodeType = barcodeType;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLatency(Integer latency) {
    this.latency = latency;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setVendorId(Integer vendorId) {
    this.vendorId = vendorId;
  }

  public Integer getLatency() {
    return latency;
  }

  public Double getPrice() {
    return price;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public Integer getVendorId() {
    return vendorId;
  }

  public String getBarcode() {
    return barcode;
  }

  public String getBarcodeType() {
    return barcodeType;
  }

  public String getCurrency() {
    return currency;
  }

  public String getDescription() {
    return description;
  }

  public String getSku() {
    return sku;
  }

  public String getTitle() {
    return title;
  }
}
