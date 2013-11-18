/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.structures;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class MeshRow {
	private Map<Object,MeshCell> map = new HashMap<Object,MeshCell>();
	
	public MeshCell transform(Object x){
		if (map.containsKey(x)) {
			return map.get(x);
		} else {
			MeshCell cell = new MeshCell();
			map.put(x, cell);
			return cell;
		}
	}
}
