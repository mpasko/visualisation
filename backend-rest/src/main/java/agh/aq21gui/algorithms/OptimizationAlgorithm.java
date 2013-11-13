/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

/**
 *
 * @author marcin
 */
public abstract class OptimizationAlgorithm {
	
	private int iterationCounter = 0;
	private static final boolean LOGGING_ENABLED = true;
	
	public void run(State state){
		init(state);
		while(!toBeStopped()){
			iteration();
			iterationCounter++;
			if(LOGGING_ENABLED){
				logState();
			}
		}
		finish();
	}
	
	public int getIteration(){
		return iterationCounter;
	}
	
	public abstract void init(State state);
	public abstract void iteration();
	public abstract boolean toBeStopped();
	public abstract void finish();
	public abstract State getResult();

	abstract void logState();
}
