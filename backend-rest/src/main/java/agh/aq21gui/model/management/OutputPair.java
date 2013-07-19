/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.management;
import agh.aq21gui.model.output.Output;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class OutputPair {
	private long id=0;
	
	private String name;
	@XmlElement
	public Output value;
	
	public long getdbid(){
		return id;
	}
	
	public void setdbid(long id){
		this.id = id;
	}
	
	@XmlElement(name="name")
	public String getname(){
		return name;
	}
	
	public void setname(String name){
		this.name=name;
	}
	
	public OutputPair(){
		
	}

	public OutputPair(String name, Output value) {
		this.name=name;
		this.value=value;
	}
}
