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
public class CacheEvaluator implements Evaluator {
	
	private CellValue value;
	

	@Override
	public Evaluator cloneItself() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Cell> getElements() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public CellValue eval(Coordinate row, Coordinate col) {
		return value;
	}
	
}
