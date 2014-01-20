/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

import agh.aq21gui.algorithms.structures.ArrayMergingSets;
import agh.aq21gui.algorithms.structures.MergingSets;
import agh.aq21gui.algorithms.structures.Mesh;
import agh.aq21gui.algorithms.structures.MeshCell;
import agh.aq21gui.model.gld.Argument;
import agh.aq21gui.model.gld.processing.CellValue;
import agh.aq21gui.model.gld.processing.Coordinate;
import agh.aq21gui.model.gld.GLDOutput;
import agh.aq21gui.model.gld.GLDProperties;
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
		state.properties = initialData.getProps();
		return state;
	}
	
	//private double p=1.0;
	private GLDOutput data;
	private int clusterscache=-1;
	
	private GLDProperties properties=null;

	@Override
	public double targetFunction() {
		return this.getClusters()/getMaxClusters() + properties.p*this.getGoldenRatioCloseness();
	}
	
	public void setRatioImportance(Double newp){
		if (properties==null){
			properties = new GLDProperties();
		}
		this.properties.p = newp;
	}
	
	public void setRepartitionProb(double prob) {
		if (properties==null){
			properties = new GLDProperties();
		}
		properties.repartition_prob = prob;
	}
    
    
	
	public double getClusters(){
		if (clusterscache != -1){
			return clusterscache;
		}
		int maxClusters = getMaxClusters();
		MergingSets sets = new ArrayMergingSets(maxClusters);
		MeshCell<CellValue> prevCell = null;
		List<Coordinate> colSeq = data.getHCoordSequence();
		List<Coordinate> rowSeq = data.getVCoordSequence();
		data.resetMesh();
		for (MeshCell<CellValue> cell:data.getMeshCellValues(rowSeq,colSeq)){
			sets.newElement(cell);
		}
		//left to right
		for (Coordinate row: rowSeq) {
			prevCell = null;
			for (Coordinate col : colSeq) {
				prevCell = mergeCell(col, row, prevCell, sets);
			}
		}
		//top to bottom
		for (Coordinate col : colSeq) {
			prevCell = null;
			for (Coordinate row: rowSeq) {
				prevCell = mergeCell(col, row, prevCell, sets);
			}
		}
		clusterscache = sets.count();
		return clusterscache;
	}

    public int getMaxClusters() {
        return data.getHeight()*data.getWidth();
    }

	public MeshCell mergeCell(Coordinate col, Coordinate row, MeshCell<CellValue> prevCell, MergingSets sets) {
		MeshCell<CellValue> cell = data.mesh.transform(col, row);
		CellValue value = (CellValue)cell.get();
		if ((prevCell!=null)) {
			CellValue prevValue = (CellValue)prevCell.get();
			if ((value==null && prevValue==null)||value.compare(prevValue)) {
				sets.merge(cell, prevCell);
			}
		}
		return cell;
	}
	
	public double getGoldenRatioCloseness(){
		double width = data.getWidth();
		double height = data.getHeight();
		return Math.abs(width/height - 377.0/233.0);
	}

	GLDOutput getData() {
		return this.data;
	}

	@Override
	public void modifyItself() {
		clusterscache = -1;
		List<Argument> rows = data.getRows();
		List<Argument> cols = data.getColumns();
		if (Math.random()<properties.repartition_prob) {
			if ((rows.size()>1) && randomBool()){
				int selected = randomElem(rows.size());
				moveItem(rows, selected, cols, 0);
			} else if (cols.size()>1) {
				int selected = randomElem(cols.size());
				moveItem(cols, selected, rows, 0);
			}
		} else if (Math.random()<properties.swap_prob) {
			if ((rows.size()>1) && randomBool()){
				swapValues(rows);
			} else if (cols.size()>1) {
				swapValues(cols);
			}
		} else {
			if ( (rows.size()>=2) && randomBool() ) {
				int from = randomElem(rows.size());
				int to = randomElem(rows.size()-1);
				moveItem(rows, from, rows, to);
			} else if (cols.size()>=2) {
				int from = randomElem(cols.size());
				int to = randomElem(cols.size()-1);
				moveItem(cols, from, cols, to);
			}
		}
	}
	
	static boolean randomBool(){
		return (Math.random()>0.5);
	}
	
	static int randomElem(int size){
		return (int) Math.round((size-1)*Math.random());
	}
	
	static <T> void moveItem(List<T> from, int from_at, List<T> to, int to_at){
		T removed = from.remove(from_at);
		to.add(to_at, removed);
	}

	@Override
	public State cloneItself() {
		GLDState state = new GLDState();
		state.data = this.data.cloneItself();
		state.properties = this.properties;
		return state;
	}

	@Override
	public void printIt() {
		this.data.print();
		System.out.printf("Clusters:%f, GoldenRatio closeness:%f\n", this.getClusters(),this.getGoldenRatioCloseness());
	}

	public void swapValues(List<Argument> rows) {
		int selected = randomElem(rows.size());
		Argument arg = rows.get(selected);
		List<Value> vals = arg.getValues();
		if (vals.size()>=2){
			int from = randomElem(vals.size());
			int to = randomElem(vals.size()-1);
			moveItem(vals,from,vals,to);
			arg.setValues(vals);
		}
	}
}
