/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author marcin
 */
@XmlRootElement
//@XmlSeeAlso(OutputHypotheses.class)
public class Output {
	public List<OutputHypotheses> outputHypotheses;
	
	public Output(){
		outputHypotheses = new LinkedList<OutputHypotheses>();
	}
}
