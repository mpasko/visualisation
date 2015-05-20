/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.TreeNode;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class OutputHypotheses implements IAQ21Deserializable{
	
	public List<Hypothesis> hypotheses=new LinkedList<Hypothesis> ();
	
	public OutputHypotheses(){
		//content = "Raw string content is deprecated. Use json fields!";
	}
	
	public void parseHypothesis(TreeNode tree){
		/*after refactor: */
			Hypothesis hypothesis = new Hypothesis();
			hypothesis.name = tree.childAt(0, TParser.ID).value();
			hypothesis.addBody(tree.childAt(1, TParser.HYPOTHESIS_BODY));
			hypotheses.add(hypothesis);
		/*before refactor: *x/
		if(tree.getType() == TParser.HYPOTHESES){
			Hypothesis hypothesis = new Hypothesis();
			hypothesis.name = tree.getChild(0).getText();
			hypothesis.addBody((CommonTree)tree.getChild(1));
			hypotheses.add(hypothesis);
		}else{
			Logger.getLogger("Interpreter").severe("Error! Expected Hypotheses, received:");
			Logger.getLogger("Interpreter").severe(tree.toString());
		}
		/* */
	}

	//@Deprecated
	//public String content;

	public void traverse() {
		for(Hypothesis h : this.hypotheses){
			h.traverse();
		}
	}

    @Override
    public String toString() {
        String out = "";
        if (!hypotheses.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            FormatterUtil.appendAll(builder, hypotheses, 0);
            out = builder.toString();
        }
        return out;
    }
}
