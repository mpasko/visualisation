/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.structures;

import agh.aq21gui.model.gld.Value;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author marcin
 */
public class StopIterator {
	private Iterator<Value> iterator;
	public boolean hasNext;
	public Value current;
	private final Collection<Value> list;
	private final String name;

	public StopIterator(String name, List<Value> values) {
		this.list = values;
		rollback();
		this.name=name;
	}

	public void shift() {
		if (iterator.hasNext()) {
			current = iterator.next();
			hasNext = iterator.hasNext();
		} else {
			this.current = null;
			this.hasNext = false;
		}
	}

	public boolean hasNext() {
		return hasNext;
	}

	public Value current() {
		return current;
	}

	public final void rollback() {
		iterator = list.iterator();
		current = iterator.next();
		hasNext = iterator.hasNext();
	}
	
	public String getName(){
		return name;
	}
}
