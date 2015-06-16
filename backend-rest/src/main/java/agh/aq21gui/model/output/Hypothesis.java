/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.TreeNode;
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
public class Hypothesis {
    
	public String name;
	private ClassesGroup classes=new ClassesGroup();
	protected String LABEL;
	public List<Rule> rules = new LinkedList<Rule>(); 
	
	public Hypothesis(){
		LABEL = "Output_hypotheses";
        name = "hypothesis0";
	}
    
    public Hypothesis(Rule...sel) {
        this();
		this.rules = new LinkedList<Rule>(Arrays.asList(sel));
    }
	
	@XmlElement(name="classes")
	public void setClasses(List<ClassDescriptor> classesDescriptors){
		classes.descriptors=classesDescriptors;
	}
	
	public List<ClassDescriptor> getClasses(){
		return classes.descriptors;
	}
	
	@XmlElement(name="name")
	public void setName(String nm){
		this.name = nm;
	}
	
	public String getName(){
		return this.name; 
	}
	
	@XmlElement(name="rules")
	public void setRules(List<Rule> rules){
		this.rules=rules;
	}
	
	public List<Rule> getRules(){
		return rules;
	}
	
	public void addClass(ClassDescriptor desc){
		this.classes.descriptors.add(desc);
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
        String string = "";
		if (!rules.isEmpty()) {
            StringBuilder builder = FormatterUtil.begin(LABEL, name);
            builder.append(printClasses());
            for(Rule rule : rules){
                builder.append("\n   <-- ");
                builder.append(rule.toString());
                builder.append("\n");
            }
            string = FormatterUtil.terminate(builder);
        }
		return string;
	}

	void traverse() {
		this.classes.traverse();
		for(Rule r : rules){
			r.traverse();
		}
	}

    public String printClasses() {
        return classes.toString();
    }

    public boolean matchesEvent(Map<String, Object> map) {
        boolean matches = false;
        for (Rule rule : this.rules) {
            matches |= rule.matchesEvent(map);
        }
        return matches;
    }
	
}
