/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.algorithms.conversion.ContinuousElement;
import agh.aq21gui.algorithms.conversion.RangeRecognizer;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Value {
	@JsonProperty("name")
	public String name;
	public RangeRecognizer recognizer;

	public Value(String value) {
		this.name = value;
		this.recognizer = new RangeRecognizer(value);
	}
}
