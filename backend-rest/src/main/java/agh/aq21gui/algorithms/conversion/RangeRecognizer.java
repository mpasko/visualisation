/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

import agh.aq21gui.model.gld.Recognizer;
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

	/*
	@Override
	public boolean accept(Value v) {
		if (v.recognizer==null){
			if(left == null){
				return v.name.equals(right.getValue());
			}
		}
		if (this.equals(v.recognizer)){
			return true;
		} else {
			return false;
		}
	}
	*/

	public String generateName() {
		StringBuilder text;
		text = new StringBuilder();
		String rightName="";
		String leftName="";
		if (right == null) {
			final RangeElement left = this.left;
			leftName = left.leftName();
		}
		if (left == null) {
			final RangeElement right = this.right;
			rightName = right.rightName();
		}
		text.append(leftName);
		if(!rightName.isEmpty()&&!leftName.isEmpty()){
			text.append(",");
		}
		text.append(rightName);
		return text.toString();
	}

	@Override
	public boolean accept(ClassDescriptor selector, Attribute attr, DomainsGroup dg) {
		String name=selector.name;
		String comparator=selector.comparator;
		String value=selector.getValue();
		RangeElement elem = null;
		Util.isNull(attr, "attr");
		String domain = attr.getdomainRecursive(dg);
		if (domain.equalsIgnoreCase("nominal")) {
			elem = new NominalElement(value);
		} else if (domain.equalsIgnoreCase("continuous")||domain.equalsIgnoreCase("integer")) {
			elem = new ContinuousElement(value, comparator);
		} else if (domain.equalsIgnoreCase("linear")) {
			elem = new LinearElement(attr, value, comparator);
		}
		
		boolean matches = true;
		if (left!=null){
			if (comparator.equals("=")){
				matches = left.minus(elem)==0;
			}else if (comparator.equals(">")){
				matches = left.minus(elem)>0;
			}else if (comparator.equals("<")){
				matches = left.minus(elem)<0;
			}else if (comparator.equals(">=")){
				matches = left.minus(elem)>=0;
			}else if (comparator.equals("<=")){
				matches = left.minus(elem)<=0;
			}
		}
		if (right!=null){
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
}
