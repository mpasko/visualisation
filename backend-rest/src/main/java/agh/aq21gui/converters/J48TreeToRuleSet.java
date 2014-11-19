/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.converters;

import agh.aq21gui.j48treegrammar.Branch;
import agh.aq21gui.j48treegrammar.J48Tree;
import agh.aq21gui.j48treegrammar.Node;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.OutputHypotheses;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.Util;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author marcin
 */
public class J48TreeToRuleSet {
    
    public OutputHypotheses treeToRules(J48Tree tree, String clasAttr){
        PathsAggregate paths = findPaths(tree);
        paths.printPaths();
        OutputHypotheses hypothesis = makeHypothesesFromPaths(paths, clasAttr);
        return hypothesis;
    }

    private PathsAggregate findPaths(J48Tree tree) {
        PathsAggregate paths = new PathsAggregate();
        for (Node node : tree.getNodes()){
            if (node.isLeaf()){
                TreePath path = followBranchToRoot(node, tree);
                paths.put(path);
            }
        }
        return paths;
    }

    public TreePath followBranchToRoot(Node leaf, J48Tree tree) {
        TreePath path = new TreePath();
        path.claz = leaf.getAttribute();
        Node parent = leaf;
        while(!parent.isRoot()){
            Branch link = tree.findBranchByTo(parent.getName());
            parent = tree.findNodeByName(link.getFrom());
            path.path.add(link);
        }
        return path;
    }

    public Rule makeRuleFromPath(TreePath path) {
        Rule rule = new Rule();
        for (Branch branch: path.path){
            Selector selector = makeSelectorFromBranch(branch);
            rule.addSelector(selector);
        }
        return rule;
    }

    public Selector makeSelectorFromBranch(Branch branch) {
        Selector selector = new Selector();
        selector.setName(branch.getAttribute());
        selector.setComparator(branch.getComparator());
        selector.setValue(branch.getValue().toString());
        return selector;
    }

    public ClassDescriptor prepareDescriptor(String clasAttr, String claz) {
        final ClassDescriptor classDescriptor = new ClassDescriptor();
        classDescriptor.name = clasAttr;
        classDescriptor.comparator = "=";
        classDescriptor.setValue(claz);
        return classDescriptor;
    }

    public OutputHypotheses makeHypothesesFromPaths(PathsAggregate paths, String clasAttr) {
        OutputHypotheses hypotheses = new OutputHypotheses();
        for (String claz : paths.getClasses()){
            List<TreePath> path = paths.getPathList(claz);
            Hypothesis hypothesis = makeHypothesisFromPath(path, clasAttr, claz);
            hypotheses.hypotheses.add(hypothesis);
        }
        return hypotheses;
    }

    public Hypothesis makeHypothesisFromPath(List<TreePath> paths, String clasAttr, final String claz) {
        final Hypothesis hypothesis = new Hypothesis();
        for (TreePath path:paths){
            Rule rule = makeRuleFromPath(path);
            hypothesis.rules.add(rule);
        }
        final ClassDescriptor clasDesc = prepareDescriptor(clasAttr, claz);
        hypothesis.setClasses(Util.singleElemList(clasDesc));
        return hypothesis;
    }
    
}
