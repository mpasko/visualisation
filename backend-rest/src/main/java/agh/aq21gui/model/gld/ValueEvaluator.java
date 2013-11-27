/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
class ValueEvaluator implements Evaluator {
//	private final ArgumentsGroup rows;
//	private final ArgumentsGroup cols;
	private final List<Hypothesis> hypotheses;
	private List<CellValue> contrdomain;
	private final Output out;

	ValueEvaluator(Output out/*, ArgumentsGroup rows, ArgumentsGroup columns*/) {
		this.out = out;
		this.hypotheses = out.getOutputHypotheses();
//		this.rows = rows;
//		this.cols = columns;
		initContrDomain();
	}

	@Override
	public Evaluator cloneItself() {
		ValueEvaluator deg = new ValueEvaluator(out/*,rows,cols*/);
		return deg;
	}

	@Override
	public List<Element> getElements() {
		List<Element> list = new LinkedList<Element>();
		return list;
	}

	/*
	@Override
	public Value eval(Coordinate row, Coordinate col) {
		Value result=null;
		for (int index=0; index<row.getValues().size(); ++index) {
			Value v = row.getValues().get(index);
			Argument arg = rows.getArguments().get(index);
			if (!arg.getValues().contains(v)){
				throw new RuntimeException("Error! Parameters are out of order!");
			}
			Value selector = v.recognizer.returns();
			if (selector != null){
				result = selector;
			}
		}
		for (int index=0; index<col.getValues().size(); ++index) {
			Value v = row.getValues().get(index);
			Argument arg = rows.getArguments().get(index);
			if (!arg.getValues().contains(v)){
				throw new RuntimeException("Error! Parameters are out of order!");
			}
			Value selector = v.recognizer.returns();
			if (selector != null){
				result = selector;
			}
		}
		return result;
	}
	*/

	@Override
	public CellValue eval(Coordinate row, Coordinate col) {
		Coordinate coord = row.merge(col);
		CellValue result = new CellValue();
		for (Hypothesis hypo:this.hypotheses){
			if (ruleMatches(hypo.rules, coord)) {
				result.addAll(hypo.getClasses());
			}
		}
		/*
		
		for (Rule rule : hypotheses.rules){
			boolean matches=true;
			for (Selector sel : rule.getSelectors()){
				if(!selectorMatches(sel,coord)){
					matches=false;
				}
			}
			if (matches) {
				return retrieveValue(rule,coord);
			}
		}
		*/
		return result;
	}

	private boolean selectorMatches(Selector sel, Coordinate coord) {
		Value v = coord.get(sel.name);
		Attribute attr = out.findAttribute(sel.name);
		return v.recognizer.accept(sel,attr,out.gDG());
	}

	private CellValue retrieveValue(Rule rule, Coordinate coord) {
		for (ClassDescriptor desc : rule.getSelectors()) {
			if (matches(desc,coord)){
				return findContrdomainValue(desc);
			}
		}
		return contrdomain.get(0);
	}

	/**
	 * @return the contrdomain
	 *x/
	public List<Value> getContrdomain() {
		return contrdomain;
	}
	*/

	/**
	 * @param contrdomain the contrdomain to set
	 *x/
	public void setContrdomain(List<Value> contrdomain) {
		this.contrdomain = contrdomain;
	}
	*/

	private void initContrDomain() {
		this.contrdomain = new LinkedList<CellValue>();
		/*
		this.contrdomain.add(null);
		for (ClassDescriptor desc : hypotheses.getClasses()){
			CellValue exist = findContrdomainValue(desc);
			if (exist==contrdomain.get(0)) {
				final CellValue value = new CellValue(desc);
				this.contrdomain.add(value);
			}
		}
		*/
	}

	private CellValue findContrdomainValue(ClassDescriptor desc) {
		CellValue exist=contrdomain.get(0);
		for (CellValue v : contrdomain){
			if (v!=null) {
				if (v.matches(desc)){
					exist = v;
				}
			}
		}
		return exist;
	}

	private boolean matches(ClassDescriptor desc, Coordinate cord) {
		Value value = cord.get(desc.name);
		Attribute attr = out.findAttribute(desc.name);
		return value.recognizer.accept(desc, attr, out.gDG());
	}

	private boolean ruleMatches(List<Rule> rules, Coordinate coord) {
		for (Rule rule:rules){
			boolean selectorMatch = true;
			for (Selector sel : rule.getSelectors()){
				selectorMatch |= selectorMatches(sel, coord);
			}
			if (selectorMatch) {
				return true;
			}
		}
		return false;
	}
}
