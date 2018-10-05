package com.fcgl.Listing.MessageQueueReceiveListing;

import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.IProductInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MessageProcessorResponse {
    private HashMap<Vendor,ArrayList<IProductInformation>> vendorProductInformation;
    private List<String> badRequests;
    private List<Vendor> vendors;

    public MessageProcessorResponse(HashMap<Vendor,ArrayList<IProductInformation>> vendorProductInformation, List<String> badRequests, List<Vendor> vendors) {
        Objects.requireNonNull(vendorProductInformation);
        Objects.requireNonNull(badRequests);
        this.vendorProductInformation = vendorProductInformation;
        this.badRequests = badRequests;
        this.vendors = vendors;
    }


    public HashMap<Vendor, ArrayList<IProductInformation>> getVendorProductInformation() {
        return vendorProductInformation;
    }

    public List<String> getBadRequests() {
        return badRequests;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

}
