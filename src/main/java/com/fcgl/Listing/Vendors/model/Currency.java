package com.fcgl.Listing.Vendors.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Order by alphabetical.
 */
public enum Currency {
  USD("USD");

  private String name;
  private Currency(String name) {
    this.name = name;
  }
  private static final Map<String, Currency> currencyMap = new HashMap<>(Currency.values().length);

  static {
    for (Currency currency : Currency.values()) {
      currencyMap.put(currency.name(), currency);
    }
  }


  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return this.getName();
  }

  public static Currency getCurrency(String currency) {
    return currencyMap.get(currency);
  }





}
