/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

import agh.aq21gui.model.gld.Recognizer;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.DomainsGroup;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class RangeList {
	private LinkedList<RangeElement> list;
	private Attribute attr;
	private final DomainsGroup dg;
	
	public RangeList(Attribute attr, DomainsGroup dg){
		this.attr = attr;
		this.dg = dg;
		this.list = new LinkedList<RangeElement>();
	}
	
	public void addContinuous(String value, String comparator){
		RangeElement elem = new ContinuousElement(value, comparator);
		addElement(elem);
	}
	
	public void addLinear(String value, String comparator){
		RangeElement elem = new LinearElement(attr, value, comparator);
		addElement(elem);
	}
	
	private void addElement(RangeElement elem){
		if ((list.size()==0)||(elem.minus(list.get(0))<0)) {
			list.add(0,elem);
			return;
		}
		if (elem.minus(list.get(list.size()-1))>0 ) {
			list.addLast(elem);
			return;
		}
		int index = 0;
		RangeElement current = list.get(index);
		int comparison = elem.minus(current);
		if (comparison==0) {
			return;
		}
		while((index<list.size())&&(comparison>0)){
			index++;
			if (index >= list.size()){
				return;
			}
			current = list.get(index);
			comparison = elem.minus(current);
			if (comparison==0) {
				return;
			}
		}
		list.add(index, elem);
	}
	
	public List<String> genereteNames(){
		LinkedList<String> names = new LinkedList<String>();
		List<RangeRecognizer> recogns = this.genereteRecognizers();
		for (RangeRecognizer reco : recogns) {
			names.add(reco.generateName());
		}
		return names;
	}

	List<RangeElement> getElements() {
		return this.list;
	}

	public List<RangeRecognizer> genereteRecognizers() {
		List<RangeRecognizer> recogns = new LinkedList<RangeRecognizer>();
		if(list.size()>0){
			for (int i=0; i<=list.size(); ++i) {
				final RangeElement right;
				final RangeElement left;
				if (i<list.size()) {
					right = list.get(i);
				} else {
					right = null;
				}
				if (i>0) {
					left = list.get(i-1);
				} else {
					left = null;
				}
				RangeRecognizer reco = new RangeRecognizer(left,right);
				recogns.add(reco);
			}
		}
		return recogns;
	}

	public void addAny(String value, String comparator) {
		if (this.attr!=null && this.attr.getdomainRecursive(dg).equalsIgnoreCase("linear")){
			this.addLinear(value, comparator);
		} else {
			this.addContinuous(value, comparator);
		}
	}

	public void addSet(List<String> set_elements, String string) {
		RangeElement elem = new SetElement(set_elements);
		addElement(elem);
	}
}
