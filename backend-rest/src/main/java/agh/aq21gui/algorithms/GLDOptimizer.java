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
	private GLDOutput initialData;

	public GLDOptimizer(GLDInput input, OptimizationAlgorithm alg) {
		this.algorithm = alg;
		this.initialData = convert(input);
	}

	public GLDOutput optimize() {
		GLDState initialState = GLDState.build(initialData);
		algorithm.run(initialState);
		GLDState resultState = (GLDState)algorithm.getResult();
		return resultState.getData();
	}

	private GLDOutput convert(GLDInput input) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
}
