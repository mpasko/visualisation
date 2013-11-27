/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

import agh.aq21gui.algorithms.structures.Mesh;
import agh.aq21gui.algorithms.structures.MeshCell;
import agh.aq21gui.model.gld.Argument;
import agh.aq21gui.model.gld.CellValue;
import agh.aq21gui.model.gld.Coordinate;
import agh.aq21gui.model.gld.GLDOutput;
import agh.aq21gui.model.gld.Value;
import java.util.List;

/**
 *
 * @author marcin
 */
public class GLDState extends State{

	public static GLDState build(GLDOutput initialData) {
		GLDState state = new GLDState();
		state.data = initialData;
		return state;
	}
	
	private double p=1.0;
	private GLDOutput data;
	public double repartition_prob=0.05;

	@Override
	public double targetFunction() {
		return this.getClusters() + p*this.getGoldenRatioCloseness();
	}
	
	public void setRatioImportance(int newp){
		this.p = newp;
	}
	
	public void setRepartitionProb(double prob) {
		this.repartition_prob = prob;
	}
	
	public double getClusters(){
		/* Create groups of clusters*/
		Mesh mesh = new Mesh();
		int maxClusters = data.height()*data.width();
		MergingSets sets = new ArrayMergingSets(maxClusters);
		List<Coordinate> colSeq = data.getHCoordSequence();
		List<Coordinate> rowSeq = data.getVCoordSequence();
		CellValue prevValue = null;
		MeshCell prevCell = null;
		
		for (Coordinate row: rowSeq) {
			for (Coordinate col : colSeq) {
				CellValue value = data.eval(row,col);
				MeshCell cell = mesh.transform(col, row);
				cell.set(value);
				sets.newElement(cell);
			}
		}
		//left to right
		for (Coordinate row: rowSeq) {
			prevCell = null;
			for (Coordinate col : colSeq) {
				MeshCell cell = mesh.transform(col, row);
				CellValue value = (CellValue)cell.get();
				
				if ((prevCell!=null)) {
					prevValue = (CellValue)prevCell.get();
					if ((value==null && prevValue==null)||value.compare(prevValue)) {
						sets.merge(cell, prevCell);
					}
				}
				prevCell=cell;
			}
		}
		//top to bottom
		for (Coordinate col : colSeq) {
			prevCell = null;
			for (Coordinate row: rowSeq) {
				MeshCell cell = mesh.transform(col, row);
				CellValue value = (CellValue)cell.get();
				
				if ((prevCell!=null)) {
					prevValue = (CellValue)prevCell.get();
					if ((value==null && prevValue==null)||value.compare(prevValue)) {
						sets.merge(cell, prevCell);
					}
				}
				prevCell=cell;
			}
		}
		return sets.count();
	}
	
	public double getGoldenRatioCloseness(){
		double width = data.width();
		double height = data.height();
		return Math.abs(width/height - 377.0/233.0);
	}

	GLDOutput getData() {
		return this.data;
	}

	@Override
	public void modifyItself() {
		List<Argument> rows = data.getRows();
		List<Argument> cols = data.getColumns();
		if (Math.random()<repartition_prob) {
			if ((rows.size()>1) && (Math.random()>0.5)){
				int selected = (int) Math.round((rows.size()-1)*Math.random());
				moveItem(rows, selected, cols, 0);
			} else if (cols.size()>1) {
				int selected = (int) Math.round((cols.size()-1)*Math.random());
				moveItem(cols, selected, rows, 0);
			}
		} else {
			if ( (rows.size()>=2) && (Math.random()>0.5) ) {
				int from = (int) Math.round((rows.size()-1)*Math.random());
				int to = (int) Math.round((rows.size()-2)*Math.random());
				moveItem(rows, from, rows, to);
			} else if (cols.size()>=2) {
				int from = (int) Math.round((cols.size()-1)*Math.random());
				int to = (int) Math.round((cols.size()-2)*Math.random());
				moveItem(cols, from, cols, to);
			}
		}
	}
	
	static void moveItem(List<Argument> from, int from_at, List<Argument> to, int to_at){
		Argument removed = from.remove(from_at);
		to.add(to_at, removed);
	}

	@Override
	public State cloneItself() {
		GLDState state = new GLDState();
		state.data = this.data.cloneItself();
		return state;
	}

	@Override
	public void printIt() {
		this.data.print();
	}
	
}
