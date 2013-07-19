/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.TreeNode;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Domain {
	private long dbid = 0;
	
	public long id;
	
	private static long generator = 0;
	
	private String name;
	private String domain;
	private String parameters;
	//private boolean brace=false;
	
	public long getdbid(){
		return dbid;
	}
	
	public void setdbid(long id){
		this.dbid = id;
	}
	
	@XmlElement(name="name")
	public String getname(){
		return name;
	}
	
	public void setname(String name){
		this.name = name;
	}
	
	@XmlElement(name="domain")
	public String getdomain(){
		return domain;
	}
	
	public void setdomain(String name){
		this.domain = name;
	}
	
	@XmlElement(name="parameters")
	public String getparameters(){
		return parameters;
	}
	
	public void setparameters(String name){
		this.parameters = name;
	}
	
	public Domain(){
		this.id = generator;
		generator++;
	}
	
	private boolean estimate_brace(){
		boolean nobrace = domain.equalsIgnoreCase("continuous");
		nobrace |= parameters.length()==0;
		return !(nobrace);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(' ')
				.append(domain).append(' ');
		parameters=parameters.replace("{", "").replace("}", "");
		if (estimate_brace()){
			builder.append("{").append(parameters).append("}");
		} else {
			builder.append(parameters);
		}
		builder.append('\n');
		return builder.toString();
	}

	void parseDomain(TreeNode node) {
		name = node.childAt(0, TParser.ID).value();
		domain = node.childAt(1, TParser.ID).value();
		CommonTree args = (CommonTree) node.tree().getChild(2);
		if(args.getType()==TParser.DOMAIN_ARGS){
			parameters = parseParams(args);
		}else if(args.getType()==TParser.DOMAIN_ARGS_BRACE){
	//		brace = true;
			parameters = parseParams(args);
		}else{
			parameters = "";
		}
	}

	public static String parseParams(CommonTree args) {
		StringBuilder pbuild = new StringBuilder();
		for(Object item : args.getChildren()){
			CommonTree arg = (CommonTree) item;
			if(pbuild.length()>0){
				pbuild.append(", ");
			}
			pbuild.append(arg.getText());
		}
		return pbuild.toString();
	}
}
