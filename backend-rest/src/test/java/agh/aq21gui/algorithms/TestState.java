/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

/**
 *
 * @author marcin
 */
class TestState extends State {
	private double x;
	private final double left = -10;
	private final double right = 10;

	public TestState(double nx) {
		x = nx;
	}

	@Override
	public double targetFunction() {
		return 5 + x * x / 10 + Math.cos(x * Math.PI / 5 * 4);
	}

	@Override
	void modifyItself() {
		double delta = Math.random()/2.0 - 0.25;
		x += delta;
		if (x > 10) {
			x = x - 1;
		}
		if (x < -10) {
			x = x + 1;
		}
	}

	@Override
	State cloneItself() {
		return new TestState(x);
	}

	public double getValue() {
		return x;
	}

	@Override
	void printIt() {
		System.out.printf("x: %f, y: %f\n",this.x, this.targetFunction());
	}
	
}
