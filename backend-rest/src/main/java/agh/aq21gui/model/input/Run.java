/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Run {
	
	@XmlTransient
	public UniversalParametersContainer runSpecificParameters;
	public String name="Run";
	
	public Run(){
		runSpecificParameters = new UniversalParametersContainer();
	}
	
	@XmlElement(name="runSpecificParameters")
	public void setRunSpecificParameters(List<Parameter> parameters){
		runSpecificParameters.parameters=parameters;
	}
	
//	@XmlElement(name="runSpecificParameters")
	public List<Parameter> getRunSpecificParameters(){
		return runSpecificParameters.parameters;
	}
	
	public void addParameter(String name, String value){
		Parameter p = new Parameter();
		p.name = name;
		p.value = value;
		runSpecificParameters.parameters.add(p);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(name).append("\n{\n");
		if(runSpecificParameters != null){
			builder.append(runSpecificParameters);
		}
		builder.append("}\n");
		return builder.toString();
	}
}
