/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import java.util.LinkedList;
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
//@XmlSeeAlso({UniversalParametersContainer.class})
public class RunsGroup {
	public LinkedList<Run> runs;
	
	@XmlTransient
	public UniversalParametersContainer globalLearningParameters;
	
	public RunsGroup(){
		globalLearningParameters = new UniversalParametersContainer();
		runs = new LinkedList<Run>();
	}
	
	public void addParameter(String name, String value){
		Parameter p = new Parameter();
		p.name = name;
		p.value = value;
		globalLearningParameters.parameters.add(p);
	}
	
	@XmlElement(name="globalLearningParameters")
	public void setGlobalLearningParameters(List<Parameter> parameters){
		globalLearningParameters.parameters=parameters;
	}
	
//	@XmlElement(name="globalLearningParameters")
	public List<Parameter> getGlobalLearningParameters(){
		return globalLearningParameters.parameters;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("Runs\n{\n");
		if(globalLearningParameters!=null){
			builder.append(globalLearningParameters.toString()).append('\n');
		}
		for(Run run : runs){
			builder.append(run.toString());
		}
		builder.append("}\n");
		return builder.toString();
	}
}
