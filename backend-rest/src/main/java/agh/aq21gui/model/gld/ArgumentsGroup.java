/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.algorithms.structures.StopIterator;
import agh.aq21gui.model.gld.processing.Coordinate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class ArgumentsGroup {

	private List<Argument> arguments = new LinkedList<Argument>();
	
	public ArgumentsGroup(List<Argument> list) {
		this.arguments = list;
	}
	
	public List<Argument> getArguments(){
		return arguments;
	}

	void print() {
		for(Argument arg : arguments){
			arg.print();
			System.out.print(", ");
		}
		System.out.println();
	}

	ArgumentsGroup cloneItself() {
		List<Argument> args = new LinkedList<Argument>();
		for (Argument arg : arguments){
			args.add(arg.cloneItself());
		}
		return new ArgumentsGroup(args);
	}

	int width() {
		int i = 1;
		for (Argument arg : arguments) {
			i = i * arg.width();
		}
		return i;
	}

	public static void incrementIterators(LinkedList<StopIterator> iterators){
		Iterator<StopIterator> rootIterator = iterators.descendingIterator();
		StopIterator pivot = rootIterator.next();
		boolean overflow = false;
		boolean move = !pivot.hasNext();
		while (move && !overflow){
			if (rootIterator.hasNext()){
				pivot.rollback();
				pivot = rootIterator.next();
			} else {
				overflow = true;
			}
			move = !pivot.hasNext();
		}
		pivot.shift();
//		Util.isNull(pivot.current, "pivot.current");
	}
	
	public List<Coordinate> getCoordSequence() {
		LinkedList<Coordinate> product = new LinkedList<Coordinate>();
		LinkedList<StopIterator> iterators = new LinkedList<StopIterator>();
		for (Argument arg : arguments) {
			iterators.add(new StopIterator(arg.name,arg.getValues()));
		}
		if (iterators.size()==0) {
			Coordinate def = new Coordinate();
			def.put("default", new Value("*"));
			product.add(def);
			return product;
		}
		while (iterators.getFirst().hasNext()||(iterators.getFirst().current!=null)) {
			Coordinate coord = new Coordinate();
			for (StopIterator iterator : iterators) {
				coord.put(iterator.getName(),iterator.current());
			}
			product.add(coord);
			incrementIterators(iterators);
		}
		return product;
	}
}
