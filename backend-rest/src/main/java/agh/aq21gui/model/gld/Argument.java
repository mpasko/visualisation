/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import java.util.LinkedList;
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
	
	public Argument () {
		values = new ValuesGroup(new LinkedList<Value>());
	}
	
	@JsonProperty("values")
	public void setValues(List<Value> list){
		this.values = new ValuesGroup(list);
	}
	
	public List<Value> getValues(){
		return values.getValues();
	}

	void print() {
		System.out.print("name:");
		System.out.print(name);
		System.out.print("values:[");
		for (Value v: getValues()){
			System.out.print(v.getName());
			System.out.print(",");
		}
		System.out.print("]");
	}

	Argument cloneItself() {
		Argument arg = new Argument();
		arg.name = name;
		arg.values = values.cloneItself();
		return arg;
	}

	int width() {
		return values.width();
	}
}
