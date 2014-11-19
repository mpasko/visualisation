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
		for(TreeNode classNode : classes.iterator(TParser.CLASS_DESCRIPTION)){
			ClassDescriptor descriptor = new ClassDescriptor();
			descriptor.parseSelector(classNode);
			descriptors.add(descriptor);
		}
	}

	void traverse() {
		for(ClassDescriptor d : descriptors){
			d.traverse();
		}
	}
    
    @Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for (ClassDescriptor descriptor: this.descriptors){
    		builder.append(descriptor.toString());
        }
		return builder.toString();
	}
}
