/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.model.input.Attribute;
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
public class Hypothesis {
	
	@XmlElement
	public String name;
	
	private ClassesGroup classes=null;
	
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
		
	}
	
	public void addBody(CommonTree tree){
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
	
}
