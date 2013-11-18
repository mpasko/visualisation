/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

/**
 *
 * @author marcin
 */
class NominalElement extends RangeElement {
	private final String value;

	public NominalElement(String value) {
		this.value = value;
	}

	@Override
	public int minus(RangeElement next) {
		return this.value.compareTo(next.getValue());
	}

	@Override
	public String getValue() {
		return value;
	}
	
}
