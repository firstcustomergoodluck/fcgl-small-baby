package com.fcgl.xml;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;

/**
 * IXMLNode is a:
 * - CompoundXMLNode
 * - ElementXMLNode
 */
interface IXMLNode {

  /**
   *
   * @return the node name
   */
  @NotNull
  public String getNodeName();

  /**
   * TODO: Visitor pattern if this becomes problem
   * @return if the ElementXMLNode instead of using "instance of"
   */
  public boolean isElementNode();

  /**
   * The List is read only, any modification to the list direct will throw UnsupportedException. Use
   * the API provided to make modification.
   * @return an optional list of children
   */
  @NotNull
  public Optional<List<IXMLNode>> getChildren();

  /**
   * Add more children to the current parent.
   * Not support of for ElementXMLNode.
   * @param node the child node
   */
  public void addChildren(IXMLNode node);

  /**
   * The index is 0 indexing when remove.
   * Not supported for ElementXMLNode
   * @param atIndex
   * @return
   */
  @NotNull
  public IXMLNode remove(int atIndex);

//  TODO
//  /**
//   * Remove the first found pointer to the node given.
//   *
//   * @param node the instance (pointer) of the node to be remove
//   * @return return the removed node.
//   */
//  @NotNull
//  public Optional<IXMLNode> remove(IXMLNode node);


  /**
   *
   * @return Only ElementXMLNode will return an optional with value. Empty optional for CompoundXMLNode
   */
  public Optional<String> getElementContent();
}
