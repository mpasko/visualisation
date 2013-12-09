/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.model.input.Attribute;
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
	public List<Cell> getElements() {
		List<Cell> list = new LinkedList<Cell>();
		return list;
	}

	@Override
	public CellValue eval(Coordinate row, Coordinate col) {
		Coordinate coord = row.merge(col);
		CellValue result = new CellValue();
		if (coord.size()>0){
			for (Hypothesis hypo:this.hypotheses){
				if (ruleMatches(hypo.rules, coord)) {
					result.addAll(hypo.getClasses());
				}
			}
		}
		return result;
	}

	private void initContrDomain() {
		this.contrdomain = new LinkedList<CellValue>();
	}

	private boolean ruleMatches(List<Rule> rules, Coordinate coord) {
		for (Rule rule:rules){
			boolean selectorMatch = true;
			for (Selector sel : rule.getSelectors()){
				selectorMatch &= selectorMatches(sel, coord);
			}
			if (selectorMatch) {
				return true;
			}
		}
		return false;
	}

	private boolean selectorMatches(Selector sel, Coordinate coord) {
		Value v = coord.get(sel.name);
		Attribute attr = out.findAttribute(sel.name);
		return v.recognizer.accept(sel,attr,out.gDG());
	}
}
