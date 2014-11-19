/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.j48treegrammar;

/**
 *
 * @author marcin
 */
public class Node {
    private String name;
    private String attribute;
    private boolean isLeaf=true;
    private boolean isRoot=false;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the isLeaf
     */
    public boolean isLeaf() {
        return isLeaf;
    }

    /**
     * @param isLeaf the isLeaf to set
     */
    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    /**
     * @return the isRoot
     */
    public boolean isRoot() {
        return isRoot;
    }

    /**
     * @param isRoot the isRoot to set
     */
    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }
}
