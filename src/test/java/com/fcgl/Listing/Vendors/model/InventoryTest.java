package com.fcgl.Listing.Vendors.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class InventoryTest {

  Inventory inventory;
  Inventory inventory2;

  @BeforeEach
  void setUp() {
    inventory = new Inventory(1, 0);
    inventory2 = new Inventory(20, 1);
  }

  @Test
  @DisplayName("Get the fulfillLatency")
  void getFulfillLatency() {
    assertEquals(0, inventory.getFulfillLatency());
    assertEquals(1, inventory2.getFulfillLatency());
  }

  @Test
  @DisplayName("Getting the inventory quantity")
  void getQuantity() {
    assertEquals(1, inventory.getQuantity());
  }

  @Test
  @DisplayName("Inventory increase by 1")
  void increaseQuantity() {
    assertEquals(2, inventory.increaseQuantity(1));
  }

  @Test
  @DisplayName("Inventory Increase by 100")
  void increaseQuantity2() {
    assertEquals(101, inventory.increaseQuantity(100));
  }

  @Test
  @DisplayName("Inventory Increase by 0")
  void increaseQuantity3() {
    assertEquals(1, inventory.increaseQuantity(0));
  }

  @Test
  @DisplayName("Test IllegalException -1")
  void increaseQuantityIllegal() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      inventory.increaseQuantity(-1);
    });
    assertEquals("Quantity to increase is negative : -1", exception.getMessage());
  }

  @Test
  @DisplayName("Test IllegalException -100")
  void increaseQuantityIllegal2() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      inventory.increaseQuantity(-100);
    });
    assertEquals("Quantity to increase is negative : -100", exception.getMessage());
  }

  @Test
  @DisplayName("Decrease Inventory by 1")
  void decreaseQuantity1() {
    assertEquals(0, inventory.decreaseQuantity(1));
  }

  @Test
  @DisplayName("Inventory decrease by 10")
  void decreaseQuantity2() {
    assertEquals(10, inventory2.decreaseQuantity(10));
  }

  @Test
  @DisplayName("The inventory decrease by is negative")
  void decreaseQuantityIllegal1() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      inventory2.decreaseQuantity(-1);
    });
    assertEquals("Quantity to decreaseBy is negative: -1",exception.getMessage());
  }

  @Test
  @DisplayName("The current inventory is less than amount wanted to decrease")
  void decreaseQuantityIllegal2() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      inventory.decreaseQuantity(2);
    });
    assertEquals("Not enough quantity, Current 1; Quantity to be decrease 2",exception.getMessage());
  }
}