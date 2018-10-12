package com.fcgl.Listing.MessageQueueReceiveListing;

import com.fcgl.Listing.MessageQueueReceiveListing.Messages.ProductInformationMessage;
import com.fcgl.Listing.MessageQueueReceiveListing.Response.MessageProcessorResponse;
import com.fcgl.Listing.MessageQueueReceiveListing.Response.MessageToProductInformationResponse;
import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.AmazonProduct;
import com.fcgl.Listing.Vendors.model.Currency;
import com.fcgl.Listing.Vendors.model.IProductInformation;
import com.fcgl.Listing.Vendors.model.Inventory;
import com.fcgl.Listing.Vendors.model.Price;
import com.fcgl.Listing.Vendors.model.Product;
import com.fcgl.Listing.Vendors.model.ProductDescriptionData;
import com.fcgl.Listing.Vendors.model.ProductIdentifier;
import com.fcgl.Listing.Vendors.model.ProductIdentifierType;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Processes Messages (Strings) and maps them to IProductInformation objects
 */
public class MessageProcessor implements IMessageProcessor {

  private List<String> messages;
  private HashMap<Vendor, ArrayList<IProductInformation>> vendorProductInformation = new HashMap<>();
  private HashMap<String, Integer> vendorSKUIndexLocation = new HashMap<>();
  private List<String> badRequests = new ArrayList<>();
  private List<Vendor> vendors = new ArrayList<>();

  /**
   * Constructor
   *
   * @param messages: Messages that will be mapped to IProductInformation Objects
   */
  public MessageProcessor(List<String> messages) {
    this.messages = messages;
  }

  /**
   * Processes the List of messages passed in the constructor.
   *
   * @return MessageProcessorResponse: List of good requests, and bad requests
   */
  public MessageProcessorResponse processMessages() {
    for (String message : messages) {
      MessageToProductInformationResponse productInfo = messageToProductInformation(message);
      switch (productInfo.getState()) {
        case INSERT:
          addProductInformation(productInfo.getProductInformation(), productInfo.getVendor(),
              productInfo.getVendorSKUId());
          break;
        case UPDATE:
          IProductInformation productInformation = productInfo.getProductInformation();
          Inventory productInformationInventory = (Inventory) productInformation.getInventory();
          productInformationInventory.increaseQuantity(productInfo.getQuantity());
          break;
        case ERROR:
          getBadRequests().add(message);
          break;
      }
    }
    return new MessageProcessorResponse(vendorProductInformation, badRequests, vendors);
  }

  /**
   * Extracts data from a String and uses that data to create a ProductInformation Object
   *
   * @param message: Json formatted String
   * @return The IProductInformation Object that it generated from the message.
   */
  public MessageToProductInformationResponse messageToProductInformation(String message) {
    Gson gson = new Gson();
    ProductInformationMessage messageObject = gson.fromJson(message, ProductInformationMessage.class);
    try {
      Integer vendorID = messageObject.getVendorId();
      String SKU = messageObject.getSku();
      Integer quantity = messageObject.getQuantity();
      String vendorSKUId = vendorID + "_" + SKU;
      Vendor vendor = Vendor.getVendor(vendorID);

      if (getVendorSKUIndexLocation().containsKey(vendorSKUId)) {
        Integer index = getVendorSKUIndexLocation().get(vendorSKUId);
        IProductInformation productInformation = getVendorProductInformation().get(vendor)
            .get(index);
        return new MessageToProductInformationResponse(productInformation, vendor, vendorSKUId,
            quantity);
      } else {
        String title = messageObject.getTitle();
        String description = messageObject.getDescription();
        String currency = messageObject.getCurrency();
        String barcode = messageObject.getBarcode();
        String barcodeType = messageObject.getBarcodeType();
        Double productPrice = messageObject.getPrice();
        Integer fulfillLatency = messageObject.getLatency();//TODO: Not sure if I need this
        ProductIdentifierType productIdentifierType = ProductIdentifierType
            .getProductIdentifierType(barcodeType);
        ProductIdentifier productIdentifier = new ProductIdentifier(productIdentifierType, barcode);
        ProductDescriptionData productDescriptionData = new ProductDescriptionData(title,
            description);
        Currency currencyEnum = Currency.getCurrency(currency);
        Price price = new Price(currencyEnum, productPrice);
        Inventory inventory = new Inventory(quantity, fulfillLatency);
        Product product = new Product(productIdentifier, productDescriptionData);
        IProductInformation productInformation = new AmazonProduct(SKU, product, price, inventory);
        return new MessageToProductInformationResponse(productInformation, vendor, vendorSKUId,
            State.INSERT);
      }
    } catch (NullPointerException e) {
      return new MessageToProductInformationResponse(State.ERROR);
    }
  }

  /**
   * Adds a ProductInformation object to the vendorProductInformation Hash Map.
   *
   * @param vendor: The vendor for which the ProductInformation is being added to
   * @param product: The ProductInformation being added to the vendor
   */
  private void addProductInformation(IProductInformation product, Vendor vendor,
      String vendorSKUId) {
    ArrayList<IProductInformation> productInformationList;
    if (vendorProductInformation.containsKey(vendor)) {
      productInformationList = vendorProductInformation.get(vendor);
      productInformationList.add(product);
    } else {
      productInformationList = new ArrayList<>();
      productInformationList.add(product);
      vendors.add(vendor);
      vendorProductInformation.put(vendor, productInformationList);
    }
    addProductInformationSKUIndexLocation(vendorSKUId, productInformationList.size() - 1);
  }

  /**
   * Adds a Key-Value pair to vendorSKUIndexLocation if one doesn't exist. Parameter
   * vendorSKUIndexLocation keeps track of where in the vendorProductInformation a particular
   * IProductInformation object exists.
   *
   * @param vendorSKUId: unique id for an vendor-IProductInformation pair.
   * @param index: Where the IProductInformation is located in vendorProductInformation for a
   * particular vendor
   */
  private void addProductInformationSKUIndexLocation(String vendorSKUId, Integer index) {
    if (!vendorSKUIndexLocation.containsKey(vendorSKUId)) {
      vendorSKUIndexLocation.put(vendorSKUId, index);
    }
  }

  public HashMap<String, Integer> getVendorSKUIndexLocation() {
    return vendorSKUIndexLocation;
  }

  public HashMap<Vendor, ArrayList<IProductInformation>> getVendorProductInformation() {
    return vendorProductInformation;
  }

  public List<String> getBadRequests() {
    return badRequests;
  }

}
