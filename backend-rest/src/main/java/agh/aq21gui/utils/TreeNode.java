/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;


import java.util.LinkedList;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
public class TreeNode {
	private final CommonTree tree;
	public static final int ANY_TYPE=-111; 

/*
	private TreeNode(CommonTree tree){
		
	}	
*/
	
	public TreeNode(CommonTree tree, int type){
		this.tree = tree;
		if(tree==null){
			report("Tree of expected type %d must not be null in constructor!",type);
		}
		int found=tree.getType();
		if(found!=type && type != ANY_TYPE){
			report("Tree Constructor: %d expected but %d found",type,found);
		}
	}
	
	public TreeNode childOf(int type){
		for (Object t: tree.getChildren()){
			CommonTree childTree = (CommonTree)t;
			if(childTree.getType()==type){
				return new TreeNode(childTree,type);
			}
		}
		report("Child node of type %d not found",type);
		return null;
	}
	
	public TreeNode childAt(int position, int type){
		CommonTree childTree = (CommonTree)tree.getChild(position);
		int found = childTree.getType();
		if(found==type || type==ANY_TYPE){
			return new TreeNode(childTree,type);
		}else{
			report("At position %d, expected type %d but %d found.",position,type,found);
			return null;
		}
	}
	
	public String value(){
		return tree.getText();
	}
	
	public CommonTree tree(){
		return tree;
	}
	
	private static void report(String error, Object...args){
		String message = String.format(error,args);
		/* *x/
		Logger.getLogger("Interpreter").info(message);
		/* */
		throw new RuntimeException(message);
		/* */
	}

	public Iterable<TreeNode> iterator(int type) {
		LinkedList<TreeNode> list = new LinkedList<TreeNode>();
		if (tree.getChildCount()>0){
			for (Object t: tree.getChildren()){
				CommonTree childTree = (CommonTree)t;
				if(childTree.getType()==type || type==ANY_TYPE){
					list.add(new TreeNode(childTree,type));
				}
			}
		}
		return list;
	}

    public Double doubleValue() {
        String value = this.value();
        return Double.parseDouble(value);
    }
}
