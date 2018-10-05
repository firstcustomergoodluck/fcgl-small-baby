package com.fcgl.Listing.MessageQueueReceiveListing;

import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.*;
import com.fcgl.Listing.Vendors.model.Currency;
import com.google.gson.Gson;

import javax.validation.constraints.Null;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Processes Messages and maps them to IProductInformation objects
 */
public class MessageProcessor {
    private List<String> messages;
    private HashMap<Vendor, ArrayList<IProductInformation>> vendorProductInformation = new HashMap<>();
    private HashMap<String, Integer> vendorSKUIndexLocation = new HashMap<>();
    private List<String> badRequests = new ArrayList<>();
    //TODO: Should populate from database?
    private String[] requiredParameters = {"vendorId", "sku", "quantity", "title", "description", "currency", "barcode", "barcodeType", "price"};
    private List<Vendor> vendors = new ArrayList<>();

    /**
     * Constructor
     * @param messages: Messages that will be mapped to IProductInformation Objects
     */
    public MessageProcessor(List<String> messages) {
        this.messages = messages;
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

    /**
     * Processes the List of messages passed in the constructor.
     * @return MessageProcessorResponse: List of good requests, bad requests, and requests that need to be requeued
     */
    public MessageProcessorResponse processMessages() {
        for (String message : messages) {
            MessageToProductInformationResponse productInfo;
            try {
                productInfo = messageToProductInformation(message);
            } catch(NullPointerException e) {
                getBadRequests().add(message);
                continue;
            }
            switch (productInfo.getState()) {
                case INSERT:
                    addProductInformation(productInfo.getProductInformation(), productInfo.getVendor(), productInfo.getVendorSKUId());
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
     * @param message: Message received from the message queue
     * @return The IProductInformation Object that it generated from the message.
     */
    MessageToProductInformationResponse messageToProductInformation(String message) throws NullPointerException {

        HashMap result = new Gson().fromJson(message, HashMap.class);

        if (!validateNotNull(result, this.requiredParameters)) {
            return new MessageToProductInformationResponse(State.ERROR);
        }

        Integer vendorID = ((Double) result.get("vendorId")).intValue();
        String SKU = (String) result.get("sku");
        Integer quantity = ((Double) result.get("quantity")).intValue();
        String vendorSKUId = vendorID + "_" + SKU;
        Vendor vendor = Vendor.getVendor(vendorID);

        if (getVendorSKUIndexLocation().containsKey(vendorSKUId)) {
            Integer index = getVendorSKUIndexLocation().get(vendorSKUId);
            IProductInformation productInformation = getVendorProductInformation().get(vendor).get(index);
            return new MessageToProductInformationResponse(productInformation, vendor, vendorSKUId, quantity);
        } else {
            String title = (String) result.get("title");
            String description = (String) result.get("description");
            String currency = (String) result.get("currency");
            String barcode = (String) result.get("barcode");
            String barcodeType = (String) result.get("barcodeType");
            Double productPrice = (Double) result.get("price");
            Integer fulfillLatency = ((Double) result.get("latency")).intValue();//TODO: Not sure if I need this
            ProductIdentifierType productIdentifierType = ProductIdentifierType.getProductIdentifierType(barcodeType);
            ProductIdentifier productIdentifier = new ProductIdentifier(productIdentifierType, barcode);
            ProductDescriptionData productDescriptionData = new ProductDescriptionData(title, description);
            Currency currencyEnum = Currency.getCurrency(currency);
            Price price = new Price(currencyEnum, productPrice);
            Inventory inventory = new Inventory(quantity, fulfillLatency);
            Product product = new Product(productIdentifier, productDescriptionData);
            IProductInformation productInformation = new AmazonProduct(SKU, product, price, inventory);
            return new MessageToProductInformationResponse(productInformation, vendor, vendorSKUId, State.INSERT);
        }
    }

    /**
     * Adds a ProductInformation object to the vendorProductInformation Hash Map.
     * @param vendor: The vendor for which the ProductInformation is being added to
     * @param product: The ProductInformation being added to the vendor
     */
    private void addProductInformation(IProductInformation product, Vendor vendor,String vendorSKUId) {
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
     * Adds a Key-Value pair to vendorSKUIndexLocation if one doesn't exist. Parameter vendorSKUIndexLocation keeps
     * track of where in the vendorProductInformation a particular IProductInformation object exists.
     * @param vendorSKUId: unique id for an vendor-IProductInformation pair.
     * @param index: Where the IProductInformation is located in vendorProductInformation for a particular vendor
     */
    private void addProductInformationSKUIndexLocation(String vendorSKUId, Integer index) {
        if (!vendorSKUIndexLocation.containsKey(vendorSKUId)) {
            vendorSKUIndexLocation.put(vendorSKUId, index);
        }
    }

    /**
     * Validates that message contains all required parameters
     * @param messageMap: message map being evaluated
     * @param requiredParameters: list of parameters required
     * @return true if a valid message | false if missing any required parameters
     */
    private Boolean validateNotNull(HashMap messageMap, String[] requiredParameters) {
        try {
            for (String value : requiredParameters) {
                Objects.requireNonNull(messageMap.get(value));
            }
            return true;
        } catch(NullPointerException e) {
            return false;
        }
    }

    /**
     * Response for messageToProductInformation method.
     */
    public class MessageToProductInformationResponse {

        private IProductInformation productInformation;
        private Vendor vendor;
        private String vendorSKUId;
        private State state;
        private Integer quantity;

        private MessageToProductInformationResponse(State state) {
            Objects.requireNonNull(state);
            this.state = state;
        }

        private MessageToProductInformationResponse(IProductInformation productInformation, Vendor vendor, String vendorSKUId, Integer quantity) {
            Objects.requireNonNull(productInformation);
            Objects.requireNonNull(vendor);
            Objects.requireNonNull(vendorSKUId);
            Objects.requireNonNull(quantity);
            this.productInformation = productInformation;
            this.vendor = vendor;
            this.quantity = quantity;
            this.state = State.UPDATE;
        }

        private MessageToProductInformationResponse(IProductInformation productInformation, Vendor vendor, String vendorSKUId, State state) {
            Objects.requireNonNull(productInformation);
            Objects.requireNonNull(vendor);
            Objects.requireNonNull(vendorSKUId);
            Objects.requireNonNull(state);
            this.productInformation = productInformation;
            this.vendor = vendor;
            this.vendorSKUId = vendorSKUId;
            this.state = state;
        }

        public IProductInformation getProductInformation() {
            return productInformation;
        }

        public Vendor getVendor() {
            return vendor;
        }

        public String getVendorSKUId() {
            return vendorSKUId;
        }

        public State getState() {
            return state;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }

    public enum State {
        INSERT, UPDATE, ERROR
    }
}
