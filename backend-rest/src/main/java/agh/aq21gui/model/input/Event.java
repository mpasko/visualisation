/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
//@XmlRootElement
public class Event {
	public LinkedList<String> values;
	
	public Event(){
		values = new LinkedList<String>();
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(String value : values){
			builder.append(value);
			if(!value.equals(values.getLast())){
				builder.append(", ");
			}else{
				builder.append('\n');
			}
		}
		return builder.toString();
	}
}
