/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld.processing;

import agh.aq21gui.model.gld.presentation.Cell;
import java.util.List;

/**
 *
 * @author marcin
 */
public interface Evaluator {

	public Evaluator cloneItself();

	public List<Cell> getElements();

	public CellValue eval(Coordinate row, Coordinate col);
	
}
