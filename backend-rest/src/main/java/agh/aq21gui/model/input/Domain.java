/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Domain {
	public String name;
	public String subdomain;
	public String parameters;
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(' ')
				.append(subdomain).append(' ')
				.append(parameters).append('\n');
		return builder.toString();
	}
}
