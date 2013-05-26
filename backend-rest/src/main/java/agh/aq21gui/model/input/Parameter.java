/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Parameter {
	@XmlElement
	public String name;
	@XmlElement
	public String value;
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(" = ").append(value).append('\n');
		return builder.toString();
	}
}
