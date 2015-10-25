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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Test implements IAQ21Serializable {
	private String name;
	protected int ID = TParser.TESTS_PARAMS;
    private static int generator=0;
	
	@XmlTransient
	public UniversalParametersContainer runSpecificParameters;

	public Test(){
		this.name = "test";
        if (generator++>0) {
            this.name+=generator;
        }
		runSpecificParameters = new UniversalParametersContainer(name);
	}	

    public Test(Test run) {
        runSpecificParameters = new UniversalParametersContainer(run.name);
        for (Parameter param : run.getRunSpecificParameters()) {
            addParameter(param);
        }
    }
	
	@XmlElement(name = "name")
	public void setName(String nm){
		this.name = nm;
	}
	
	public String getName(){
		return name;
	}
	
    
	public void addParameter(String name, String value) {
        addParameter(name, value, new LinkedList<ClassDescriptor>());
    }
    
	public void addParameter(String name, String value, List<ClassDescriptor> descriptors) {
		Parameter p = new Parameter(this.getName());
		p.name = name.toLowerCase(Locale.US);
        p.setDescriptors(descriptors);
        p.setValue(value.toLowerCase(Locale.US));
		runSpecificParameters.parameters.add(p);
	}
    
    public final void addParameter(Parameter param) {
		addParameter(param.getName(), param.getValue(), param.getDescriptors());
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
        String text = "";
		if (isNotEmpty()) {
			StringBuilder builder = FormatterUtil.begin(name);
			builder.append(runSpecificParameters.toString());
			text = FormatterUtil.terminate(builder);
		}
        return text;
	}

	void traverse() {
		if(name.isEmpty()){
            name = "";
        }
	}

    public ClassDescriptor grepClassDescriptor() {
        ClassDescriptor cd = null;
        Parameter param = findConsequentParam();
        if (param != null) {
            List<ClassDescriptor> descriptors = param.getDescriptors();
            if (descriptors.isEmpty()) {
                throw new IncorrectInputException("Consequent should specify at least one class!");
            }
            cd = descriptors.get(0);
        }
        return cd;
    }
    
    public Parameter findConsequentParam() {
        return findParam("Consequent");
    }
    
    public Parameter findParam(String name) {
        Parameter found = null;
        for (Parameter param : this.runSpecificParameters.parameters) {
            if (param.name.equalsIgnoreCase(name)) {
                found = param;
            }
        }
        return found;
    }
    
    public String grepClassName() {
        String cname = "";
        ClassDescriptor desc = grepClassDescriptor();
        if (desc != null) {
            cname = desc.getName();
        }
        return cname;
    }

    public String grepCondition() {
        return grepClassDescriptor().toString();
    }

    public void enforceClass(String newClass, String threshold) {
        ClassDescriptor cd = new ClassDescriptor();
        cd.setName(newClass);
        if (threshold==null) {
            cd.setComparator("=");
            cd.setValue("*");
        } else {
            cd.setComparator("<=");
            cd.setValue(threshold);
        }
        enforceClass(cd);
    }

    public void enforceClass(ClassDescriptor classDescriptor) {
        Parameter param = findOrCreateParam("Consequent");
        List<ClassDescriptor> descs = new LinkedList<ClassDescriptor>();
        descs.add(classDescriptor);
        param.setDescriptors(descs);
    }

    public void switchParam(String name, String value) {
        Parameter param = findOrCreateParam(name);
        param.setValue(value);
    }

    public Parameter findOrCreateParam(String name) {
        Parameter param = findParam(name);
        if (param==null) {
            String parent = runSpecificParameters.getparent();
            param = new Parameter(parent);
            param.setName(name);
            this.runSpecificParameters.parameters.add(param);
        }
        return param;
    }
	
}
