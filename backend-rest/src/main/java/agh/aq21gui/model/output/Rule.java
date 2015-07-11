/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.TreeNode;
import agh.aq21gui.utils.Util;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Rule {
	
//	@XmlTransient
	private transient List<Selector> selectors = new LinkedList<Selector>();

    public Rule() {
        
    }
    
    public Rule(Selector...sel) {
		selectors = new LinkedList<Selector>(Arrays.asList(sel));
    }
	
	@XmlElement(name="selectors")
	public void setSelectors(List<Selector> selectors){
		this.selectors = selectors;
	}
	
	public List<Selector> getSelectors(){
		return selectors;
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
    
    //Rule is impossible to parse this way :(
    /*
    public static Rule parse(String string) throws RecognitionException {
        String ruleString = String.format("<-- %s", string);
        TParser tokens = new OutputParser().prepareParser(ruleString);
        CommonTree cd_tree = (CommonTree) tokens.rule().getTree();
        TreeNode node = new TreeNode(cd_tree, TParser.RULE);
        Rule rule = new Rule();
        rule.parseRule(node);
        return rule;
    }
    */

	public void traverse() {
		for(Selector s : selectors){
			s.traverse();
		}
	}

    public void addSelector(Selector selector) {
        this.selectors.add(selector);
    }
    
    @Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for (Selector selector: this.selectors){
    		builder.append(selector.toString());
        }
		return builder.toString();
	}

    public boolean matchesEvent(Map<String, Object> map) {
        boolean matches = true;
        for (Selector sel : this.getSelectors()) {
            Object eventValue = map.get(sel.getName());
            Util.isNull(eventValue, "eventValue");
            Util.isNull(sel, "sel");
            matches &= sel.matchesValue(eventValue.toString());
        }
        return matches;
    }
    
    public boolean matchesEventStrictly(Map<String, Object> map) {
        boolean matches = true;
        for (Selector sel : this.getSelectors()) {
            Object eventValue = map.get(sel.getName());
            //Util.isNull(eventValue, "eventValue");
            //Util.isNull(sel, "sel");
            matches &= sel.matchesValueStrictly(eventValue.toString());
        }
        return matches;
    }

}
