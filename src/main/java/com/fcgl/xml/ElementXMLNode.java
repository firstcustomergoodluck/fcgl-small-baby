package com.fcgl.xml;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ElementXMLNode implements IXMLNode {

  private String nodeName;
  private String elementContent;

  private Tag xmlTag;
  public ElementXMLNode(String nodeName, String elementContent) {
    Objects.requireNonNull(nodeName);
    Objects.requireNonNull(elementContent);
    this.nodeName = nodeName;
    this.setXmlTag(new Tag(nodeName));
    this.elementContent = elementContent;
  }

  public ElementXMLNode(Tag xmlTag, String elementContent) {
    Objects.requireNonNull(xmlTag);
    Objects.requireNonNull(elementContent);
    this.nodeName = xmlTag.getTagName();
    this.setXmlTag(xmlTag);
    this.elementContent = elementContent;
  }

  private void setXmlTag(Tag tag) {
    this.xmlTag = tag;
  }

  private Tag getXmlTag() {
    return this.xmlTag;
  }

  @Override
  public String getNodeName() {
    return this.nodeName;
  }

  @Override
  public boolean isElementNode() {
    return true;
  }

  @Override
  public Optional<List<IXMLNode>> getChildren() {
    return Optional.empty();
  }

  @Override
  public void addChildren(IXMLNode node) {
    throw new UnsupportedOperationException("ElementXMLNode cannot have children");
  }

  @Override
  public IXMLNode remove(int atIndex) {
    throw new UnsupportedOperationException("ElementXMLNode can't have children, therefore unsupported");
  }

  @Override
  public Optional<String> getElementContent() {
    return Optional.of(this.elementContent);
  }

  @Override
  public String toString() {
    Tag tag = this.getXmlTag();
    return "\n" + tag.getStartTag() + this.elementContent + tag.getEndTag();
  }
}
