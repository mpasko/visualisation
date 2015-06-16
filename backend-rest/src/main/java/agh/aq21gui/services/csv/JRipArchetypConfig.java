/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.csv;

import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.input.EventsGroup;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.input.Test;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author legion-primary
 */
public class JRipArchetypConfig extends IArchetypeConfig {

    @Override
    public List<Test> createConfig(AttributesGroup attributes, DomainsGroup domains, EventsGroup events) {
        return createJRipConfig(attributes, domains, events);
    }

    private List<Test> createJRipConfig(AttributesGroup attributes, DomainsGroup domains, EventsGroup events) {
         List<Test> runs = new LinkedList<Test>();
        final int collumn = attributes.attributes.size() - 1;
        final Attribute classAttr = attributes.attributes.get(collumn);
        runs.add(generateRun(classAttr, domains, events, collumn));
        return runs;
    }
    
    private Test generateRun(Attribute attr, DomainsGroup domains, EventsGroup events, int collumn) {
        Run r = new Run();
        r.setName("c1");
        String condition;
        Domain dom = attr.getdomainObjectRecursively(domains);
        if (dom.isContinuous() || dom.isInteger()){
            String mean = recalculateMean(events, collumn);
            condition = String.format(Locale.US,"[%s<=%s]", attr.getname(), mean);
        } else {
            condition = String.format("[%s=*]", attr.getname());
        }
        r.addParameter("consequent", condition);
        addJRipSpecificParameters(r, condition);
        return r;
    }

    private void addJRipSpecificParameters(Test r, String condition) {
        r.addParameter("folds", "3");
        r.addParameter("minimal_weights", "2.0");
        r.addParameter("number_of_runs", "10");
        r.addParameter("debug", "false");
        r.addParameter("seed", "1");
        r.addParameter("not_check_error_rate", "false");
        r.addParameter("not_use_pruning", "false");
    }
    
    public void addAllJRipSpecificParameters(List<Test> list) {
        for (Test t : list){
            addJRipSpecificParameters(t, t.grepCondition());
        }
    }
    
}
