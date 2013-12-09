/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.stubs;

import agh.aq21gui.model.gld.Value;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class Utils {

	public static boolean containsString(List<Value> list, String... strings) {
		for (String str : strings) {
			boolean found = false;
			for (Value item : list) {
				if (item.getName().contains(str)) {
					found = true;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	public static Hypothesis hypothesis(Selector... sel) {
		Hypothesis hypo = new Hypothesis();
		List<Rule> rules = new LinkedList<Rule>();
		Rule rule1 = new Rule();
		List<Selector> selectors = new LinkedList<Selector>(Arrays.asList(sel));
		rule1.setSelectors(selectors);
		rules.add(rule1);
		hypo.setRules(rules);
		return hypo;
	}

	public static Selector valueSelector(String name, String comparator, String value) {
		Selector selector2 = new Selector();
		selector2.name = name;
		selector2.comparator = comparator;
		selector2.setValue(value);
		return selector2;
	}

	public static Selector listSelector(String name, List<String> elems) {
		Selector selector1 = new Selector();
		selector1.name = name;
		selector1.comparator = "=";
		selector1.set_elements = elems;
		return selector1;
	}
	
}
