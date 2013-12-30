/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class GLDProperties {
	@JsonProperty
	public double p = 1.0;
	@JsonProperty
	public boolean swap = false;
	@JsonProperty
	public double repartition_prob = 0.05;
	@JsonProperty
	public double swap_prob = 0.005;
}
