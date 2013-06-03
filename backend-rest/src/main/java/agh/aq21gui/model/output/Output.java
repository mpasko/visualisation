/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
@XmlRootElement
//@XmlSeeAlso(OutputHypotheses.class)
public class Output {
	
	OutputHypotheses outHypo = new OutputHypotheses();
	
	@XmlElement(name="outputHypotheses")
	public void setOutputHypotheses(List<Hypothesis> hypotheses){
		this.outHypo.hypotheses = hypotheses;
	}
	
	public List<Hypothesis> getOutputHypotheses(){
		return this.outHypo.hypotheses;
	}
	
	public Output(){}
	
	public Output(CommonTree tree){
		for (Object t: tree.getChildren()){
			CommonTree childTree = (CommonTree)t;
			if(childTree.getType()==TParser.HYPOTHESES){
				outHypo.addHypothesis(childTree);
			}else{
//				Logger.getLogger("Interpreter").info("Received:");
//				Logger.getLogger("Interpreter").info(childTree.toString());
			}
		}
	}
}
