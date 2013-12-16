/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.structures;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class Mesh<K,V> implements IMesh<K, V> {
	
	private Map<K,IMesh<K,V>> map;
	
	public Mesh(){
		this.clear();
	}
	
	@Override
	public MeshCell<V> transform(K ...list){
		LinkedList<K> xses= new LinkedList<K>(Arrays.asList(list));
		K x;
		if (map.containsKey(xses.getLast())) {
			x = xses.removeLast();
		} else {
			x = xses.removeFirst();
		}
		IMesh<K, V> current;
		current = retrieve(x, xses.size());
		return current.transform((K[])xses.toArray());
	}
	
	private IMesh<K,V> retrieve(K elem, int remaining){
		if (map.containsKey(elem)){
			return map.get(elem);
		} else {
			IMesh<K,V> row;
			if (remaining == 1) {
				row = new MeshRow<K,V>();
			} else {
				row = new Mesh<K,V>();
			}
			map.put(elem, row);
			return row;
		}
	}

	public final void clear() {
		map = new HashMap<K,IMesh<K,V>>();
	}
}
