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
public class Mesh {
	
	private Map<Object,MeshRow> map = new HashMap<Object,MeshRow>();
	
	public MeshCell transform(Object x, Object y){
		if (map.containsKey(y)){
			return map.get(y).transform(x);
		} else {
			MeshRow row = new MeshRow();
			map.put(y, row);
			return row.transform(x);
		}
	}
}
