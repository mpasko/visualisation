/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.structures;

import agh.aq21gui.algorithms.structures.MergingSets;
import agh.aq21gui.model.gld.processing.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author marcin
 */
public class ArrayMergingSets implements MergingSets {
	
	ArrayList <Integer> array;
	int generator;
	HashMap<Object,Integer> map;
	private int maxClusters;

	public ArrayMergingSets(int maxClusters) {
		this.maxClusters=maxClusters;
		array = new ArrayList <Integer>(maxClusters);
		map = new HashMap<Object, Integer>(maxClusters);
		for (int i=0; i<maxClusters; ++i){
			array.add(i, i);
		}
		generator = 0;
	}

	@Override
	public void newElement(Object obj) {
		map.put(obj, generator);
		generator++;
	}

	@Override
	public void merge(Object a, Object b) {
		int anum = map.get(a);
		int bnum = map.get(b);
		anum = getRoot(anum);
		bnum = getRoot(bnum);
		if (anum<bnum) {
			array.set(bnum, anum);
		} else {
			array.set(anum, bnum);
		}
	}

	@Override
	public int count() {
		fold();
		HashSet<Integer> kinda = new HashSet<Integer>();
		for (int i = 0; i<maxClusters; ++i){
			int root = array.get(i);
			if (!kinda.contains(root)){
				kinda.add(root);
			}
		}
		return kinda.size();
	}

	@Override
	public void addAll(List<? extends Object> colSeq) {
		for (Object o : colSeq) {
			newElement(o);
		}
	}

	private int getRoot(int anum) {
		int tmp = anum;
		while (array.get(tmp)<tmp) {
			tmp = array.get(tmp);
		}
		return tmp;
	}

	private void fold() {
		for (int i = 0; i<maxClusters; ++i){
			int tmp = getRoot(i);
			array.set(i, tmp);
		}
	}
	
}
