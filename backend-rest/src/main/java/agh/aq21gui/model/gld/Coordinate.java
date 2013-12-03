/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Coordinate {
	
	private Map<String,Value> values = new HashMap<String,Value>();
	
	@Override
	public String toString(){
		StringBuilder build = new StringBuilder("{");
		for (Value val : getValues()) {
			build.append(val.getName());
			build.append(", ");
		}
		build.append("}");
		return build.toString();
	}

	/**
	 * @return the values
	 */
	@JsonProperty("values")
	public List<Value> getValues() {
		return new LinkedList<Value>(values.values());
	}

	void put(String name, Value current) {
		values.put(name, current);
	}

	Coordinate cloneItself() {
		Coordinate copy = new Coordinate();
		copy.values = new HashMap<String, Value>();
		for (String key: this.values.keySet()){
			copy.put(key, this.values.get(key));
		}
		return copy;
	}

	Coordinate merge(Coordinate col) {
		Coordinate coord = this.cloneItself();
		for (String key : col.values.keySet()) {
			coord.put(key, col.get(key));
		}
		return coord;
	}

	Value get(String name) {
		return values.get(name);
	}
}
