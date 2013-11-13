/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

/**
 *
 * @author marcin
 */
public abstract class State {
	public abstract double targetFunction();

	abstract void modifyItself();

	abstract State cloneItself();
	
	abstract void printIt();
}
