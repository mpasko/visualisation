/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.TreeNode;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class RunsGroup extends TestsGroup{
	
	public RunsGroup(){
		LABEL = "Runs";
	}
	
	public void parseRuns(TreeNode treeNode) {
		globalLearningParameters.parseParams(treeNode.childAt(0, TParser.RUNS_PARAMS));
		TreeNode runsList = treeNode.childAt(1, TParser.RUNS_LIST);
		for (TreeNode runNode : runsList.iterator(TParser.RUN)) {
			Run run = new Run();
			run.parseRun(runNode);
			this.runs.add(run);
		}
	}	
}
