/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.model.gld.Value;
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

	public String name;
	public String comparator;
	private String value="";
	public String range_begin="";
	public String range_end="";
	public List<String> set_elements=new LinkedList<String>();
		
	@XmlElement(name="name")
	public void setName(String nm){
		this.name = nm;
	}
	
	public String getName(){
		return this.name; 
	}
	
	@XmlElement(name="comparator")
	public void setComparator(String comp){
		this.comparator = comp;
	}
	
	public String getComparator(){
		return this.comparator; 
	}
	
	@XmlElement(name="value")
	public void setValue(String val){
		this.value = val;
	}
	
	public String getValue(){
		if (value.isEmpty() && (set_elements.size()>=1) ) {
			rangeToString();
		}
		return this.value; 
	}
	
	@XmlElement(name="range_begin")
	public void setRange_begin(String r_b){
		this.range_begin = r_b;
	}
	
	public String getRange_begin(){
		return this.range_begin; 
	}
	
	@XmlElement(name="range_end")
	public void setRange_end(String r_e){
		this.range_end = r_e;
	}
	
	public String getRange_end(){
		return this.range_end; 
	}
	
	@XmlElement(name="set_elements")
	public List<String> getSet_elements(){
		return set_elements;
	}
	
	public void setSet_elements(List<String> values){
		this.set_elements = new LinkedList<String>(values);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(name);
		builder.append(comparator);
		builder.append(getValue());
		builder.append("]");
		return builder.toString();
	}
	
	private void rangeToString(){
		StringBuilder build = new StringBuilder();
		for (String element:set_elements) {
			if (build.length()>0) {
				build.append(",");
			}
			build.append(element);
		}
		value = build.toString();
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

	void traverse() {
		if(name.isEmpty());
	}
}
