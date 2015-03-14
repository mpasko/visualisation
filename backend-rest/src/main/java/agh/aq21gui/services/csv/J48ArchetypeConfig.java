/*
 * To change this template, choose Tools | Templates
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
 * @author marcin
 */
public class J48ArchetypeConfig extends IArchetypeConfig{

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
        addJ48SpecificParameters(r, condition);
        return r;
    }
    
    public List<Test> createJ48Config(AttributesGroup attributes, DomainsGroup domains, EventsGroup events) {
        List<Test> runs = new LinkedList<Test>();
        final int collumn = attributes.attributes.size() - 1;
        final Attribute classAttr = attributes.attributes.get(collumn);
        runs.add(generateRun(classAttr, domains, events, collumn));
        return runs;
    }
    
    @Override
    public List<Test> createConfig(AttributesGroup attributes, DomainsGroup domains, EventsGroup events) {
        return createJ48Config(attributes, domains, events);
    }

    public void addJ48SpecificParameters(Test r, String condition) {
        r.addParameter("prune", "false");
        r.addParameter("collapse", "false");
        r.addParameter("minimum_instances", "2");
    }

    public void addAllJ48SpecificParameters(List<Test> list) {
        for (Test t : list){
            addJ48SpecificParameters(t, t.grepCondition());
        }
    }
    
}
