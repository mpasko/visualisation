/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.management;

import agh.aq21gui.adapters.InputPairAdapter;
import agh.aq21gui.model.input.Input;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class InputPair {
	private static long id_gen=0;
	private long id=id_gen++;
	
	private String name;
	private Input value;
	private String description="";
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	@JsonProperty("value")
	public void setLink(Input value){
		this.value = value;
	}
	
	public String getLink(){
		return name;
	}
	
	@JsonIgnore
	public Input getValue(){
		return value;
	}
	
	@JsonIgnore
	public void setValue(Input value){
		this.value = value;
	}
	
	@XmlElement(name="name")
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	@XmlElement(name="description")
	public void setDescription(String d){
		this.description = d;
	}
	
	public String getDescription(){
		return description;
	}
	
	public InputPair(){
		
	}

	public InputPair(String name, Input value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String toString(){
		StringBuilder build = new StringBuilder("name:");
		build.append(name);
		build.append("value:");
		build.append(value.toString());
		return build.toString();
	}

	void traverse() {
		this.value.traverse();
	}
}
