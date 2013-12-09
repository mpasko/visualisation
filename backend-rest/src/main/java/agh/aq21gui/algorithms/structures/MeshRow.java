/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.structures;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class MeshRow<K,V> implements IMesh<K, V> {
	private Map<K,MeshCell<V>> map = new HashMap<K,MeshCell<V>>();
	
	public MeshCell<V> transform(K x){
		if (map.containsKey(x)) {
			return map.get(x);
		} else {
			MeshCell cell = new MeshCell();
			map.put(x, cell);
			return cell;
		}
	}

	@Override
	public MeshCell<V> transform(K... list) {
		LinkedList<K> xses = new LinkedList<K>(Arrays.asList(list));
		if (xses.size()==0){
			throw new RuntimeException("Too little parameters for a mesh");
		}
		K x = xses.removeFirst();
		if (xses.size()!=0){
			throw new RuntimeException("Too many parameters for a mesh");
		}
		return transform(x);
	}
}
