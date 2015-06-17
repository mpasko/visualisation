/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.j48treegrammar;

import agh.aq21gui.utils.TreeNode;
import java.util.LinkedList;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
public class J48Tree {
    private LinkedList<Node> nodes = new LinkedList<Node>();
    private LinkedList<Branch> branches = new LinkedList<Branch>();

    public J48Tree(CommonTree grammartree) {
        for (Object t: grammartree.getChildren()){
			CommonTree childTree = (CommonTree)t;
            switch(childTree.getType()){
                case Tj48TreeParser.NODE:
                    parseNode(new TreeNode(childTree, Tj48TreeParser.NODE));
                    break;
                case Tj48TreeParser.BRANCH:
                    parseBranch(new TreeNode(childTree, Tj48TreeParser.BRANCH));
                    break;
                default:
                    throw new ParseError("Expected node or branch!");
            }
        }
        fixupBranchAttributes();
    }

    private void parseNode(TreeNode tree) {
        Node node = new Node();
        node.setName(tree.childAt(0, Tj48TreeParser.ID).value());
        node.setAttribute(tree.childAt(1, Tj48TreeParser.ID).value());
        this.getNodes().add(node);
    }

    private void parseBranch(TreeNode tree) {
        Branch branch = new Branch();
        branch.setFrom(tree.childAt(0, Tj48TreeParser.ID).value());
        branch.setTo(tree.childAt(1, Tj48TreeParser.ID).value());
        branch.setComparator(tree.childAt(2, Tj48TreeParser.EQUAL).value());
        CommonTree valueTree = tree.childAt(3, TreeNode.ANY_TYPE).tree();
        branch.setValue(valueTree.getText());
        this.getBranches().add(branch);
    }

    private void fixupBranchAttributes() {
        for (Branch branch : this.getBranches()){
            Node from = findNodeByName(branch.getFrom());
            branch.setAttribute(from.getAttribute());
            from.setIsLeaf(false);
        }
        this.getNodes().getFirst().setIsRoot(true);
    }

    public Node findNodeByName(String name) {
        for (Node node : this.getNodes()){
            if (node.getName().equalsIgnoreCase(name)){
                return node;
            }
        }
        return null;
    }
    
    public Branch findBranchByTo(String to) {
        for (Branch branch : this.getBranches()){
            if (branch.getTo().equalsIgnoreCase(to)){
                return branch;
            }
        }
        return null;
    }

    /**
     * @return the nodes
     */
    public LinkedList<Node> getNodes() {
        return nodes;
    }

    /**
     * @return the branches
     */
    public LinkedList<Branch> getBranches() {
        return branches;
    }
    
    @Override
    public String toString(){
        StringBuilder build = new StringBuilder("nodes");
        for (Node node: this.nodes){
            build.append(node.getName()).append(" is ").append(node.getAttribute());
            build.append(" root=").append(node.isRoot()).append(" leaf=").append(node.isLeaf());
            build.append("\n");
        }
        for (Branch branch: this.branches){
            build.append(branch.toString());
        }
        return build.toString();
    }
    
}
