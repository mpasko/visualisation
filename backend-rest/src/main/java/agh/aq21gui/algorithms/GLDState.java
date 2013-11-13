/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

import agh.aq21gui.model.gld.Argument;
import agh.aq21gui.model.gld.GLDOutput;
import java.util.List;

/**
 *
 * @author marcin
 */
public class GLDState extends State{

	public static GLDState build(GLDOutput initialData) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	private double p=1.0;
	private GLDOutput data;
	public static final double REPARTITION_PROB=0.05;

	@Override
	public double targetFunction() {
		return this.getClusters() + p*this.getGoldenRatioCloseness();
	}
	
	public void setP(int newp){
		this.p = newp;
	}
	
	private double getClusters(){
		return 1;
	}
	
	private double getGoldenRatioCloseness(){
		return 1;
	}

	GLDOutput getData() {
		return this.data;
	}

	@Override
	public void modifyItself() {
		List<Argument> rows = data.getRows();
		List<Argument> cols = data.getColumns();
		if (Math.random()>REPARTITION_PROB) {
			if ((rows.size()>1) && (Math.random()>0.5)){
				int selected = (int) Math.round((rows.size()-1)*Math.random());
				moveItem(rows, selected, cols, 0);
			} else if (cols.size()>1) {
				int selected = (int) Math.round((cols.size()-1)*Math.random());
				moveItem(cols, selected, rows, 0);
			}
		} else {
			if ( (rows.size()>=2) && (Math.random()>0.5) ) {
				int position = (int) Math.round((rows.size()-2)*Math.random());
			} else {
				
			}
		}
	}
	
	private static void moveItem(List<Argument> from, int from_at, List<Argument> to, int to_at){
		Argument removed = from.remove(from_at);
		to.add(to_at, removed);
	}

	@Override
	public State cloneItself() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void printIt() {
		this.data.print();
	}
	
}
