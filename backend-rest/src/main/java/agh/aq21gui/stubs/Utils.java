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

	public static boolean valuesContainStrings(List<Value> list, String... strings) {
		LinkedList<String> newlist = new LinkedList<String>();
        for (Value item : list) {
            newlist.add(item.getName());
        }
		return containsAllStrings(newlist,strings);
	}
    
    public static boolean containsAllStrings(List<String> list, String... strings) {
		for (String str : strings) {
			boolean found = false;
			for (String item : list) {
				if (item.contains(str)) {
					found = true;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
}
