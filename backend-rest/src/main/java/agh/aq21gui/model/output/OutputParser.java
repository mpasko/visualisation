/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

/**
 *
 * @author marcin
 */
public class OutputParser {
	
	public OutputParser(){
		
	}
	
	public Output parse(String out){
		Output output = new Output();
		OutputHypotheses oh = new OutputHypotheses();
		oh.content = out;
		output.outputHypotheses.add(oh);
		return output;
	}
}
