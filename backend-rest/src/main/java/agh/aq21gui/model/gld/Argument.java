/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Argument {
	private ValuesGroup values;
	
	@JsonProperty
	public String name;
	
	public void setValues(List<Value> list){
		this.values = new ValuesGroup(list);
	}
	
	public List<Value> getValues(){
		return values.getValues();
	}

	void print() {
		System.out.print("name:");
		System.out.print(name);
	}
}
