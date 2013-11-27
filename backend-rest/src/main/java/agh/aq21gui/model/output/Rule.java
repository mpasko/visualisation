/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
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
public class Rule {
	
//	@XmlTransient
	private transient List<Selector> selectors = new LinkedList<Selector>();
	
	@XmlElement(name="selectors")
	public void setSelectors(List<Selector> selectors){
		this.selectors = selectors;
	}
	
	public List<Selector> getSelectors(){
		return selectors;
	}
	
	public Rule(){
		
	}
	
/* now */
	public void parseRule(TreeNode ruleNode) {
		
		TreeNode selectorsTree = ruleNode.childAt(0, TParser.SELECTORS);
		for(TreeNode selectorNode : selectorsTree.iterator(TParser.SELECTOR)){
			Selector selector = new Selector();
			selector.parseSelector(selectorNode);
			selectors.add(selector);
		}
	}
/* */	

	public void traverse() {
		for(Selector s : selectors){
			s.traverse();
		}
	}
}
