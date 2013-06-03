/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class ClassDescriptor {
	@XmlElement
	public String name;
	@XmlElement
	public String comparator;
	@XmlElement
	public String value;
}
