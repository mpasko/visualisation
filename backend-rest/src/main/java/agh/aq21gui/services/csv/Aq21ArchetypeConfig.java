/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.csv;

import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.input.Event;
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
public class Aq21ArchetypeConfig extends IArchetypeConfig{
    
    @Override
    public List<Test> createConfig(AttributesGroup attributes, DomainsGroup domains, EventsGroup events) {
        return createAq21Config(attributes, domains, events);
    }
    
    private Test generateRun(Attribute classAttr, DomainsGroup domains, EventsGroup events, int collumn) {
        Run r = new Run();
        r.setName("c1");
        Domain predictedDomain = classAttr.getdomainObjectRecursively(domains);
        String condition;
        if (predictedDomain.isContinuous() || predictedDomain.isInteger()){
            String mean = recalculateMean(events, collumn);
            condition = String.format(Locale.US,"[%s<=%s]", classAttr.getname(), mean);
        } else {
            condition = String.format("[%s=*]", classAttr.getname());
        }
        generateAq21SpecificParameters(r, condition);
        return r;
    }
    
    public List<Test> createAq21Config(AttributesGroup attributes, DomainsGroup domains, EventsGroup events) {
        List<Test> runs = new LinkedList<Test>();
        final int collumn = attributes.attributes.size() - 1;
        final Attribute classAttr = attributes.attributes.get(collumn);
        runs.add(generateRun(classAttr, domains, events, collumn));
        return runs;
    }

    public void generateAq21SpecificParameters(Run r, String condition) {
        r.addParameter("consequent", condition);
        r.addParameter("mode", "tf");
        r.addParameter("ambiguity", "IncludeInPos");
        r.addParameter("trim", "optimal");
        r.addParameter("compute_alternative_covers", "true");
        r.addParameter("maxstar", "1");
        r.addParameter("maxrule", "10");
        
        r.addParameter("learn_rules_mode", "standard");
        r.addParameter("exceptions", "false");
        r.addParameter("minimum_u", "1");
        r.addParameter("optimize_ruleset", "true");
        r.addParameter("continuous_optimization_probe", "5");
        r.addParameter("truncate", "true");
        r.addParameter("display_selectors_coverage", "true");
        r.addParameter("display_values_coverage", "false");
        r.addParameter("display_events_covered", "false");
        r.addParameter("display_alternative_covers", "true");
        r.addParameter("max_alternatives", "10");
        r.addParameter("attribute_selection_method", "none");
        r.addParameter("handling_unknown_values", "program_selected_method");
    }
}
