/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

/**
 *
 * @author marcin
 */
public abstract class RangeElement {

	protected boolean leftOpened;
	protected boolean singleNumber;
	
	public static int classify(RangeElement elem) {
		if (elem.isSingleNumber()) {
			return 2;
		} else if (elem.isLeftOpened()) {
			return 1;
		} else {
			return 3;
		}
	}
	public abstract int minus(RangeElement next);

	public boolean isLeftOpened() {
		return leftOpened;
	}

	public boolean isSingleNumber() {
		return singleNumber;
	}

	public void setComparator(String comparator) {
		if (comparator.equals("=")) {
			this.singleNumber = true;
		} else {
			this.singleNumber = false;
			if (comparator.equals(">") || comparator.equals("<=")) {
				this.leftOpened = false;
			} else if (comparator.equals("<") || comparator.equals(">=")) {
				this.leftOpened = true;
			} else {
				throw new RuntimeException("Unexpected comparator value (other than: {=,<,>,<=,>=})");
			}
		}
	}
	
	public abstract String getValue();

	public String leftName() {
		StringBuilder b = new StringBuilder();
		if (this.singleNumber) {
			b.append("<>");
		} else if (this.leftOpened) {
			b.append(">=");
		} else {
			b.append(">");
		}
		b.append(this.getValue());
		return b.toString();
	}
	
	public String rightName() {
		StringBuilder b = new StringBuilder();
		if (this.singleNumber) {
			b.append("=");
		} else if (this.leftOpened) {
			b.append("<");
		} else {
			b.append("<=");
		}
		b.append(this.getValue());
		return b.toString();
	}
}
