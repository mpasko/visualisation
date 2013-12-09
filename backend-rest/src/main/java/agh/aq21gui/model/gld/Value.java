/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.algorithms.conversion.ContinuousElement;
import agh.aq21gui.algorithms.conversion.RangeRecognizer;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Value {
	private String name="";
	@JsonIgnore
	public Recognizer recognizer;

	public Value(String value) {
		this.name = value;
		this.recognizer = new RangeRecognizer(value);
	}

	/**
	 * @return the name
	 */
	@JsonProperty("name")
	public String getName() {
		if (name.isEmpty()){
			return this.recognizer.generateName();
		} else {
			return name;
		}
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	boolean notWildcard() {
		return !name.equalsIgnoreCase("*");
	}
}
