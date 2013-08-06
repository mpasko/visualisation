/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.FormatterUtil;
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
	
	private String parent;
	public List<Parameter> parameters;
	
	public UniversalParametersContainer(String parent){
		this.parent = parent;
		parameters = new LinkedList<Parameter>();
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		FormatterUtil.appendAll(builder, parameters, 3);
		return builder.toString();
	}

	void parseParams(TreeNode childAt) {
		for(TreeNode paramNode : childAt.iterator(TParser.PARAMETER)){
			Parameter param = new Parameter(parent);
			param.parseParam(paramNode);
			parameters.add(param);
		}
	}

	void sName(String name) {
		parent = name;
	}
}
