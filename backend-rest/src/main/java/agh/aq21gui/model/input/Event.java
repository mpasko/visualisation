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
	
	private static Long enumerator;
	
	static{
		enumerator = new Long(0);
	}
	
	public Event(){
		values = new LinkedList<String>();
	}

	Event(Map<String, Object> event, AttributesGroup attributes) {
		values = new LinkedList<String>();
		for(Attribute attr : attributes.attributes){
			String name = attr.getname();
			values.add(event.get(name).toString());
		}
	}
	
	public int getdbid(){
		return dbid;
	}
	
	public void setdbid(int id){
		this.dbid = id;
	}
	
	@XmlElement(name="values")
	public List<String> getvalues(){
		return values;
	}
	
	public void setvalues(List<String> values){
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

	TreeMap<String, Object> formatEvent(AttributesGroup attributesGroup) {
		TreeMap<String, Object> workingMap = new TreeMap<String, Object>();
		workingMap.put("id", enumerator);
		enumerator++;
		Iterator<Attribute> attrIterator = attributesGroup.attributes.iterator();
		for (String value:values){
			Attribute attr = attrIterator.next();
			workingMap.put(attr.getname(), value);
		}
		return workingMap;
	}

	void traverse() {
		this.values.contains("ala 123");
	}
}
