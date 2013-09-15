/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.TreeNode;
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
@XmlRootElement
public class Rule {
	
	@XmlElement
	public List<Selector> selectors = new LinkedList<Selector>();

//	@XmlElement
//	public String bulkData = "nevermind :)";
	
	Rule(){
		
	}
	
/* before *x/	
	Rule(TreeNode rule) {
		//System.out.println("uno");
		CommonTree selectorsTrees = (CommonTree) child.getChild(0);
		if(selectorsTrees.getType()==TParser.SELECTORS){
		//	System.out.println("due");
			if (selectorsTrees.getChildCount()==0){
				Logger.getLogger("Interpreter").severe("Error! Rule has 0 selectors!");
			}
			for (Object t: selectorsTrees.getChildren()){
		//		System.out.println("tre");
				CommonTree selectorTree = (CommonTree)t;
				Selector selector = new Selector();
				selector.name = selectorTree.getChild(0).getText();
				selector.comparator = selectorTree.getChild(1).getText();
				selector.value = selectorTree.getChild(2).getText();
				selectors.add(selector);
		//		System.out.println("quattro");
			}
		}else{
			Logger.getLogger("Interpreter").severe("Error! Expected SELECTORS, received:");
			Logger.getLogger("Interpreter").severe(selectorsTrees.toString());
		}
	}
	
/* now */
	void parseRule(TreeNode ruleNode) {
		
		TreeNode selectorsTree = ruleNode.childAt(0, TParser.SELECTORS);
		for(TreeNode selectorNode : selectorsTree.iterator(TParser.SELECTOR)){
			Selector selector = new Selector();
			selector.parseSelector(selectorNode);
			selectors.add(selector);
		}
	}
/* */	

	void traverse() {
		for(Selector s : selectors){
			s.traverse();
		}
	}
}
