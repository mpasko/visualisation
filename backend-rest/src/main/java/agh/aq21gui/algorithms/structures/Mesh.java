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
	
	private Map<K,IMesh<K,V>> map = new HashMap<K,IMesh<K,V>>();
	
	@Override
	public MeshCell<V> transform(K ...list){
		LinkedList<K> xses= new LinkedList<K>(Arrays.asList(list));
		K x = xses.removeFirst();
		IMesh<K, V> current;
		if (xses.size()==1){
			current = retrieveLast(x);
		} else {
			current = retrieveFirst(x);
		}
		return current.transform((K[])xses.toArray());
	}
	
	private IMesh<K,V> retrieveFirst(K elem){
		if (map.containsKey(elem)){
			return map.get(elem);
		} else {
			Mesh<K,V> row = new Mesh<K,V>();
			map.put(elem, row);
			return row;
		}
	}
	
		private IMesh<K,V> retrieveLast(K elem){
		if (map.containsKey(elem)){
			return map.get(elem);
		} else {
			MeshRow<K,V> row = new MeshRow<K,V>();
			map.put(elem, row);
			return row;
		}
	}
}
