/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.utils.TreeNode;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Parameter {
	
	@XmlElement
	public String name;
	@XmlElement
	public String value;
	@XmlElement
	public long id;
	
	private String parent="global";
	private static Long generator;
	private long dbid = 0;
	
	static{
		generator = new Long(0);
	}
	
	public void setdbid(long id){
		this.dbid = id;
	}
	
	public long getdbid(){
		return this.dbid; 
	}
	
	@XmlElement(name="parent")
	public String getparent(){
		return parent;
	}
	
	public void setparent(String parent){
		this.parent = parent;
	}
	
	public Parameter(){
		this.id = generator;
		generator++;
	}
	
	public Parameter(String parent){
		this();
		this.parent = parent;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(" = ").append(value).append('\n');
		return builder.toString();
	}

	void parseParam(TreeNode paramNode) {
		name = paramNode.childAt(0, TParser.ID).value();
		CommonTree paramTree = (CommonTree) paramNode.tree();
		if(paramTree.getChildCount()==1){
			return;
		}
		CommonTree valueNode = (CommonTree) paramTree.getChild(1);
		if(valueNode.getType()==TParser.VALUE){
			value = valueNode.getChild(0).getText();
		}
		if(valueNode.getType()==TParser.CLASSES){
			StringBuilder builder = new StringBuilder();
			TreeNode classes = new TreeNode(valueNode, TParser.CLASSES);
			for(TreeNode desc : classes.iterator(TParser.CLASS_DESCRIPTION)){
				ClassDescriptor descriptor = new ClassDescriptor();
				descriptor.parseSelector(desc);
				builder.append(descriptor.toString());
			}
			value = builder.toString();
		}
	}
}
