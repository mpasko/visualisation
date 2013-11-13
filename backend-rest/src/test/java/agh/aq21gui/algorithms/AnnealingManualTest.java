/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

/**
 *
 * @author marcin
 */
public class AnnealingManualTest {
	
	public static void main(String args[]){
		for (int i = 0; i < 10; ++i) {
			test();
			System.out.println();
		}
	}
	
	private static void test(){
		SimulatedAnnealing alg = new SimulatedAnnealing();
		double initialValue = Math.random()*20.0 - 10.0;
		alg.run(new TestState(initialValue));
		TestState resultState = (TestState) alg.getResult();
		System.out.printf("Result: %f -> %f\n", resultState.getValue(), resultState.targetFunction());
		TestState left = new TestState(resultState.getValue()-1);
		System.out.printf("Left: %f -> %f\n", left.getValue(), left.targetFunction());
		TestState right = new TestState(resultState.getValue()-1);
		System.out.printf("Right: %f -> %f\n", right.getValue(), right.targetFunction());
	}
}
