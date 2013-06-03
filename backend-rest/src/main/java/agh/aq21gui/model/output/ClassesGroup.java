/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
public class ClassesGroup {
	List<ClassDescriptor> descriptors = new LinkedList<ClassDescriptor>();
	
	public ClassesGroup(){}
	
	public ClassesGroup(CommonTree tree){
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
	}
}
