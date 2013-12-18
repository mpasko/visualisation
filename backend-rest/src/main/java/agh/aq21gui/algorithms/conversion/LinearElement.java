/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

import agh.aq21gui.model.input.Attribute;
import java.util.List;

/**
 *
 * @author marcin
 */
public class LinearElement extends RangeElement{
	private final Attribute attr;
	private String value;
	
	public LinearElement(Attribute attr){
		this.attr = attr;
	}

	LinearElement(Attribute attr, String value, String comparator) {
		this.attr = attr;
		this.value = value;
		this.setComparator(comparator);
	}

	@Override
	public int minus(RangeElement other) {
		LinearElement next = (LinearElement)other;
		if ((this.getAttrIndex()==-1) || (next.getAttrIndex()==-1)){
			boolean matches = true;
			int tmp = classify(this)-classify(next);
			if (tmp == 0) {
				tmp = this.value.compareToIgnoreCase(next.value);
			}
			return tmp;
		}
		if (next.getAttrIndex()==this.getAttrIndex()){
			return classify(this)-classify(next);
		} else {
			return this.getAttrIndex()-next.getAttrIndex();
		}
	}
	
	private int getAttrIndex(){
		List<String> range = this.attr.getRange();
		int index=-1;
		int count=0;
		for (String str : range) {
			if (str.equalsIgnoreCase(value)){
				index=count;
			}
			count++;
		}
		return index;
	}

	@Override
	public String getValue() {
		return value;
	}
}
