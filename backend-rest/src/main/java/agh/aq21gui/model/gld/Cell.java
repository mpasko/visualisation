/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import javax.xml.bind.annotation.XmlRootElement;
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
	@JsonProperty("value")
	public CellValue getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(CellValue value) {
		this.value = value;
	}
}
