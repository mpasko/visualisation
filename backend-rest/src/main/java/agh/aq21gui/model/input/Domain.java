/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.TreeNode;
import agh.aq21gui.utils.Util;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Domain implements IAQ21Serializable{
	private long dbid = 0;
	
	public long id;
	
	private static long generator = 0;
	
	private String name;
	private String domain;
	private String parameters;
	private List<String> range = new LinkedList<String>();
	//private boolean brace=false;
	
	@XmlElement(name="id")
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
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
		this.buildParams();
		this.parameters = this.bracify(parameters);
		return parameters;
	}
	
	public void setparameters(String params){
		String params_nonbre = params.replace("{", "").replace("}", "").replace(" ", "");
		this.range = new LinkedList<String>();
		for (String item : params_nonbre.split(",")) {
			if (!item.isEmpty()) {
				this.range.add(item);
			}
		}
		this.parameters = params_nonbre;
	}
	
	public Domain(){
		this.id = generator;
		generator++;
	}
	
	private boolean estimate_brace(){
		boolean nobrace=false;
		if (domain != null) {
			nobrace	= domain.equalsIgnoreCase("continuous");
		}
		nobrace |= emptyParams();
		return !(nobrace);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(' ')
				.append(domain);
		if (!emptyParams()){
			builder.append(' ');
		    builder.append(this.getparameters());
		}
		builder.append('\n');
		return builder.toString();
	}
	
	private String bracify(String parameters){
		StringBuilder builder = new StringBuilder();
		if (!emptyParams()) { 
            parameters=parameters.replace("{", "").replace("}", "");
            if (estimate_brace()){
                builder.append("{").append(parameters).append("}");
            } else {
            	builder.append(parameters);
            }
        }
		return builder.toString();
	}

	void parseDomain(TreeNode node) {
		name = node.childAt(0, TParser.ID).value();
		domain = node.childAt(1, TParser.ID).value();
		CommonTree args = (CommonTree) node.tree().getChild(2);
		if(args.getType()==TParser.DOMAIN_ARGS){
			parseRange(args);
		}else if(args.getType()==TParser.DOMAIN_ARGS_BRACE){
			parseRange(args);
		}else{
			parameters = "";
		}
	}
	
	private void parseRange(CommonTree args) {
		for(Object item : args.getChildren()){
			CommonTree arg = (CommonTree) item;
			this.range.add(arg.getText());
		}
		buildParams();
	}

	private void buildParams() {
		StringBuilder pbuild = new StringBuilder();
		for(String param : this.range){
			if(pbuild.length()>0){
				pbuild.append(", ");
			}
			pbuild.append(param);
		}
		this.parameters = pbuild.toString();
	}

	void traverse() {
		if(name.isEmpty());
	}

	public void setRange(Double min, Double max) {
/*		StringBuilder build = new StringBuilder("{");
		build.append(min);
		build.append(", ");
		build.append(max);
		build.append("}");
		this.parameters = build.toString();
		*/
		setRange(Util.strings(min.toString(),max.toString()));
	}

	public void setRange(List<String> values) {
		this.range = values;
		buildParams();
	}
	
	
	public List<String> getRange() {
		return this.range;
	}

	public boolean emptyParams() {
		Util.isNull(parameters, "parameters");
		Util.isNull(range, "range");
		return ((this.parameters==null)||this.parameters.isEmpty()) && (this.range.isEmpty());
	}
}
