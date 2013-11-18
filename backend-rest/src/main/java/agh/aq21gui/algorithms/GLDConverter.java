/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

import agh.aq21gui.algorithms.conversion.RangeList;
import agh.aq21gui.algorithms.conversion.RangeRecognizer;
import agh.aq21gui.model.gld.Argument;
import agh.aq21gui.model.gld.GLDInput;
import agh.aq21gui.model.gld.GLDOutput;
import agh.aq21gui.model.gld.Recognizer;
import agh.aq21gui.model.gld.Value;
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
public class GLDConverter {
	private GLDOutput out;
	private List<Argument> rows;
	private List<Argument> cols;
	private List<Attribute> attrs;
	private int init_part;
	private List<Selector> selectors;
	private final Output data;

	public GLDConverter(GLDInput input) {
		cols = new LinkedList<Argument>();
		rows = new LinkedList<Argument>();
		attrs = input.getData().getAttributes();
		init_part = attrs.size() / 2;
		data = input.getData();
		out = new GLDOutput(data);
	}

	public GLDOutput convert() {
		selectors = extractSelectors();
		for (int i = 0; i < attrs.size(); ++i) {
			Argument arg = new Argument();
			agh.aq21gui.model.input.Attribute attr = attrs.get(i);
			arg.name = attr.getname();
			
			List<Value> values = new LinkedList<Value>();
			final String domain = attr.getdomain();
			RangeList range = new RangeList(attr);
			for (Selector selector : selectors) {		
				if (selector.name.equals(arg.name)) {
					if (domain.equalsIgnoreCase("nominal")) {
						if (attr.getRange().size()>0){
							List<String> list = attr.getRange();
							for (String element:list){
								values.add(new Value(element));
							}
						} else {
							if (selector.comparator.equals("=")) {
								values.add(new Value(selector.getValue()));
							} else {
								throw new RuntimeException("Unexpected comparator:"+selector.comparator);
							}
						}
					} else {
						if (selector.range_begin.isEmpty()) {
							range.addAny(selector.getValue(), selector.comparator);
						} else {
							range.addAny(selector.range_begin, ">=");
							range.addAny(selector.range_end, "<=");
						}
					} 
				}
			}
			if (!domain.equalsIgnoreCase("nominal")) {
				List<RangeRecognizer> names = range.genereteRecognizers();
				for (RangeRecognizer name : names) {
					final Value value = new Value(name.generateName());
					value.recognizer = name;
					values.add(value);
				}
			}
			arg.setValues(values);
			if (i >= init_part) {
				cols.add(arg);
			} else {
				rows.add(arg);
			}
		}
		out.setRows(rows);
		out.setColumns(cols);
		return out;
	}
	
	public List<Selector> extractSelectors(){
		List<Selector> selctrs = new LinkedList<Selector>();
		for (Hypothesis hypothesis : data.getOutputHypotheses()) {
			for (Rule rule : hypothesis.getRules()) {
				selctrs.addAll(rule.getSelectors());
			}
		}
		return selctrs;
	}
	
}
