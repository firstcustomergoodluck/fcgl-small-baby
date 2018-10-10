package com.fcgl.xml;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElementXMLNodeTest {

  IXMLNode elementNode;
  Tag tag;
  Map<String, String> attr;
  @BeforeEach
  void setUp() {
    attr = new HashMap<>();
    attr.put("Currency", "USD");
    tag = new Tag("Element", attr);
    elementNode = new ElementXMLNode(tag,"12.50");
  }

  @Test
  void getNodeName() {
    assertEquals("Element", elementNode.getNodeName());
  }

  @Test
  void isElementNode() {
    assertTrue(elementNode.isElementNode());
  }

  @Test
  void getChildren() {
    assertEquals(Optional.empty(), elementNode.getChildren());
  }

  @Test
  void addChildren() {
    UnsupportedOperationException unsupportedEx = assertThrows(
        UnsupportedOperationException.class,
        () -> elementNode.addChildren(elementNode));
    assertEquals("ElementXMLNode cannot have children",unsupportedEx.getMessage());
  }

  @Test
  void remove() {
    UnsupportedOperationException unsupportedEx = assertThrows(
        UnsupportedOperationException.class,
        () -> elementNode.remove(0));
    assertEquals(
        "ElementXMLNode can't have children, therefore unsupported",
        unsupportedEx.getMessage());
  }

  @Test
  void getElementContent() {
    assertEquals(Optional.of("12.50"), elementNode.getElementContent());
  }

  @Test
  void toStringTest() {
    assertEquals("\n<Element Currency=\"USD\">12.50</Element>",elementNode.toString());
  }

}