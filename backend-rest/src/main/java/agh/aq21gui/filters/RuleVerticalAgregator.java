/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.NumericUtil;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author marcin
 */
public class RuleVerticalAgregator {

    public static boolean areExclusive(String comp1, String comp2) {
        boolean exclusive = comp1.equals("!=") && comp2.equals("=");
        exclusive |= comp1.equals("=") && comp2.equals("!=");
        exclusive |= comp1.equals("<=") && comp2.equals(">");
        exclusive |= comp1.equals("<") && comp2.equals(">=");
        exclusive |= comp1.equals(">=") && comp2.equals("<");
        exclusive |= comp1.equals(">") && comp2.equals("<=");
        exclusive |= comp1.equals(">=") && comp2.equals("!=");
        exclusive |= comp1.equals("<=") && comp2.equals("!=");
        exclusive |= comp1.equals("!=") && comp2.equals("<=");
        exclusive |= comp1.equals("!=") && comp2.equals(">=");
        exclusive |= comp1.equals("<=") && comp2.equals(">=");
        exclusive |= comp1.equals(">=") && comp2.equals("<=");
        return exclusive;
    }

    public static boolean areBothSharp(String comp1, String comp2) {
        boolean both_sharp = (comp1.equals("<") || comp1.equals("!=")) && (comp2.equals(">") || comp2.equals("!="));
        both_sharp |= (comp1.equals(">") || comp1.equals("!=")) && (comp2.equals("<") || comp2.equals("!="));
        return both_sharp;
    }

    public static boolean isSameDirOrEnclosure(char dir1, char dir2, String comp2, String comp1) {
        boolean same_dir_or_enclosure = (dir1 == dir2);
        same_dir_or_enclosure |= dir1 == '<' && comp2.equals("=");
        same_dir_or_enclosure |= dir1 == '>' && comp2.equals("=");
        same_dir_or_enclosure |= dir2 == '<' && comp1.equals("=");
        same_dir_or_enclosure |= dir2 == '>' && comp1.equals("=");
        return same_dir_or_enclosure;
    }

    public Output agregate(Output result) {
        Output copy = result;//Util.deepCopyOutput(result);
        List<Hypothesis> outputHypotheses = copy.getOutputHypotheses();
        for (Hypothesis hypo : outputHypotheses) {
            List<Rule> rules = new LinkedList<Rule>(hypo.getRules());
            agregateRules(rules, result);
            hypo.setRules(rules);
        }
        return copy;
    }

    public void agregateRules(List<Rule> rules, Input in) {
        boolean retry = true;
        boolean all_nonremoving_found = false;
        while (retry) {
            retry = false;
            if (rules.size() >= 2) {
                Rule rule1 = null;
                Rule rule2 = null;
                Rule new_rule = null;
                for (int i = 1; (i < rules.size()) && (new_rule == null); ++i) {
                    rule1 = rules.get(i);
                    for (int j = 0; (j < i) && (new_rule == null); ++j) {
                        rule2 = rules.get(j);
                        Map<String, LinkedList<Selector>> map = new TreeMap<String, LinkedList<Selector>>();
                        mapAllFromRule(rule1, map);
                        mapAllFromRule(rule2, map);
                        LinkedList<Selector> different_selectors = new LinkedList<Selector>();
                        int difference_count = findDifference(map, different_selectors);
                        new_rule = analiseAllPossibilities(difference_count, rule1, different_selectors, in);
                        if ((isShorter(new_rule, rule1) || isShorter(new_rule, rule2)) && !all_nonremoving_found) {
                            new_rule = null;
                        }
                    }
                }
                if (new_rule != null) {
                    rules.remove(rule1);
                    rules.remove(rule2);
                    rules.add(new_rule);
                    retry = true;
                }
                if ((!retry) && !all_nonremoving_found) {
                    retry = true;
                    all_nonremoving_found = true;
                }
            }
        }
    }

