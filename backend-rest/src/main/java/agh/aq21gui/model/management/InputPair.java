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
	
	public long getdbid(){
		return id;
	}
	
	public void setdbid(long id){
		this.id = id;
	}
	
	@XmlElement(name="value")
	public Input getValue(){
		return value;
	}
	
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
