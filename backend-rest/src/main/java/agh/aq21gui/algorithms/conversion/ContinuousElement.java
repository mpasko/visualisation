/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

/**
 *
 * @author marcin
 */
public class ContinuousElement extends RangeElement{
	
	private Double value;

	ContinuousElement(String value, String comparator) {
		this.value = Double.parseDouble(value);
		this.setComparator(comparator);
	}

	@Override
	public int minus(RangeElement other) {
		ContinuousElement next = (ContinuousElement)other;
		int cmp = value.compareTo(next.value);
		if (cmp==0) {
			return classify(this)-classify(next);
		} else {
			return cmp;
		}
	}

	@Override
	public String getValue() {
		return value.toString();
	}
	
}
