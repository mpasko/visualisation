/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import java.util.List;

/**
 *
 * @author marcin
 */
interface Evaluator {

	public Evaluator cloneItself();

	public List<Element> getElements();

	public Value eval(Coordinate row, Coordinate col);
	
}
