/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.TreeNode;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class ClassDescriptor {
	@XmlElement
	public String name;
	@XmlElement
	public String comparator;
	@XmlElement
	public String value;
	@XmlElement
	public String range_begin="";
	@XmlElement
	public String range_end="";
	@XmlElement
	public List<String> set_elements=null;
	
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(name);
		builder.append(comparator);
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}

	public void parseSelector(TreeNode desc) {
		name = desc.childAt(0, TParser.ID).value();
		comparator = desc.childAt(1, TParser.EQUAL).value();
		StringBuilder builder = new StringBuilder();
		if(desc.tree().getChild(2).getType()==TParser.RANGE){
			
			TreeNode range = desc.childAt(2, TParser.RANGE);
			range_begin = range.childAt(0, TreeNode.ANY_TYPE).value();
			range_end = range.childAt(1, TreeNode.ANY_TYPE).value();
			builder.append(range_begin);
			builder.append("..");
			builder.append(range_end);
			value = builder.toString();
		}else if(desc.tree().getChild(2).getType()==TParser.VALUE_SET){
			TreeNode set = desc.childAt(2, TParser.VALUE_SET);
			int count = set.tree().getChildCount();
			set_elements = new LinkedList<String>();
			for(TreeNode itemNode : set.iterator(TParser.ID)){
				builder.append(itemNode.value());
				set_elements.add(itemNode.value());
				--count;
				if(count>0){
					builder.append(",");
				}
			}
			value = builder.toString();
		}else{
			value = desc.childAt(2, TreeNode.ANY_TYPE).value();
		}
	}
	/*
	@XmlElement
	public String name;
	@XmlElement
	public String comparator;
	@XmlElement
	public String value;
	 */
}
