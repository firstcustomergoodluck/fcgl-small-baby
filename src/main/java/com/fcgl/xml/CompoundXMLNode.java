package com.fcgl.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

public class CompoundXMLNode implements IXMLNode {

  private String nodeName;
  private List<IXMLNode> childrenNode;
  private Tag nodeXmlTag;

  public CompoundXMLNode(String nodeName) {
    Objects.requireNonNull(nodeName);
    this.nodeName = nodeName;
    this.setNodeXmlTag(new Tag(nodeName));
    this.childrenNode = new LinkedList<>();
  }

  public CompoundXMLNode(String nodeName, List<IXMLNode> children) {
    Objects.requireNonNull(nodeName);
    Objects.requireNonNull(children);
    throwExceptionIfChildrenInvalid(children, "Invalid children, creating infinite loop");
    this.nodeName = nodeName;
    this.setNodeXmlTag(new Tag(nodeName));
    this.childrenNode = new LinkedList<>(children);
  }

  public CompoundXMLNode(Tag nodeXmlTag, List<IXMLNode> children) {
    Objects.requireNonNull(nodeXmlTag);
    Objects.requireNonNull(children);
    throwExceptionIfChildrenInvalid(children, "Invalid children, creating infinite loop");
    this.nodeName = nodeXmlTag.getTagName();
    this.setNodeXmlTag(nodeXmlTag);
    this.childrenNode = new LinkedList<>(children);
  }

  private void throwExceptionIfChildrenInvalid(List<IXMLNode> childrenNode, String msg) {
    Objects.requireNonNull(childrenNode);
    Objects.requireNonNull(msg);
    if (!isChilrenValid(childrenNode)) {
      throw new IllegalArgumentException(msg);
    }
  }

  private static boolean isChilrenValid(List<IXMLNode> childrenNode) {
    Objects.requireNonNull(childrenNode);
    return childrenNode.stream().allMatch(CompoundXMLNode::isChildValid);
  }

  private static boolean isChildValid(IXMLNode node) {
    Objects.requireNonNull(node);

    if (node.isElementNode()) {
      return true;
    }
    // Added children
    List<IXMLNode> children = node.getChildren().orElse(Collections.emptyList());
    Stack<IXMLNode> nodeToVisit = new Stack<>();
    children.forEach(nodeToVisit::push);

    // Direct Ancestor, ONLY add IXMLCompound with children
    Stack<IXMLNode> directAncestor = new Stack<>();
    directAncestor.push(node);
    IXMLNode curParent = node;

    List<IXMLNode> visited = new ArrayList<>();
    visited.add(node);
    while (!nodeToVisit.empty()) {
      IXMLNode curNode = nodeToVisit.pop();

      // Add children to the visit stack
      if (!curNode.isElementNode()
          && !visited.contains(curNode)
          && curNode.getChildren().isPresent()) {
        curNode.getChildren().get().forEach(nodeToVisit::push);
      }

      if (directAncestor.contains(curNode)) {
        return false;
      }
      visited.add(curNode);

      boolean isChildOfCurrentParent =
          curParent.getChildren().orElse(Collections.emptyList()).contains(curNode);
      if (!isChildOfCurrentParent) {
        directAncestor.pop();
        curParent = directAncestor.peek();
      }
      // Parents without any children, don't have an kids to be ancestor
      // Only add parents when they have kids
      if (!curNode.isElementNode()
          && curNode.getChildren().orElse(Collections.emptyList()).size() > 0) {
        directAncestor.add(curNode);
        curParent = curNode;
      }
    }
    return true;
  }

  private void setNodeXmlTag(Tag tag) {
    this.nodeXmlTag = tag;
  }

  private Tag getNodeXmlTag() {
    return this.nodeXmlTag;
  }
  @Override
  public String getNodeName() {
    return this.nodeName;
  }

  @Override
  public boolean isElementNode() {
    return false;
  }

  private List<IXMLNode> getChildrenNode() {
    return this.childrenNode;
  }

  private void setChildrenNode(List<IXMLNode> childrenNode) {
    this.childrenNode = childrenNode;
  }

  @Override
  public Optional<List<IXMLNode>> getChildren() {
    List<IXMLNode> unModifiableList = Collections.unmodifiableList(this.getChildrenNode());
    return Optional.of(unModifiableList);
  }

  @Override
  public void addChildren(IXMLNode node) {
    Objects.requireNonNull(node);

    List<IXMLNode> originalChilren = this.getChildrenNode();
    // the same "pointer" object, crosses are ok
    List<IXMLNode> temp = new LinkedList<>(this.getChildrenNode());
    temp.add(node);
    this.setChildrenNode(temp);
    if (node == this || !isChildValid(this)) {
      //reset to the original list
      this.setChildrenNode(originalChilren);
      throw new IllegalArgumentException("Ancestor can't both be child/grandchild of itself");
    }
    this.setChildrenNode(originalChilren);
    this.getChildrenNode().add(node);
  }

  @Override
  public IXMLNode remove(int atIndex) {
    int childrenLen = this.getChildrenNode().size();
    // Invalid if negative or bigger than list of children
    if (atIndex < 0 || atIndex >= childrenLen) {
      throw new IllegalArgumentException(String.format(
          "The index %d is invalid, when tried to remove actual list size of %d",
          atIndex, childrenLen));
    }
    return this.getChildrenNode().remove(atIndex);
  }

  @Override
  public Optional<String> getElementContent() {
    return Optional.empty();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("\n");
    Tag tag = this.getNodeXmlTag();
    String nodeBody = this.listOfNodeString().replace("\n","\n\t");
    return builder
        .append(tag.getStartTag())
        .append(nodeBody)
        .append("\n")
        .append(tag.getEndTag())
        .toString();
  }

  private String listOfNodeString() {
    StringBuilder stringBuilder = new StringBuilder();
    this.getChildrenNode().stream()
        .map(Object::toString).forEach(str -> {
          stringBuilder.append("\n").append(str);
    });
    return stringBuilder.toString();
  }

}
