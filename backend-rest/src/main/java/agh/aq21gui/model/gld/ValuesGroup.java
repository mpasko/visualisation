/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

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
	
}
