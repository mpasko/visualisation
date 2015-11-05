/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

import agh.aq21gui.model.output.Selector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sun.misc.Compare;
import sun.misc.Sort;

/**
 *
 * @author marcin
 */
class SetElement extends RangeElement {
	List<String> elems;

	public SetElement(List<String> set_elements) {
		String [] array = new ArrayList<String>(set_elements).toArray(new String[set_elements.size()]);
		Sort.quicksort(array, new Compare() {

			@Override
			public int doCompare(Object o, Object o1) {
				String a = (String)o;
				String b = (String)o1;
				return a.compareToIgnoreCase(b);
			}
		});
		elems = Arrays.asList(array);
	}

	@Override
	public int minus(RangeElement next) {
		if (next instanceof SetElement){
			SetElement other = (SetElement)next;
			int res = this.elems.size()-other.elems.size();
			if(res==0){
				return compareLists(this.elems,other.elems);
			} else {
				return res;
			}
		} else {
			if (this.elems.size()>=2){
				return 1;
			} else if (this.elems.size()==1) {
				String a = this.elems.get(0);
				String b = next.getValue();
				return a.compareToIgnoreCase(b);
			} else {
				return -1;
			}
		}
	}

	@Override
	public String getValue() {
		Selector s = new Selector();
		s.setSet_elements(elems);
		return s.getValue();
	}

	private int compareLists(List<String> elems, List<String> elems0) {
		for (int i = 0; i<elems.size(); ++i){
			String a = elems.get(i);
			String b = elems0.get(i);
			int res = a.compareToIgnoreCase(b);
			if (res != 0){
				return res;
			}
		}
		return 0;
	}
	
}
