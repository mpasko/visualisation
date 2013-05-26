/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Attribute {
	@XmlAttribute
	public String name;
	@XmlAttribute
	public String domain="continuous";
	@XmlAttribute
	public String parameters="0, 100";
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(' ')
				.append(domain).append(' ')
				.append(parameters).append('\n');
		return builder.toString();
	}
}
