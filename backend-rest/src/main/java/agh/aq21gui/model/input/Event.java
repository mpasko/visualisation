/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.utils.TreeNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.bind.annotation.XmlElement;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
//@XmlRootElement
public class Event implements IAQ21Serializable {
	private int dbid=0;
	public int id=0;
	private List<String> values;
	
	public Event(){
		values = new LinkedList<String>();
	}
	
	public Event(List<String> values) {
		this.values = values;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	@XmlElement(name="values")
	public List<String> getValues(){
		return values;
	}
	
	public void setValues(List<String> values){
		this.values = new LinkedList<String>(values);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<values.size(); ++i){
			String value = values.get(i);
			builder.append(value);
			if(i<values.size()-1){
				builder.append(", ");
			}else{
				builder.append('\n');
			}
		}
		return builder.toString();
	}

	void parseRow(TreeNode eventNode) {
		for(Object obj : eventNode.tree().getChildren()){
			CommonTree cell = (CommonTree) obj;
			values.add(cell.getText());
		}
	}

	void traverse() {
		this.values.contains("ala 123");
	}

    public void replaceValueAt(int index, String newValue) {
        values.set(index, newValue);
    }
}
