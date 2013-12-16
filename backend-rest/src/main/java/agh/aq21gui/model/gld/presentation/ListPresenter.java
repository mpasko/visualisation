/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld.presentation;

import agh.aq21gui.algorithms.structures.Mesh;
import agh.aq21gui.algorithms.structures.MeshCell;
import agh.aq21gui.model.gld.processing.CellValue;
import agh.aq21gui.model.gld.processing.Evaluator;
import agh.aq21gui.model.output.ClassDescriptor;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class ListPresenter implements Presenter{
	private CellValue emptyValue;

	public ListPresenter(Evaluator elements, Mesh mesh) {
	//	throw new UnsupportedOperationException("Not yet implemented");
		this.emptyValue = new CellValue(new ClassDescriptor());
	}

	@Override
	public Object getValues(Iterable<CellValue> mesh_cells) {
		List<Cell> cells = new LinkedList<Cell>();
		for (CellValue cellValue : mesh_cells) {
			if (cellValue==null) {
				cellValue = this.emptyValue;
			}
			Cell c = new Cell(cellValue);
			cells.add(c);
		}
		return cells;
	}
	
}
