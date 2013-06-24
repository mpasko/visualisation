/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.TreeNode;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class ClassesGroup {
	List<ClassDescriptor> descriptors = new LinkedList<ClassDescriptor>();
	
	public ClassesGroup(){}
	
	public ClassesGroup(TreeNode classes){
		/* now */
		for(TreeNode classNode : classes.iterator(TParser.CLASS_DESCRIPTION)){
			ClassDescriptor descriptor = new ClassDescriptor();
			descriptor.parseSelector(classNode);
			descriptors.add(descriptor);
		}
		/* before *x/
		for(Object t : tree.getChildren()){
			CommonTree childTree = (CommonTree)t;
			if(childTree.getType()==TParser.CLASS_DESCRIPTION){
				ClassDescriptor descriptor = new ClassDescriptor();
				descriptor.name = childTree.getChild(0).getText();
				descriptor.comparator = childTree.getChild(1).getText();
				descriptor.value = childTree.getChild(2).getText();
				descriptors.add(descriptor);
			}else{
				Logger.getLogger("Interpreter").severe("Error! Expected CLASS_DESCRIPTION, received:");
				Logger.getLogger("Interpreter").severe(tree.toString());
			}
		}
		/* */
	}
}
