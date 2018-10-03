package com.fcgl.xml;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.emory.mathcs.backport.java.util.Collections;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompoundXMLNodeTest {

  IXMLNode nodeA;
  IXMLNode nodeB;
  IXMLNode nodeC;
  IXMLNode elementD;
  IXMLNode nodeBC;
  IXMLNode nodeAB;

  @BeforeEach
  void setUp() {
    nodeA = new CompoundXMLNode("A");
    nodeB = new CompoundXMLNode("B");
    nodeC = new CompoundXMLNode("C");
    elementD = new ElementXMLNode("D", "body");
    nodeBC = new CompoundXMLNode("BC", Arrays.asList(nodeB,nodeC));
    nodeAB = new CompoundXMLNode(new Tag("AB"), Arrays.asList(nodeA,nodeB));
  }

  @Test
  void getNodeName() {
    assertEquals("A", nodeA.getNodeName());
    assertEquals("BC", nodeBC.getNodeName());
  }

  @Test
  void isElementNode() {
    assertFalse(nodeBC.isElementNode());
    assertFalse(nodeB.isElementNode());
    assertFalse(nodeA.isElementNode());
  }

  @Test
  void getChildren1() {
    assertTrue(nodeA.getChildren().isPresent());
    assertFalse(nodeA.getChildren().get().contains(nodeB));
    assertEquals(0,nodeA.getChildren().get().size());
  }

  @Test
  void getChildren2() {
    assertTrue(nodeBC.getChildren().isPresent());
    assertTrue(nodeBC.getChildren().get().contains(nodeB));
    assertTrue(nodeBC.getChildren().get().contains(nodeC));
    assertEquals(2,nodeBC.getChildren().get().size());
  }

  @Test
  void getChildren3() {
    nodeB.addChildren(nodeA);
    nodeB.addChildren(elementD);

    assertTrue(nodeBC.getChildren().isPresent());
    assertTrue(nodeBC.getChildren().get().contains(nodeB));
    assertTrue(nodeBC.getChildren().get().contains(nodeC));
    assertEquals(2,nodeBC.getChildren().get().size());

    assertFalse(nodeBC.getChildren().get().contains(nodeA));
    assertFalse(nodeBC.getChildren().get().contains(elementD));
  }

  @Test
  void getChildrenErr1() {
    nodeB.addChildren(nodeA);
    nodeB.addChildren(elementD);
    // Ensure that the list is not modifiable
    assertThrows(
        UnsupportedOperationException.class,
        () -> nodeB.getChildren().ifPresent(ixmlNodes -> ixmlNodes.add(nodeA)));
  }

  @Test
  void addChildren() {
    nodeA.addChildren(elementD);
    nodeA.addChildren(nodeBC);
    // Pointer contains as children
    assertTrue(nodeA.getChildren().orElse(Collections.emptyList()).contains(elementD));
    assertTrue(nodeA.getChildren().orElse(Collections.emptyList()).contains(nodeBC));
    // Ensure it doesn't flatten the list
    assertFalse(nodeA.getChildren().orElse(Collections.emptyList()).contains(nodeB));
    assertFalse(nodeA.getChildren().orElse(Collections.emptyList()).contains(nodeC));
  }

  @Test
  void addChildrenErr1() {
    /* A
     * |
     * A
     */

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      nodeA.addChildren(nodeA);
    });

    assertEquals("Ancestor can't both be child/grandchild of itself", exception.getMessage());
  }

  @Test
  void addChildrenErr2() {
    /* B
     * |
     * A
     * |
     * B
     */
    nodeA.addChildren(nodeB);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      nodeB.addChildren(nodeA);
    });

    assertEquals("Ancestor can't both be child/grandchild of itself", exception.getMessage());
  }

  @Test
  void addChildrenErr3() {
    nodeA.addChildren(nodeB);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      nodeB.addChildren(nodeA);
    });

    assertEquals("Ancestor can't both be child/grandchild of itself", exception.getMessage());
  }

  @Test
  void addChildrenErr4() {
    nodeA.addChildren(nodeB);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      nodeB.addChildren(nodeA);
    });

    assertEquals("Ancestor can't both be child/grandchild of itself", exception.getMessage());
  }

  @Test
  void remove() {
    assertEquals(nodeB, nodeBC.remove(0));
    nodeBC.addChildren(nodeB);
    nodeBC.addChildren(elementD);
    nodeBC.addChildren(nodeA);
    assertEquals(nodeA, nodeBC.remove(3));
    assertEquals(nodeC, nodeBC.remove(0));
    assertEquals(elementD, nodeBC.remove(1));
  }

  @Test
  void removeErr1() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      nodeBC.remove(-1);
    });
    assertEquals(
        "The index -1 is invalid, when tried to remove actual list size of 2",
        exception.getMessage());
  }

  @Test
  void removeErr2() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      nodeBC.remove(2);
    });

    assertEquals(
        "The index 2 is invalid, when tried to remove actual list size of 2",
        exception.getMessage());
  }

  @Test
  void getElementContent() {
    assertEquals(Optional.empty(), nodeA.getElementContent());
    assertEquals(Optional.empty(), nodeB.getElementContent());
    assertEquals(Optional.empty(), nodeBC.getElementContent());
  }

  @Test
  void toStringTest1() {
    assertEquals("\n<A>\n</A>", nodeA.toString());
  }

  @Test
  void toStringTest2() {
    assertEquals("\n<BC>\n\t\n\t<B>\n\t</B>\n\t\n\t<C>\n\t</C>\n</BC>", nodeBC.toString());
  }

  @Test
  void toStringTest3() {
    nodeB.addChildren(elementD);
    assertEquals("\n<BC>\n\t\n\t<B>\n\t\t\n\t\t<D>body</D>\n\t</B>\n\t\n\t<C>\n\t</C>\n</BC>",
        nodeBC.toString());
  }

}