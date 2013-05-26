/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlTransient
public class AttributesGroup implements IAQ21Serializable{
	public List<Attribute> attributes;
	
	public AttributesGroup(){
		attributes = new LinkedList<Attribute>();
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("Attributes\n{\n");
		for(Attribute attribute : attributes){
			builder.append(attribute.toString());
		}
		builder.append("}\n");
		return builder.toString();
	}
}
