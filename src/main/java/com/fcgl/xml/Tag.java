package com.fcgl.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Tag {

  private String startTag;
  private String endTag;
  /*
   * <AttributeName, Value> The Key is the attribute name and value is the content
   */
  private Map<String, String> attributeInTag = new HashMap<>();
  public Tag(String startTag, String endTag) {
    Objects.requireNonNull(startTag);
    Objects.requireNonNull(endTag);
    this.startTag = startTag;
    this.endTag = endTag;
  }

  public Tag(String xmlTagName) {
    Objects.requireNonNull(xmlTagName);
    this.endTag = this.composeEndTag(xmlTagName);
    this.startTag = this.composeStartTag(xmlTagName);
  }

  public Tag(String tagName, Map<String,String> attrInTag) {
    Objects.requireNonNull(tagName);
    Objects.requireNonNull(attrInTag);
    this.setAttributeInTag(attrInTag);
    this.endTag = this.composeEndTag(tagName);
    this.startTag = this.composeStartTag(attributeInTag);
  }

  public String getTagName() {
    // minus one because substring is inclusive
    // started at 2 because </ is the first 2 characters
    return this.getEndTag().substring(2, this.getEndTag().length() - 1);
  }

  private String composeStartTag(String tagName) {
    return this.composeBasicHelp("<", ">", tagName);
  }

  private String composeEndTag(String tagName) {
    return this.composeBasicHelp("</", ">", tagName);
  }

  private String composeBasicHelp(String startDelim, String endDelim, String name) {
    return startDelim + name + endDelim;
  }

  private void setAttributeInTag(Map<String, String> map) {
    this.attributeInTag = map;
  }

  private String composeStartTag(Map<String, String> map) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<").append(this.getTagName());
    map.forEach((key, val) -> {
      stringBuilder.append(" ").append(key).append("=\"").append(val).append("\"");
    });
    stringBuilder.append(">");
    return stringBuilder.toString();
  }

  public String getStartTag() {
    return this.startTag;
  }

  public String getEndTag() {
    return this.endTag;
  }

  public Map<String, String> getAttributeInTag() {
    return this.attributeInTag;
  }
}
