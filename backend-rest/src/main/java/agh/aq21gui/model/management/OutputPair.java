/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.management;
import agh.aq21gui.model.output.Output;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class OutputPair {
	private static long id_gen=0;
	private long id=id_gen++;
	
	private String name;
	private Output value;
	private String description="";
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	@XmlElement(name="name")
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	@JsonProperty("value")
	public void setLink(Output value){
		this.value = value;
	}
	
	public String getLink(){
		return name;
	}
	
	@JsonIgnore
	public Output getValue(){
		return value;
	}
	
	@JsonIgnore
	public void setValue(Output value){
		this.value = value;
	}
	
	@XmlElement(name="description")
	public void setDescription(String d){
		this.description = d;
	}
	
	public String getDescription(){
		return description;
	}
	
	public OutputPair(){
		
	}

	public OutputPair(String name, Output value) {
		this.name=name;
		this.value=value;
	}

	void traverse() {
		this.value.traverse();
	}
}
