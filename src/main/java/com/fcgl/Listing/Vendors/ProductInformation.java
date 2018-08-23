package com.fcgl.Listing.Vendors;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * ProductInformation holds the basic product data that is needed in order to make a feed on an e-commerce website
 * productMisc: Holds data specific to a particular category. Example: A book would have an author but an
 *              Electronic device would not.
 */
public class ProductInformation {
        private final String ERROR_MESSAGE = "ProductInformation has an empty %s";
        private int SKU;
        private String standardProductIDType;
        private String standardProductIDValue;
        private String productTaxCode;
        private String title;
        private String description;
        private LinkedList<String> bulletPoint;
        private String itemType;
        private HashMap<String, String> productMisc;
        //TODO: Should we use quantity or availability?

    public ProductInformation(int SKU, String standardProductIDType, String standardProductIDValue, String title,
                       String description, LinkedList<String> bulletPoint, String itemType,
                       HashMap<String, String> productMisc, String productTaxCode) {

        isBadConstructor(SKU, standardProductIDType, standardProductIDValue, title, description, bulletPoint, itemType,
                productMisc, productTaxCode);

        this.SKU = SKU;
        this.standardProductIDType = standardProductIDType;
        this.standardProductIDValue = standardProductIDValue;
        this.title = title;
        this.description = description;
        this.bulletPoint = bulletPoint;
        this.itemType = itemType;
        this.productMisc = productMisc;
        this.productTaxCode = productTaxCode;
    }

    public int getSKU() {
        return this.SKU;
    }


    public String getStandardProductIDType() {
        return this.standardProductIDType;
    }

    public String getStandardProductIDValue() {
        return this.standardProductIDValue;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public LinkedList<String> getBulletPoint() {
        return this.bulletPoint;
    }

    public String getItemType() {
        return this.itemType;
    }

    public HashMap<String, String> getProductMisc() {
        return this.productMisc;
    }

    public String getProductTaxCode() {
        return this.productTaxCode;
    }

    private void isBadInt(int value, String variable) {
        if (value < 1)
            throw new NullPointerException(String.format(ERROR_MESSAGE, variable));
    }

    private void isBadString(String value, String variable) {
        if (value == null || value.length() == 0) {
            throw new NullPointerException(String.format(ERROR_MESSAGE, variable));
        }
    }

    private void isBadHashMap(HashMap<String, String> productMisc, String variable) {
        if(productMisc == null) {
            throw new NullPointerException(String.format(ERROR_MESSAGE, variable));
        }
    }

    private void isBadLinkedList(LinkedList bulletPoint, String variable) {
        if (bulletPoint == null) {
            throw new NullPointerException(String.format(ERROR_MESSAGE, variable));
        }
    }

    private void isBadConstructor(int SKU, String standardProductIDType, String standardProductIDValue, String title,
                                  String description, LinkedList<String> bulletPoint, String itemType,
                                  HashMap<String, String> productMisc, String productTaxCode) {
        isBadInt(SKU, "SKU");
        isBadString(standardProductIDType, "Standard Product ID Type");
        isBadString(standardProductIDValue, "Standard Product ID Value");
        isBadString(title, "Title");
        isBadString(description, "Description");
        isBadLinkedList(bulletPoint, "Bullet Point");
        isBadString(itemType, "Item Type");
        isBadHashMap(productMisc, "Product Misc");
        isBadString(productTaxCode, "Product Tax Code");
    }
}
