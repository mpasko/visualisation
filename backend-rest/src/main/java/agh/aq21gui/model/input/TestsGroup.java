/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.TreeNode;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class TestsGroup {
	@XmlTransient
	public UniversalParametersContainer globalLearningParameters;
	public LinkedList<Test> runs;
	@XmlTransient
	String LABEL = "Tests";
	
	public TestsGroup(){
		globalLearningParameters = new UniversalParametersContainer();
		runs = new LinkedList<Test>();
	}

	public void addParameter(String name, String value) {
		Parameter p = new Parameter();
		p.name = name;
		p.value = value;
		globalLearningParameters.parameters.add(p);
	}

	@XmlElement(name = "globalLearningParameters")
	public void setGlobalLearningParameters(List<Parameter> parameters) {
		globalLearningParameters.parameters = parameters;
	}

	//	@XmlElement(name="globalLearningParameters")
	public List<Parameter> getGlobalLearningParameters() {
		return globalLearningParameters.parameters;
	}

	public void parseTests(TreeNode treeNode) {
		globalLearningParameters.parseParams(treeNode.childAt(0, TParser.TESTS_PARAMS));
		TreeNode runsList = treeNode.childAt(1, TParser.TESTS_LIST);
		for (TreeNode runNode : runsList.iterator(TParser.TEST)) {
			Test test = new Test();
			test.parseTest(runNode);
			this.runs.add(test);
		}
	}

	@Override
	public String toString() {
		boolean empty=true;
		if(globalLearningParameters!=null){
			empty=globalLearningParameters.parameters.isEmpty();
		}
		if(empty&&runs.isEmpty()){
			return "";
		}
		StringBuilder builder = FormatterUtil.begin(LABEL);
		if (globalLearningParameters != null) {
			builder.append(globalLearningParameters.toString());
		}
		builder.append('\n');
		FormatterUtil.appendAll(builder, runs, 1);
		return FormatterUtil.terminate(builder);
	}
	
}
