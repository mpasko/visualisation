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
import java.util.logging.Logger;
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
		if (!val.isEmpty()){
			this.value = val;
			this.range_begin="";
			this.set_elements=new LinkedList<String>();
		}
	}
	
	public String getValue(){
		if (value.isEmpty() && (set_elements.size()>=1) ) {
			setToString();
		}
		if (value.isEmpty() && (!range_begin.isEmpty()) ) {
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

	public void parseSelector(TreeNode desc) {
		name = desc.childAt(0, TParser.ID).value();
		comparator = desc.childAt(1, TParser.EQUAL).value();
		if(desc.tree().getChild(2).getType()==TParser.RANGE){
			TreeNode range = desc.childAt(2, TParser.RANGE);
			range_begin = range.childAt(0, TreeNode.ANY_TYPE).value();
			range_end = range.childAt(1, TreeNode.ANY_TYPE).value();
			rangeToString();
		}else if(desc.tree().getChild(2).getType()==TParser.VALUE_SET){
			Logger.getLogger("Parser").info("Parsing value set");
			TreeNode set = desc.childAt(2, TParser.VALUE_SET);
			set_elements = new LinkedList<String>();
			for(TreeNode itemNode : set.iterator(TreeNode.ANY_TYPE)){
				set_elements.add(itemNode.value());
			}
			setToString();
		}else{
			value = desc.childAt(2, TreeNode.ANY_TYPE).value();
		}
	}

	void traverse() {
		if(name.isEmpty());
	}

	public boolean contains(ClassDescriptor other) {
		if (equals(other)){
			return true;
		}
		if (this.comparator.equals("=")){
			return false;
		}
		//TODO
		return false;
	}
	
	public boolean equals(ClassDescriptor other){
		if (other==null){
			return false;
		}
		if (!this.name.equalsIgnoreCase(other.name)){
			return false;
		}
		if (!this.comparator.equalsIgnoreCase(other.comparator)){
			return false;
		}
		if (!this.getValue().equalsIgnoreCase(other.getValue())){
			return false;
		}
		return true;
	}
	
	private void setToString(){
		StringBuilder build = new StringBuilder();
		for (String element:set_elements) {
			if (build.length()>0) {
				build.append(",");
			}
			build.append(element);
		}
		value = build.toString();
	}

	private void rangeToString() {
		StringBuilder builder = new StringBuilder();
		builder.append(range_begin);
		builder.append("..");
		builder.append(range_end);
		value = builder.toString();
	}
}
