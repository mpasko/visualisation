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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Hypothesis {
	private long id=0;
	
	@XmlElement
	public String name;
	
	private ClassesGroup classes=null;
	protected String LABEL;
	
	@XmlElement(name="classes")
	public void setClasses(List<ClassDescriptor> classesDescriptors){
		classes.descriptors=classesDescriptors;
	}
	
	public List<ClassDescriptor> getClasses(){
		return classes.descriptors;
	}
	
	@XmlElement
	public List<Rule> rules = new LinkedList<Rule>();
	
	public Hypothesis(){
		LABEL = "Output_hypotheses";
	}
	
	public void addBody(TreeNode tree){
		/* now: */
		TreeNode classesTree = tree.childAt(0, TParser.CLASSES);
		classes = new ClassesGroup(classesTree);
		for(TreeNode ruleNode : tree.iterator(TParser.RULE)){
			Rule ruleObject = new Rule();
			ruleObject.parseRule(ruleNode);
			rules.add(ruleObject);
		}
		/* before: *x/
		if(tree.getType() == TParser.HYPOTHESIS_BODY){
			CommonTree classesTree = (CommonTree)tree.getChild(0);
			classes = new ClassesGroup(classesTree);
			for (int rule = 1; rule < tree.getChildCount(); ++rule){
				Rule ruleObject = new Rule((CommonTree)tree.getChild(rule));
				rules.add(ruleObject);
			}
		}else{
			Logger.getLogger("Interpreter").severe("Error! Expected HYPOTHESIS_BODY, received:");
			Logger.getLogger("Interpreter").severe(tree.toString());
		}
		/* */
	}
	
/*	
	public void addParam(CommonTree tree){
		if(tree.getType() == TParser.HYPOTHESIS_BODY){
			
		}else{
			Logger.getLogger("Interpreter").severe("Error! Expected HYPOTHESIS_BODY, received:");
			Logger.getLogger("Interpreter").severe(tree.toString());
		}
	}
*/

	@Override
	public String toString() {
		if (rules.isEmpty()) {
			return "";
		}
		StringBuilder builder = FormatterUtil.begin(LABEL, name);
		FormatterUtil.appendAll(builder, rules, 1);
		return FormatterUtil.terminate(builder);
	}

	void traverse() {
		this.classes.traverse();
		for(Rule r : rules){
			r.traverse();
		}
	}
	
}
