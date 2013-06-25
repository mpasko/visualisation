/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
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
public class Test {
	public String name = "";
	
	@XmlTransient
	public UniversalParametersContainer runSpecificParameters;

	public Test(){
		runSpecificParameters = new UniversalParametersContainer();
	}	
	
	public void addParameter(String name, String value) {
		Parameter p = new Parameter();
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
		TreeNode runParams = testNode.childAt(1, TParser.TESTS_PARAMS);
		runSpecificParameters.parseParams(runParams);
	}

	@Override
	public String toString() {
		StringBuilder builder = FormatterUtil.begin(name);
		if (runSpecificParameters != null) {
			builder.append(runSpecificParameters);
		}
		return FormatterUtil.terminate(builder);
	}
	
}
