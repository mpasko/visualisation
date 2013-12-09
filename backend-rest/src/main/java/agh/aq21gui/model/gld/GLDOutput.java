/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.algorithms.GLDState;
import agh.aq21gui.algorithms.State;
import agh.aq21gui.algorithms.structures.Mesh;
import agh.aq21gui.algorithms.structures.MeshCell;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class GLDOutput {

	private ArgumentsGroup rows;
	private ArgumentsGroup columns;
	private Evaluator elements;
	private Output hypo;
	public Mesh mesh;
	
	public GLDOutput(Output out) {
		this.hypo = out;
		rows = new ArgumentsGroup(new LinkedList<Argument>());
		columns = new ArgumentsGroup(new LinkedList<Argument>());
		elements = new ValueEvaluator(out/*,rows,columns*/);
		this.resetMesh();
	}
	
	@JsonProperty("rows")
	public void setRows(List<Argument> list){
		this.rows = new ArgumentsGroup(list);
	}
	
	public List<Argument> getRows(){
		return this.rows.getArguments();
	}
	
	@JsonProperty("columns")
	public void setColumns(List<Argument> list){
		this.columns = new ArgumentsGroup(list);
	}
	
	public List<Argument> getColumns(){
		return this.columns.getArguments();
	}
	
	/**
	 * 
	 * @return cols in full sequence
	 */
	@JsonIgnore
	public List<Coordinate> getHCoordSequence(){
		return this.columns.getCoordSequence();
	}
	
	/**
	 * 
	 * @return rows in full sequence
	 */
	@JsonIgnore
	public List<Coordinate> getVCoordSequence(){
		return this.rows.getCoordSequence();
	}

	public void print() {
		System.out.println("Columns: ");
		this.columns.print();
		System.out.println("Rows: ");
		this.rows.print();
	}

	public GLDOutput cloneItself() {
		GLDOutput out = new GLDOutput(hypo);
		out.columns = columns.cloneItself();
		out.rows = rows.cloneItself();
		out.elements = elements.cloneItself();
		return out;
	}

	@JsonProperty("values")
	public List<Cell> getElements() {
		this.resetMesh();
		Iterable<MeshCell<CellValue>> mesh_cells = this.getCells(this.getVCoordSequence(), this.getHCoordSequence());
		List<Cell> cells = new LinkedList<Cell>();
		for (MeshCell<CellValue> mesh_value: mesh_cells) {
			Cell c = new Cell(mesh_value.get());
			cells.add(c);
		}
		return cells;
	}

	public int width() {
		return columns.width();
	}

	public int height() {
		return rows.width();
	}

	/**
	 * Returns cell value
	 * @param row ==V
	 * @param col ==H
	 * @return cellvalue
	 */
	public CellValue eval(Coordinate row, Coordinate col) {
		return this.elements.eval(row,col);
	}
	
	static void printCellValues(GLDOutput gld_output) {
		List<Coordinate> v = gld_output.getVCoordSequence();
		List<Coordinate> h = gld_output.getHCoordSequence();
		System.out.print("Width:");
		System.out.println(h.size());
		System.out.print("Height:");
		System.out.println(v.size());

		System.out.println();
		for(Coordinate row : v){
			for(Coordinate col : h){
				CellValue value = gld_output.eval(row, col);
				System.out.print(value.toString());
			}
			System.out.println();
		}
	}

	public void sColAG(ArgumentsGroup argumentsGroupSample) {
		this.columns=argumentsGroupSample;
	}

	public void sRowAG(ArgumentsGroup argumentsGroupSample) {
		this.rows=argumentsGroupSample;
	}

	public Iterable<MeshCell<CellValue>> getCells(Iterable<Coordinate> rowSeq, Iterable<Coordinate> colSeq) {
		List<MeshCell<CellValue>> list = new LinkedList<MeshCell<CellValue>>();
		for (Coordinate row: rowSeq) {
			for (Coordinate col : colSeq) {
				MeshCell<CellValue> cell = mesh.transform(col, row);
				list.add(cell);
			}
		}
		return list;
	}

	public void resetMesh() {
		mesh = new Mesh<Coordinate,CellValue>();
		List<Coordinate> rowSeq=this.getVCoordSequence();
		List<Coordinate> colSeq=this.getHCoordSequence();
		for (Coordinate row : rowSeq) {
			for (Coordinate col : colSeq) {
				CellValue value = eval(row, col);
				MeshCell cell = mesh.transform(col, row);
				cell.set(value);
			}
		}
	}
}
