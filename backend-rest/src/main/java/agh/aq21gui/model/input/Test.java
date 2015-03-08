/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.exceptions.IncorrectInputException;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.TreeNode;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Test implements IAQ21Serializable {
	public String name = "";
	protected int ID = TParser.TESTS_PARAMS;
	
	@XmlTransient
	public UniversalParametersContainer runSpecificParameters;

	public Test(){
	//	this.name = name;
		runSpecificParameters = new UniversalParametersContainer(name);
	}	
	
	@XmlElement(name = "name")
	public void setName(String nm){
		this.name = nm;
	}
	
	public String getName(){
		return name;
	}
	
	public void addParameter(String name, String value) {
		Parameter p = new Parameter(this.name);
		p.name = name;
		p.value = value;
		runSpecificParameters.parameters.add(p);
	}

	@XmlElement(name = "runSpecificParameters")
	public void setRunSpecificParameters(List<Parameter> parameters) {
		runSpecificParameters.parameters = parameters;
	}

	//	@XmlElement(name="runSpecificParameters")
	public List<Parameter> getRunSpecificParameters() {
		return runSpecificParameters.parameters;
	}

	void parseTest(TreeNode testNode) {
		name = testNode.childAt(0, TParser.ID).value();
		runSpecificParameters.sName(name);
		TreeNode runParams = testNode.childAt(1, ID);
		runSpecificParameters.parseParams(runParams);
	}

	@XmlTransient
	boolean isNotEmpty(){
		return runSpecificParameters != null && !runSpecificParameters.parameters.isEmpty();
	}
	
	@Override
	public String toString() {
		if (isNotEmpty()) {
			StringBuilder builder = FormatterUtil.begin(name);
			builder.append(runSpecificParameters.toString());
			return FormatterUtil.terminate(builder);
		}else{
			return  "";
		}
	}

	void traverse() {
		if(name.isEmpty());
	}

    public ClassDescriptor grepClassDescriptor() {
        for (Parameter param : this.runSpecificParameters.parameters) {
            if (param.name.equalsIgnoreCase("Consequent")) {
                List<ClassDescriptor> descriptors = param.getDescriptors();
                if (descriptors.isEmpty()) {
                    throw new IncorrectInputException("Consequent should specify at least one class!");
                }
                return descriptors.get(0);
            }
        }
        return null;
    }
    
    public String grepClassName() {
        ClassDescriptor desc = grepClassDescriptor();
        if (desc != null) {
            return desc.getName();
        }
        return "";
    }

    public String grepCondition() {
        return grepClassDescriptor().toString();
    }
	
}
