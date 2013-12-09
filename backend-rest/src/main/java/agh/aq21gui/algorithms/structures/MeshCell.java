/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.structures;

/**
 *
 * @author marcin
 */
public class MeshCell<V> {
	private V value;
	
	public V get(){
		return value;
	}
	
	public void set(V value){
		this.value=value;
	}
}
