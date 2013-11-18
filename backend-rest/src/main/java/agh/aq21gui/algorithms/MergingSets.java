/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

import agh.aq21gui.model.gld.Coordinate;
import java.util.List;

/**
 *
 * @author marcin
 */
public interface MergingSets {
	public void newElement(Object obj);
	public void merge(Object a, Object b);
	public int count();

	public void addAll(List<? extends Object> colSeq);
}
