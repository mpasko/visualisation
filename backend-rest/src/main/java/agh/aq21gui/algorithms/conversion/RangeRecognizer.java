/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

import agh.aq21gui.model.gld.processing.Recognizer;
import agh.aq21gui.model.gld.Value;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.Util;

/**
 *
 * @author marcin
 */
public class RangeRecognizer implements Recognizer{
	public final RangeElement left;
	public final RangeElement right;

	RangeRecognizer(RangeElement left, RangeElement right) {
		if ((left == null) && (right == null)){
			throw new RuntimeException("RangeRecognizer: both left and right are null!");
		}
		this.left = left;
		this.right = right;
	}

	public RangeRecognizer(String value) {
		left = null;
		right = new NominalElement(value);
	}

	public String generateName() {
		StringBuilder text;
		text = new StringBuilder();
		String rightName="";
		String leftName="";
		if (right != null) {
			rightName = right.rightName();
		}
		if (left != null) {
			leftName = left.leftName();
		}
		text.append(leftName);
		if(!rightName.isEmpty()&&!leftName.isEmpty()){
			text.append("..");
		}
		text.append(rightName);
		return text.toString();
	}

	@Override
	public boolean accept(ClassDescriptor selector, Attribute attr, DomainsGroup dg) {
		String comparator=selector.comparator;
		Util.isNull(attr, "attr");
		String value=selector.getValue();
		String domain = attr.getdomainRecursive(dg);
		boolean matches = true;
		RangeElement from=null;
		RangeElement to=null;
		RangeElement elem = null;
		if (domain.equalsIgnoreCase("continuous")||domain.equalsIgnoreCase("integer")) {
			if(!selector.range_begin.isEmpty()){
				from = new ContinuousElement(selector.range_begin, ">=");
				to = new ContinuousElement(selector.range_end, "<=");
			} else if (!value.isEmpty()){
				elem = new ContinuousElement(value, comparator);
			}
		} else if (domain.equalsIgnoreCase("linear")) {
			if(!selector.range_begin.isEmpty()){
				from = new LinearElement(attr, selector.range_begin, ">=");
				to = new LinearElement(attr, selector.range_end, "<=");
			} else if (!value.isEmpty()){
				elem = new LinearElement(attr, value, comparator);
			}
		} else if (domain.equalsIgnoreCase("nominal")) {
			elem = new NominalElement(value);
		}
		if (!selector.getRange_begin().isEmpty()){
			matches &= matchesComparator(from, "<=", left);
			matches &= matchesComparator(to, ">=", right);
		}else {
			matches &= matchesComparator(left, comparator, elem);
			matches &= matchesComparator(right, comparator, elem);
		}
		return matches;
	}

	private boolean matchesComparator(RangeElement right, String comparator, RangeElement elem) {
		boolean matches = true;
		if ((right!=null) && (elem != null)){
			if (comparator.equals("=")){
				matches = matches && right.minus(elem)==0;
			}else if (comparator.equals(">")){
				matches = matches && right.minus(elem)>0;
			}else if (comparator.equals("<")){
				matches = matches && right.minus(elem)<0;
			}else if (comparator.equals(">=")){
				matches = matches && right.minus(elem)>=0;
			}else if (comparator.equals("<=")){
				matches = matches && right.minus(elem)<=0;
			}
		}
		return matches;
	}

	@Override
	public Recognizer getCounterRecognizer() {
		return new NegatedRecognizer(this);
	}
}
