/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.algorithms.SAProperties;
import agh.aq21gui.model.output.Output;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class GLDInput {
	private Output data;
	private final GLDProperties gld_properties;
	private final SAProperties sa_properties;
	
	public GLDInput(){
		this.gld_properties = new GLDProperties();
		this.sa_properties = new SAProperties();
	}
	
	@JsonProperty("data")
	public void setData(Output data){
		this.data = data;
	}
	
	public Output getData(){
		return data;
	}
	
	@JsonProperty("ratio_importance")
	public void setP(double p){
		this.gld_properties.p = p;
	}
	
	public double getP(){
		return gld_properties.p;
	}
	
	@JsonProperty("repartition_probability")
	public void setR(double r){
		this.gld_properties.repartition_prob = r;
	}
	
	public double getR(){
		return gld_properties.repartition_prob;
	}
	
	@JsonProperty("swap_probability")
	public void setS(double s){
		this.gld_properties.swap_prob = s;
	}
	
	public double getS(){
		return gld_properties.swap_prob;
	}
	
	@JsonProperty("swap_enabled")
	public void setSwap(boolean s){
		this.gld_properties.swap = s;
	}
	
	public boolean getSwap(){
		return gld_properties.swap;
	}
	
	@JsonIgnore
	public GLDProperties getGLDProperties(){
		return gld_properties;
	}
	
	@JsonIgnore
	public SAProperties getSAProperties(){
		return sa_properties;
	}

	@JsonProperty("iterations")
	public int getIterations() {
		return sa_properties.iterations;
	}
	
	public void setIterations(int iter){
		this.sa_properties.iterations = iter;
	}
}
