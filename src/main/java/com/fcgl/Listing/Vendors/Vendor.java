package com.fcgl.Listing.Vendors;

import java.util.HashMap;
import java.util.Map;

public enum Vendor {
    AMAZON(1);
    private static final Map<Integer, Vendor> vendorMap = new HashMap<>(Vendor.values().length);

    static {
        for (Vendor vendor : Vendor.values()) {
            vendorMap.put(vendor.getId(), vendor);
        }
    }

    private Integer id;
    Vendor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Vendor getVendor(Integer vendor) {
        return vendorMap.get(vendor);
    }


}
