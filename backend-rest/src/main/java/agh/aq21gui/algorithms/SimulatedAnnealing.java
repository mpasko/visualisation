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
	private SAProperties properties;

	@Override
	public void init(State state) {
		if (properties == null){
			this.properties = new SAProperties();
		}
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
		return this.getIteration()>=properties.iterations;
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
		double alpha = (properties.end_temperature - properties.start_temperature)/(properties.iterations);
		this.temperature = alpha * this.getIteration() + properties.start_temperature;
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

	@Override
	public void setProperties(Object props) {
		this.properties = (SAProperties)props;
	}
	
}
