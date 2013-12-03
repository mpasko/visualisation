/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

import agh.aq21gui.model.gld.GLDInput;
import agh.aq21gui.model.gld.GLDOutput;

/**
 *
 * @author marcin
 */
public class GLDOptimizer {
	
	private OptimizationAlgorithm algorithm;
	GLDConverter converter;

	public GLDOptimizer(GLDInput input, OptimizationAlgorithm alg) {
		this.algorithm = alg;
		converter = new GLDConverter(input);
	}
	
	public GLDOutput getInitialData(){
		return converter.convert();
	}

	public GLDOutput optimize() {
		GLDState initialState = GLDState.build(getInitialData());
		algorithm.run(initialState);
		GLDState resultState = (GLDState)algorithm.getResult();
		return resultState.getData();
	}
	
}
