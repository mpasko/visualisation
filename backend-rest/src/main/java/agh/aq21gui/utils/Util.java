/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import agh.aq21gui.model.gld.processing.CellValue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class Util {
	public static void isNull(Object obj, String name){
		if(obj==null){
			System.out.println(name+" is null!");
		}
	}

	public static List<String> strings(String... elem) {
		List<String> elems1 = new LinkedList<String>();
		elems1.addAll(Arrays.asList(elem));
		return elems1;
	}

	public static void collectionIntoArray(Iterable<CellValue> mesh_cells, List<CellValue> array) {
		Iterator<CellValue> iter = mesh_cells.iterator();
		while (iter.hasNext()) {
			CellValue val = iter.next();
			array.add(val);
		}
	}
}
