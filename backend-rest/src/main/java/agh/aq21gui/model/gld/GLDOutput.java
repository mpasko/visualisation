/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.algorithms.structures.Mesh;
import agh.aq21gui.algorithms.structures.MeshCell;
import agh.aq21gui.model.gld.presentation.ListPresenter;
import agh.aq21gui.model.gld.presentation.MapPresenter;
import agh.aq21gui.model.gld.presentation.Presenter;
import agh.aq21gui.model.gld.processing.CellValue;
import agh.aq21gui.model.gld.processing.Coordinate;
import agh.aq21gui.model.gld.processing.Evaluator;
import agh.aq21gui.model.gld.processing.ValueEvaluator;
import agh.aq21gui.model.output.Output;
import java.util.Iterator;
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
	public Mesh<Coordinate,CellValue> mesh;
	private Presenter presenter;
	
	public GLDOutput(Output out) {
		mesh = new Mesh<Coordinate,CellValue>();
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
	@JsonProperty("column_sequence")
	public List<Coordinate> getHCoordSequence(){
		return this.columns.getCoordSequence();
	}
	
	/**
	 * 
	 * @return rows in full sequence
	 */
	@JsonProperty("row_sequence")
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
	public Object getElements() {
		this.resetMesh();
		Iterable<MeshCell<CellValue>> mesh_cells = this.getMeshCellValues(this.getVCoordSequence(), this.getHCoordSequence());
		presenter = new MapPresenter();
		return this.presenter.getValues(extractCells(mesh_cells));
	}

	@JsonProperty("width")
	public int getWidth() {
		return columns.width();
	}

	@JsonProperty("height")
	public int getHeight() {
		return rows.width();
	}
	
	@JsonIgnore
	public void setEvaluator(Evaluator eval){
		this.elements = eval;
	}

	/**
	 * Returns cell value
	 * @param row ==V
	 * @param col ==H
	 * @return CellValue
	 */
	public CellValue eval(Coordinate row, Coordinate col) {
		return this.elements.eval(row,col);
	}
	
	public static void printCellValues(GLDOutput gld_output) {
		List<Coordinate> v = gld_output.getVCoordSequence();
		List<Coordinate> h = gld_output.getHCoordSequence();
		System.out.print("Width:");
		System.out.println(h.size());
		System.out.print("Height:");
		System.out.println(v.size());

		System.out.println();
		
		gld_output.resetMesh();
		Iterable<MeshCell<CellValue>> values = gld_output.getMeshCellValues(v, h);
		Iterator<MeshCell<CellValue>> iterator = values.iterator();
		for (int i=0; i<v.size(); ++i){
			for (int j=0; j<h.size(); ++j) {
				CellValue value = iterator.next().get();
				System.out.print(value.toString());
			}
			System.out.println(String.format(" ==[%s]", i));
		}
	}

	public void sColAG(ArgumentsGroup argumentsGroupSample) {
		this.columns=argumentsGroupSample;
	}

	public void sRowAG(ArgumentsGroup argumentsGroupSample) {
		this.rows=argumentsGroupSample;
	}

	/*
	 * @param rowSeq = V
	 * @param colSeq = H
	 */
	public Iterable<MeshCell<CellValue>> getMeshCellValues(Iterable<Coordinate> rowSeq, Iterable<Coordinate> colSeq) {
		List<MeshCell<CellValue>> list = new LinkedList<MeshCell<CellValue>>();
		for (Coordinate row: rowSeq) {
			for (Coordinate col : colSeq) {
				MeshCell<CellValue> cell = mesh.transform(col, row);
				list.add(cell);
			}
		}
		return list;
	}

	public final void resetMesh() {
		mesh.clear();
		List<Coordinate> rowSeq=this.getVCoordSequence();
		List<Coordinate> colSeq=this.getHCoordSequence();
		for (Coordinate row : rowSeq) {
			for (Coordinate col : colSeq) {
				CellValue value = eval(row, col);
				MeshCell<CellValue> cell = mesh.transform(col, row);
				cell.set(value);
			}
		}
	}

	/**
	 * Converts representation
	 * @param mesh_cells list of mesh cells
	 * @return list of CellValue
	 */
	public static Iterable<CellValue> extractCells(Iterable<MeshCell<CellValue>> mesh_cells) {
		List<CellValue> list = new LinkedList<CellValue>();
		for (MeshCell<CellValue> meshcell : mesh_cells){
			list.add(meshcell.get());
		}
		return list;
	}
}
