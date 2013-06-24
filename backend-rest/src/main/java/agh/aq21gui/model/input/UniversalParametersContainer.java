/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.TreeNode;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
public class UniversalParametersContainer {
	
	public List<Parameter> parameters;
	
	public UniversalParametersContainer(){
		parameters = new LinkedList<Parameter>();
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(Parameter parameter : parameters){
			builder.append(parameter.toString());
		}
		return builder.toString();
	}

	void parseParams(TreeNode childAt) {
		for(TreeNode paramNode : childAt.iterator(TParser.PARAMETER)){
			Parameter param = new Parameter();
			param.parseParam(paramNode);
			parameters.add(param);
		}
	}
}
