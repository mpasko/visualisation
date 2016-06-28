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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class TestsGroup implements IAQ21Serializable {
	//@XmlTransient
	protected transient UniversalParametersContainer globalLearningParameters;
	public List<Test> runs;
	@XmlTransient
	String LABEL = "Tests";
	
	@XmlElement(name="runsNames")
	public List<String> getRunsNames(){
		LinkedList<String> working = new LinkedList<String>();
		for(Test run : runs){
			working.add(run.getName());
		}
		return working;
	}
	
	public void setRunsNames(List<String> names){
		/* Do nothing */
	}
	
	@XmlElement(name = "runs")
	public void setRuns(List<Test> runs){
		this.runs = runs;
	}
	
	public List<Test> getRuns(){
		return runs;
	}
	
	public TestsGroup(){
		globalLearningParameters = new UniversalParametersContainer("global");
		runs = new LinkedList<Test>();
	}

	public void addParameter(String name, String value) {
		Parameter p = new Parameter("global");
		p.name = name;
		p.value = value;
		globalLearningParameters.parameters.add(p);
	}
    
    public Parameter findParam(String name) {
        return globalLearningParameters.findParam(name);
    }

	@XmlElement(name = "globalLearningParameters")
	public void setGlobalLearningParameters(List<Parameter> parameters) {
		//Logger.getLogger("Serialization").log(Level.INFO, "class name:{0}", parameters.getClass().getCanonicalName());
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
        String out;
		boolean emptyGlobParams=true;
		if(globalLearningParameters!=null){
			emptyGlobParams=globalLearningParameters.parameters.isEmpty();
		}
		boolean emptyRuns=true;
		if(!runs.isEmpty()){
			for(Test test: runs){
				if (test.isNotEmpty()){
					emptyRuns = false;
				}
			}
		}
		if(emptyGlobParams&&emptyRuns){
			out = "";
		} else {
            StringBuilder builder = FormatterUtil.begin(LABEL);
            if (!emptyGlobParams) {
                builder.append(globalLearningParameters.toString());
            }
            builder.append('\n');
            FormatterUtil.appendAll(builder, runs, 1);
            out = FormatterUtil.terminate(builder);
        }
        return out;
	}

	void traverse() {
		for(Test t : runs){
			t.traverse();
		}
		globalLearningParameters.traverse();
	}
	
}
