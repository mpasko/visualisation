/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services;

import agh.aq21gui.filters.AttributeRemover;
import agh.aq21gui.filters.ContinuousClassFilter;
import agh.aq21gui.filters.RuleAgregator;
import agh.aq21gui.filters.RulePrunner;
import agh.aq21gui.filters.RuleSorter;
import agh.aq21gui.filters.RuleVerticalAgregator;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Parameter;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Util;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class AlgorithmIOWrapper {

    private List<String> ignore_attributes = new LinkedList<String>();
    private boolean aggregate = false;
    private boolean vertical_aggregate = false;
    private boolean post_prune = false;
    private boolean sort = false;
    private List<DiscretizerRanges> ranges = new LinkedList<DiscretizerRanges>();

    public Output outputPostProcessing(Output result) {
        Output processed = result;
        /* */
        if (post_prune) {
            processed = new RulePrunner().doAll(processed);
        }
        if (aggregate) {
            processed = new RuleAgregator().agregate(processed);
        }
        /* */
        if (vertical_aggregate) {
            processed = new RuleVerticalAgregator().agregate(processed);
        }
        /* */
        if (sort) {
            processed = new RuleSorter().sort(processed);
        }
        /* */
        return processed;
    }

    public Input inputPreProcessing(Input input) {
        Input copy = Util.deepCopyInput(input);
        parseParameters(copy.runsGroup);
        copy = applyRangeDiscretization(copy);
        copy = new AttributeRemover().dropAttributes(copy, ignore_attributes);
        copy = removeAdditionalOptions(copy);
        return copy;
    }

    private Input applyRangeDiscretization(Input in) {
        Input result = in;
        for (DiscretizerRanges range : ranges) {
            ClassDescriptor cd = new ClassDescriptor(range.attribute, NumericUtil.dobleListToStringList(range.values));
            result = new ContinuousClassFilter().filter(result, cd, range.labels);
        }
        return result;
    }

    public void addDefaultValues(Input input, RunsGroup runs) {
        for (Test run : runs.runs) {
            run.addParameter("aggregate", "false");
            run.addParameter("vertical_aggregate", "false");
            run.addParameter("post_prune", "false");
            run.addParameter("sort", "false");
            run.addParameter("ignore_attributes", "[ignore=]");
            run.addParameter("discretize_ranges", "[]");
        }
        /*LinkedList<Parameter> parameters = new LinkedList<Parameter>();
         parameters.add();
         return parameters;*/
    }

    private Input removeAdditionalOptions(Input input) {
        for (Test run : input.runsGroup.runs) {
            List<Parameter> parameters = run.getRunSpecificParameters();
            ArrayList<Parameter> copy = new ArrayList<Parameter>(parameters);
            for (Parameter param : copy) {
                if (param.name.equalsIgnoreCase("aggregate")
                        || param.name.equalsIgnoreCase("vertical_aggregate")
                        || param.name.equalsIgnoreCase("post_prune")
                        || param.name.equalsIgnoreCase("sort")
                        || param.name.equalsIgnoreCase("ignore_attributes")
                        || param.name.equalsIgnoreCase("discretize_ranges")) {
                    parameters.remove(param);
                }
            }
        }
        return input;
    }

    private void parseParameters(RunsGroup runs) {
        if (runs.runs.size() >= 1) {
            parseParameters(runs.runs.get(0).getRunSpecificParameters());
        }
        parseParameters(runs.getGlobalLearningParameters());
    }

    private void parseParameters(List<Parameter> parameters) {
        for (Parameter param : parameters) {
            if (param.name.equalsIgnoreCase("aggregate")) {
                aggregate = param.isTrue();
            } else if (param.name.equalsIgnoreCase("vertical_aggregate")) {
                vertical_aggregate = param.isTrue();
            } else if (param.name.equalsIgnoreCase("post_prune")) {
                post_prune = param.isTrue();
            } else if (param.name.equalsIgnoreCase("sort")) {
                sort = param.isTrue();
            } else if (param.name.equalsIgnoreCase("ignore_attributes")) {
                ignore_attributes = new LinkedList<String>();
                for (ClassDescriptor desc : param.getDescriptors()) {
                    ignore_attributes.addAll(desc.set_elements);
                }
            } else if (param.name.equalsIgnoreCase("discretize_ranges")) {
                for (ClassDescriptor descriptor : param.getDescriptors()) {
                    List<Double> values = NumericUtil.stringListToDoubleList(descriptor.set_elements);
                    ranges.add(new DiscretizerRanges(descriptor.getName(), values));
                }
            }
        }
    }
}