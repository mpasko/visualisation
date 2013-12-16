/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class ValuesGroup {
	private List<Value> values;

	ValuesGroup(List<Value> list) {
		this.values = list;
	}

	List<Value> getValues() {
		return values;
	}

	ValuesGroup cloneItself() {
		List<Value> vals = new LinkedList<Value>();
		for (Value v : values) {
			vals.add(v);
		}
		return new ValuesGroup(vals);
	}

	int width() {
		return values.size();
	}
	
}
