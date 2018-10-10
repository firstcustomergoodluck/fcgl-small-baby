package com.fcgl.xml;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TagTest {

  Tag xmlTag;
  String tagName;
  Map<String, String> xmlInnerAttrMap;

  @BeforeEach
  void setUp() {
    tagName = "Header";
    xmlInnerAttrMap = new HashMap<>();
    xmlInnerAttrMap.put("key1", "testValue");
    xmlInnerAttrMap.put("key2", "testVal2");
    xmlInnerAttrMap.put("key3", "testVal3");
    xmlTag = new Tag(tagName, xmlInnerAttrMap);
  }

  @Test
  void getTagName() {
    assertEquals("Header", xmlTag.getTagName());
  }

  @Test
  void getStartTag() {
    assertEquals(
        "<Header key1=\"testValue\" key2=\"testVal2\" key3=\"testVal3\">",
        xmlTag.getStartTag());
  }

  @Test
  void getEndTag() {
    assertEquals("</Header>",xmlTag.getEndTag());
  }

  @Test
  void getAttributeInTag() {
    Map<String, String> exp = new HashMap<>();
    exp.put("key1", "testValue");
    exp.put("key2", "testVal2");
    exp.put("key3", "testVal3");
    assertArrayEquals(exp.values().toArray(),xmlTag.getAttributeInTag().values().toArray());
  }

  @Test
  void test() {

    String tagName2;
    Map<String, String> xmlInnerAttrMap2;
    tagName2 = "Header";
    xmlInnerAttrMap2 = new HashMap<>();
    xmlInnerAttrMap2.put("key1", "testValue");
    xmlInnerAttrMap2.put("key2", "testVal2");
    xmlInnerAttrMap2.put("key3", "testVal3");

    Tag t2 = new Tag(tagName2, xmlInnerAttrMap2);
    List<Tag> tags = Arrays.asList(xmlTag);
    assertTrue(tags.contains(xmlTag));
  }
}