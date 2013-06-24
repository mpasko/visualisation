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
public class Run extends Test{
	
	void parseRun(TreeNode runNode) {
		name = runNode.childAt(0, TParser.ID).value();
		TreeNode runParams = runNode.childAt(1, TParser.RUNS_PARAMS);
		runSpecificParameters.parseParams(runParams);
	}
}
