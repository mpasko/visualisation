/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.input.EventsGroup;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.InputHypotheses;
import agh.aq21gui.model.input.TestingEventsGroup;
import agh.aq21gui.utils.TreeNode;
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
public class Output extends Input{
	public long id=0;
	
	OutputHypotheses outHypo = new OutputHypotheses();
	
	@XmlElement(name="outputHypotheses")
	@Override
	public void setOutputHypotheses(List<Hypothesis> hypotheses){
		this.outHypo.hypotheses = hypotheses;
	}
	
	@Override
	public List<Hypothesis> getOutputHypotheses(){
		return this.outHypo.hypotheses;
	}
	
	@XmlElement(name="id")
	public void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return this.id; 
	}
	
	@XmlElement(name="domains")
	@Override
	public void setDomains(List<Domain> domains){
		super.setDomains(domains);
	}
	
	@Override
	public List<Domain> getDomains(){
		return super.getDomains();
	}
	
	public Output(){}
	
	public Output(CommonTree tree){
/*after refactor *x/
		TreeNode childTree = new TreeNode(tree, TParser.OUTPUT).childOf(TParser.HYPOTHESES);
		outHypo.parseHypothesis(childTree);
/*before refactor: */
		for (Object t: tree.getChildren()){
			CommonTree childTree = (CommonTree)t;
			if(childTree.getType()==TParser.HYPOTHESES){
				outHypo.parseHypothesis(new TreeNode(childTree,TParser.HYPOTHESES));
			}else if(childTree.getType()==TParser.INPUT_HYPOTHESES){
				InputHypotheses inHypho = gInputHypho();
				inHypho.parseInputHypothesis(new TreeNode(childTree,TParser.INPUT_HYPOTHESES));
				sOutputHypho(inHypho);
			}else if(childTree.getType()==TParser.DOMAINS){
				DomainsGroup domains = new DomainsGroup();
				domains.parseDomains(new TreeNode(childTree,TParser.DOMAINS));
				this.sDomainsGroup(domains);
			}else if(childTree.getType()==TParser.ATTRIBUTES){
				AttributesGroup domains = new AttributesGroup();
				domains.parseAttributes(new TreeNode(childTree,TParser.ATTRIBUTES));
				this.sAttributesGroup(domains);
			}else if(childTree.getType()==TParser.RUNS){
				runsGroup.parseRuns(new TreeNode(childTree,TParser.RUNS));
			}else if(childTree.getType()==TParser.EVENTS){
				EventsGroup group = new EventsGroup();
				group.parseEvents(new TreeNode(childTree,TParser.EVENTS));
				this.sEG(group);
			}else if(childTree.getType()==TParser.TESTS){
				testsGroup.parseTests(new TreeNode(childTree,TParser.TESTS));
			}else if(childTree.getType()==TParser.TESTING_EVENTS){
				TestingEventsGroup group = new TestingEventsGroup();
				group.parseEvents(new TreeNode(childTree,TParser.TESTING_EVENTS));
				this.sTEG(group);
			}
			else{
//				Logger.getLogger("Interpreter").info("Received:");
//				Logger.getLogger("Interpreter").info(childTree.toString());
			}
		}
/* */
	}
		
	@Override
	public void traverse(){
		super.traverse();
		this.outHypo.traverse();
	}
}
