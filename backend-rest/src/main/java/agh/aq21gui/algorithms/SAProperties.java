/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class SAProperties {
	@JsonProperty
	public int iterations = 400;
	@JsonProperty
	double end_temperature = 0.001;
	@JsonProperty
	double start_temperature = 1;
}
