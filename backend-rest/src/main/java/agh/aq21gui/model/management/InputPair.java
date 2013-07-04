/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.management;

import agh.aq21gui.model.input.Input;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class InputPair {
	private long id=0;
	
	private String name;
	private Input value;
	
	public long getid(){
		return id;
	}
	
	public void setid(long id){
		this.id = id;
	}
	
	@XmlElement(name="value")
	public Input getvalue(){
		return value;
	}
	
	public void setvalue(Input value){
		this.value = value;
	}
	
	@XmlElement(name="name")
	public String getname(){
		return name;
	}
	
	public void setname(String name){
		this.name=name;
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
}