    public void mapAllFromRule(Rule rule1, Map<String, LinkedList<Selector>> map) {
        for (Selector sel : rule1.getSelectors()) {
            String name = sel.getName();
            if (map.get(name) == null) {
                map.put(name, new LinkedList<Selector>());
            }
            map.get(name).add(sel);
        }
    }

    public boolean equals(Selector sel1, Selector sel2) {
        String comp1 = sel1.getComparator();
        String comp2 = sel2.getComparator();
        String val1 = sel1.getValue();
        String val2 = sel2.getValue();
        boolean are_same = val1.equalsIgnoreCase(val2) && comp1.equalsIgnoreCase(comp2);
        return are_same;
    }

    public int findDifference(Map<String, LinkedList<Selector>> map, LinkedList<Selector> different_selectors) {
        int difference_count = 0;
        for (LinkedList<Selector> sel_list : map.values()) {
            boolean found_difference;
            if (sel_list.size() == 1) {
                found_difference = true;
            } else {
                if (equals(sel_list.get(0), sel_list.get(1))) {
                    found_difference = false;
                } else {
                    found_difference = true;
                }
            }
            if (found_difference) {
                ++difference_count;
                different_selectors.addAll(sel_list);
            }
        }
        return difference_count;
    }

    public static Rule analiseAllPossibilities(int difference_count, Rule rule1, LinkedList<Selector> different_selectors, Input input) {
        Rule new_rule = null;
        if (difference_count == 0) {
            new_rule = rule1;
        } else if (difference_count == 1) {
            String name = different_selectors.get(0).getName();
            Rule candidate = makeRuleWithoutSelector(rule1, name);
            if (different_selectors.size() == 1) {
                new_rule = candidate;
            } else {
                Selector sel1 = different_selectors.get(0);
                List<String> sel1list = input.findDomainObjectRecursively(sel1.getName()).set_elements;
                Selector sel2 = different_selectors.get(1);
                List<String> sel2list = input.findDomainObjectRecursively(sel2.getName()).set_elements;
                if (sel1.isGeneralizationOf(sel2, sel2list)) {
                    new_rule = candidate;
                    new_rule.addSelector(sel1);
                } else if (sel2.isGeneralizationOf(sel1, sel1list)) {
                    new_rule = candidate;
                    new_rule.addSelector(sel2);
                } else {
                    String comp1 = sel1.getComparator();
                    String comp2 = sel2.getComparator();
                    String val1 = sel1.getValue();
                    String val2 = sel2.getValue();
                    char dir1 = comp1.charAt(0);
                    char dir2 = comp2.charAt(0);
                    Double doubleval1 = NumericUtil.tryParse(val1);
                    Double doubleval2 = NumericUtil.tryParse(val2);
                    boolean are_not_range = (sel1.getRange_begin().isEmpty()) && (sel2.getRange_begin().isEmpty());
                    Selector newsel = null;
                    if (are_not_range) {
                        if (val1.equals(val2)) {
                            boolean both_sharp = areBothSharp(comp1, comp2);
                            boolean exclusive = areExclusive(comp1, comp2);
                            boolean same_dir_or_enclosure = isSameDirOrEnclosure(dir1, dir2, comp2, comp1);
                            if (both_sharp) {
                                newsel = new Selector(name, "!=", val1);
                            } else if (exclusive) {
                                new_rule = candidate;
                            } else if (same_dir_or_enclosure) {
                                String newcomp = "<";
                                if (dir1 == '>' || dir2 == '>') {
                                    newcomp = ">";
                                }
                                if (comp1.contains("=") || comp2.contains("=")) {
                                    newcomp += "=";
                                }
                                newsel = new Selector(name, newcomp, val1);
                            }
                        } else {
                            if ((dir1 != '<' && dir1 != '>') || (dir2 != '<' && dir2 != '>')) {
                                new_rule = null;
                            } else if (dir1 != dir2) {
                                if ((dir1 == '<') && (doubleval1 >= doubleval2)) {
                                    new_rule = candidate;
                                }
                                if ((dir1 == '>') && (doubleval1 <= doubleval2)) {
                                    new_rule = candidate;
                                }
                            } else { //dir1==dir2
                                if (dir1 == '<') {
                                    if (doubleval1 > doubleval2) {
                                        newsel = new Selector(name, comp1, val1);
                                    } else {
                                        newsel = new Selector(name, comp2, val2);
                                    }
                                } else { //dir1=='>'
                                    if (doubleval1 < doubleval2) {
                                        newsel = new Selector(name, comp1, val1);
                                    } else {
                                        newsel = new Selector(name, comp2, val2);
                                    }
                                }
                            }
                        }
                    } else { //ranges
                        boolean first_is_range = !sel1.getRange_begin().isEmpty();
                        boolean second_is_range = !sel2.getRange_begin().isEmpty();
                        boolean both_range = first_is_range && second_is_range;
                        if (both_range) {
                            double a = NumericUtil.tryParse(sel1.getRange_begin());
                            double b = NumericUtil.tryParse(sel2.getRange_begin());
                            double c = NumericUtil.tryParse(sel1.getRange_end());
                            double d = NumericUtil.tryParse(sel2.getRange_end());
                            if (((a <= b) && (b <= c)) || ((b <= c) && (c <= d))) {
                                double newbegin;
                                double newend;
                                if (comp1.equals("=")) {
                                    newbegin = Math.min(a, b);
                                    newend = Math.max(c, d);
                                } else {
                                    newbegin = Math.max(a, b);
                                    newend = Math.min(c, d);
                                }
                                newsel = new Selector(name, comp1, "");
                                newsel.setRange_begin(Double.valueOf(newbegin).toString());
                                newsel.setRange_end(Double.valueOf(newend).toString());
                            }
                        } else {
                            if (first_is_range) {
                                double a = NumericUtil.tryParse(sel1.getRange_begin());
                                double b = doubleval2;
                                double c = NumericUtil.tryParse(sel1.getRange_end());
                                if ((a <= b) && (b <= c)) {
                                    double newvalue;
                                    if (dir2=='<') {
                                        newvalue = c;
                                        newsel = new Selector(name, dir2+"=", Double.valueOf(newvalue).toString());
                                    } else if (dir2=='>') {
                                        newvalue = a;
                                        newsel = new Selector(name, dir2+"=", Double.valueOf(newvalue).toString());
                                    } else if (comp2.equals("=")) {
                                        newsel = sel1;
                                    } else {
                                        new_rule = candidate;
                                    }
                                }
                            } else if (second_is_range) {
                                double a = NumericUtil.tryParse(sel2.getRange_begin());
                                double b = doubleval1;
                                double c = NumericUtil.tryParse(sel2.getRange_end());
                                if ((a <= b) && (b <= c)) {
                                    double newvalue;
                                    if (dir1=='<') {
                                        newvalue = c;
                                        newsel = new Selector(name, dir1+"=", Double.valueOf(newvalue).toString());
                                    } else if (dir1=='>') {
                                        newvalue = a;
                                        newsel = new Selector(name, dir1+"=", Double.valueOf(newvalue).toString());
                                    } else if (comp1.equals("=")) {
                                        newsel = sel1;
                                    } else {
                                        new_rule = candidate;
                                    }
                                }
                            }
                        }
                    }
                    if (newsel != null) {
                         new_rule = candidate;
                         new_rule.addSelector(newsel);
                    }
                }
            }
        }
        return new_rule;
    }

    public static Rule makeRuleWithoutSelector(Rule rule1, String name) {
        Rule new_rule = new Rule();
        for (Selector sel : rule1.getSelectors()) {
            if (!sel.getName().equalsIgnoreCase(name)) {
                new_rule.addSelector(sel);
            }
        }
        return new_rule;
    }

    public boolean isShorter(Rule new_rule, Rule rule1) {
        boolean returned;
        if (new_rule == null) {
            returned = false;
        } else {
            returned = new_rule.getSelectors().size() < rule1.getSelectors().size();
        }
        return returned;
    }
}
