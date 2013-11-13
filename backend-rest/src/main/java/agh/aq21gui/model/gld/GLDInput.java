/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.model.output.Output;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class GLDInput {
	private Output data;
	private Double p;
	
	public GLDInput(){}
	
	@JsonProperty("data")
	public void setData(Output data){
		this.data = data;
	}
	
	public Output getData(){
		return data;
	}
	
	@JsonProperty("ratio_importance")
	public void setP(Double p){
		this.p = p;
	}
	
	public Double getP(){
		return p;
	}
}
