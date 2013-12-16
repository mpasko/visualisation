/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld.presentation;

import agh.aq21gui.model.gld.processing.CellValue;
import agh.aq21gui.model.output.ClassDescriptor;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Cell{
//	private Coordinate coordinate;
	private CellValue value;

	public Cell(CellValue value){
		this.value=value;
	}
	
	/**
	 * @return the value
	 */
	@JsonIgnore
	public CellValue getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	@JsonIgnore
	public void setValue(CellValue value) {
		this.value = value;
	}
	
	@JsonProperty("name")
	public String getName(){
		return this.value.getName();
	}
	
	public void setName(String name){
		
	}
	
	/**
	 * @return the descriptors
	 */
	@JsonProperty("descriptors")
	public List<ClassDescriptor> getDescriptors() {
		return value.getDescriptors();
	}

	/**
	 * @param descriptors the descriptors to set
	 */
	public void setDescriptors(List<ClassDescriptor> descriptors) {
		this.value.setDescriptors(descriptors);
	}
}
