/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class OutputHypotheses implements IAQ21Deserializable{
	public String name;
//	@Deprecated
	public String content;
}
