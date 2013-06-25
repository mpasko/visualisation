/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.model.output.OutputHypotheses;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.TreeNode;

/**
 *
 * @author marcin
 */
public class InputHypotheses extends OutputHypotheses{
	
	@Override
	public String toString(){
		if(hypotheses.isEmpty()){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		FormatterUtil.appendAll(builder, hypotheses, 0);
		return builder.toString();
	}

	public void parseInputHypothesis(TreeNode treeNode) {
		InputHypothesis hypothesis = new InputHypothesis();
		hypothesis.name = treeNode.childAt(0, TParser.ID).value();
		hypothesis.addBody(treeNode.childAt(1, TParser.HYPOTHESIS_BODY));
		hypotheses.add(hypothesis);
	}
}
