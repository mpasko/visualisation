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
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Domain extends NameValueEntity{
	
	private static long generator = 0;
	
	private String domain="";
	private String parameters="";
	//private boolean brace=false;
    
    public Domain(String name, String domain){
        this();
        this.name = name;
        this.domain=domain;
    }
	
	@JsonProperty("name")
	public String getname(){
		return name;
	}
	
	public void setname(String name){
		this.name = name;
	}
	
	@JsonProperty("domain")
	public String getdomain() {
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
		this.set_elements = new LinkedList<String>();
		for (String item : params_nonbre.split(",")) {
			if (!item.isEmpty()) {
				this.set_elements.add(item);
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
		if (!domain.isEmpty()) {
			nobrace	= isContinuous();
			nobrace |= isInteger();
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
			this.set_elements.add(arg.getText());
		}
		buildParams();
	}

	private void buildParams() {
		StringBuilder pbuild = new StringBuilder();
		for(String param : this.set_elements){
			if(pbuild.length()>0){
				pbuild.append(", ");
			}
			pbuild.append(param);
		}
		this.parameters = pbuild.toString();
	}

	public void setRange(Double min, Double max) {
        if (isInteger()) {
            Integer a = min.intValue();
            Integer b = max.intValue();
            setRange(Util.strings(a.toString(),b.toString()));
        }
        else {
            setRange(Util.strings(min.toString(),max.toString()));
        }
		
	}

	public void setRange(List<String> values) {
		this.set_elements = values;
		buildParams();
	}
	
	
	public List<String> getRange() {
		return this.set_elements;
	}
    
    @Deprecated
     @JsonIgnore
    public List<String> getRangeRecursively(DomainsGroup dg) {
		return dg.getRangeRecursively(this);
	}

	public boolean emptyParams() {
		Util.isNull(parameters, "parameters");
		Util.isNull(set_elements, "range");
		return this.parameters.isEmpty() && (this.set_elements.isEmpty());
	}

	
    @Deprecated
     @JsonIgnore
	public String getdomainNameRecursively(DomainsGroup dg){
        Domain domainObject = dg.getdomainObjectRecursively(this);
		return domainObject.getdomain();
	}
    
    @Deprecated
     @JsonIgnore
    public Domain getdomainObjectRecursively(DomainsGroup dg){
		return dg.getdomainObjectRecursively(this);
	}
    @JsonIgnore
    public boolean isContinuous() {
        return domain.equalsIgnoreCase("continuous");
    }
    @JsonIgnore
    public boolean isInteger() {
        return domain.equalsIgnoreCase("integer");
    }

    @JsonIgnore
    public boolean isNominal() {
        return domain.equalsIgnoreCase("nominal");
    }
    @JsonIgnore
    public boolean isLinear() {
        return domain.equalsIgnoreCase("linear");
    }
     @JsonIgnore
    public boolean isTerminal() {
        return isContinuous()||isInteger()||isLinear()||isNominal();
    }
}
