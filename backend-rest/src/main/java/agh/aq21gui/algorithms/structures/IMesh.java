/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.structures;

/**
 *
 * @author marcin
 */
public interface IMesh<K,V> {
	MeshCell<V> transform(K ...list);
}
