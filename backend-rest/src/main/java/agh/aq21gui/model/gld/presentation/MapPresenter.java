/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld.presentation;

import agh.aq21gui.algorithms.structures.ArrayMergingSets;
import agh.aq21gui.algorithms.structures.MergingSets;
import agh.aq21gui.model.gld.processing.CellValue;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.utils.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marcin
 */
public class MapPresenter implements Presenter{
	
	private HashMap<ClassDescriptor, NumberList> map;
	
	public MapPresenter(){
	}
	
	public class NumberList {
		@JsonProperty("exists_in")
		public List<Integer> list;
		
		public NumberList(){
			list = new LinkedList<Integer>();
		}

		private void put(Integer value) {
			this.list.add(value);
		}
		
		@Override
		public String toString(){
			StringBuilder b = new StringBuilder("{");
			int cnt = 0;
			for (Integer i : list) {
				b.append(i);
				if (++cnt<list.size()){
					b.append(", ");
				}
			}
			b.append("}");
			return b.toString();
		}
	}

	@Override
	public Object getValues(Iterable<CellValue> mesh_cells) {
		map = new HashMap<ClassDescriptor, NumberList>();
		ArrayList<CellValue> array = new ArrayList<CellValue>();
		Util.collectionIntoArray(mesh_cells, array);
		for (int i=0; i<array.size(); ++i) {
			CellValue cell = array.get(i);
			for (ClassDescriptor desc : cell.getDescriptors()) {
				put(desc,i);
			}
		}
		return map;
	}
	
	private void put(ClassDescriptor claz, int value) {
		NumberList nums;
		if (map.keySet().contains(claz)){
			nums = map.get(claz);
		} else {
			nums = new NumberList();
			map.put(claz, nums);
		}
		nums.put(value);
	}
}
