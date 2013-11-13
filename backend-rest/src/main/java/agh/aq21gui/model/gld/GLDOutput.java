/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class GLDOutput {
	private ArgumentsGroup rows;
	private ArgumentsGroup columns;
	
	@JsonProperty("rows")
	public void setRows(List<Argument> list){
		this.rows = new ArgumentsGroup(list);
	}
	
	public List<Argument> getRows(){
		return this.rows.getArguments();
	}
	
	@JsonProperty("columns")
	public void setColumns(List<Argument> list){
		this.columns = new ArgumentsGroup(list);
	}
	
	public List<Argument> getColumns(){
		return this.columns.getArguments();
	}

	public void print() {
		System.out.println("Columns: ");
		this.columns.print();
		System.out.println("Rows: ");
		this.rows.print();
	}
}
