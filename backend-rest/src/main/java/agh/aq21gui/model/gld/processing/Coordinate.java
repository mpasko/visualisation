/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld.processing;

import agh.aq21gui.model.gld.Value;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
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

	public void put(String name, Value current) {
		values.put(name, current);
	}

	public Coordinate cloneItself() {
		Coordinate copy = new Coordinate();
		copy.values = new HashMap<String, Value>();
		copy.putAll(this.values);
		return copy;
	}

	public Coordinate merge(Coordinate col) {
		Coordinate coord = this.cloneItself();
		coord.putAll(col.values);
		return coord;
	}
	
	private void putAll(Map<String,Value> values) {
		for (String key: values.keySet()){
			final Value value = values.get(key);
			if (value.notWildcard()){
				put(key, value);
			}
		}
	}

	@JsonIgnore
	public Value get(String name) {
		return values.get(name);
	}

	public int size() {
		return this.values.size();
	}
	
	@Override
	public boolean equals(Object other){
		if (other == null){
			return false;
		}
		if (other instanceof Coordinate){
			Coordinate next = (Coordinate) other;
			if (next.size()!=this.size()){
				return false;
			}
			for (String key : this.values.keySet()){
				Value value1 = this.values.get(key);
				Value value2 = next.values.get(key);
				if (!value1.equals(value2)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = 7;
		for (Value v : values.values()) {
			hash = 41 * hash + (v != null ? v.hashCode() : 0);
		}
		return hash;
	}
}
