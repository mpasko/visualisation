/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

/**
 *
 * @author marcin
 */
public class SimulatedAnnealing extends OptimizationAlgorithm{
	
	private double temperature;
	private double energyBefore,energyNow;
	private State previousState,currentState;
	private static final int MAX_ITERATIONS=400;
	private static final double START_TEMP=1;
	private static final double END_TEMP=0.001;

	@Override
	public void init(State state) {
		currentState = state;
		previousState = state;
		energyNow = currentState.targetFunction();
	}

	@Override
	public void iteration() {
		recalcTemperature();
		energyBefore = energyNow;
		previousState = currentState.cloneItself();
		currentState.modifyItself();
		energyNow = currentState.targetFunction();
		if(!newStateAcceptable()){
			currentState = previousState;
		}
	}

	@Override
	public boolean toBeStopped() {
		return this.getIteration()>=MAX_ITERATIONS;
	}

	@Override
	public void finish() {
		
	}
	
	private boolean newStateAcceptable(){
		return P(this.energyBefore,this.energyNow,temperature) >= Math.random();
	}
	
	private double P(double previousE, double newE, double temperature) {
		if(newE < previousE){
			return 1.0;
		} else {
			return Math.exp((previousE-newE)/temperature);
		}
	}
	
	private void recalcTemperature(){
		double alpha = (this.END_TEMP - this.START_TEMP)/(this.MAX_ITERATIONS);
		this.temperature = alpha * this.getIteration() + this.START_TEMP;
	}

	@Override
	public State getResult() {
		return this.currentState;
	}

	@Override
	void logState() {
		if (this.getIteration() % 50 == 0){
			this.currentState.printIt();
		}
	}
	
}
