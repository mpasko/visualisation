/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
public class OutputHypotheses implements IAQ21Deserializable{
	
	public List<Hypothesis> hypotheses=new LinkedList<Hypothesis> ();
	
	public OutputHypotheses(){
	}
	
	public void addHypothesis(CommonTree tree){
		if(tree.getType() == TParser.HYPOTHESES){
			Hypothesis hypothesis = new Hypothesis();
			hypothesis.name = tree.getChild(0).getText();
			hypothesis.addBody((CommonTree)tree.getChild(1));
			hypotheses.add(hypothesis);
		}else{
			Logger.getLogger("Interpreter").severe("Error! Expected Hypotheses, received:");
			Logger.getLogger("Interpreter").severe(tree.toString());
		}
	}

	@Deprecated
	public String content;
}
