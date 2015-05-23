/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.aq21;

import agh.aq21gui.filters.Discretizer;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Output;

/**
 *
 * @author marcin
 */
public class Aq21FunctionalityWrapper {
    public Output enhancedInvoke(Input in){
        Aq21Invoker inv = new Aq21Invoker();
        ClassDescriptor descriptor = in.getAggregatedClassDescriptor();
        Input filteredData = in;
        filteredData = performDiscretizationWhenNeeded(in, descriptor, filteredData);
        Output created_by_algorithm = inv.invoke(filteredData);
        recoverUsedDate(created_by_algorithm, filteredData);
		return created_by_algorithm;
    }

    public void recoverUsedDate(Output created_by_algorithm, Input filteredData) {
        created_by_algorithm.replaceEventsGroup(filteredData.obtainEventsGroup());
    }

    public Input performDiscretizationWhenNeeded(Input in, ClassDescriptor descriptor, Input filteredData) {
        Domain domain = in.findDomainObjectRrecursively(descriptor.name);
        boolean classIsNumeric = domain.isContinuous()||domain.isInteger();
        if (descriptor.isCustomValue()&&classIsNumeric) {
            Discretizer discretizer = new Discretizer();
            Discretizer.Mode mode = Discretizer.Mode.SIMILAR_SIZE;
            filteredData = discretizer.discretize(in, descriptor.name, "square root", mode);
            //J48Service.expandRunsForStar(filteredData, descriptor);
        }
        return filteredData;
    }
}
