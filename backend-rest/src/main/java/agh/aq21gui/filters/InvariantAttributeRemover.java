/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.utils.MapSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author marcin
 */
public class InvariantAttributeRemover {
    
    private Map<String, Integer> domainUsage;
    private MapSet<String> attributeUsage;
    private AttributeRemover remover = new AttributeRemover();
    
    public Output filter(Input in) {
        Input partiallyProcessed = in;
        initializeEmptyUsages(in);
        countAttributes(in);
        partiallyProcessed = removeUnnecessaryAttributes(partiallyProcessed, in);
        countDomains(partiallyProcessed);
        removeUnecessaryDomains(partiallyProcessed, in);
        return new Output(partiallyProcessed);
    }

    private void initializeEmptyUsages(Input in) {
        domainUsage = new HashMap<String, Integer>();
        for (Domain dom : in.getDomains()) {
            domainUsage.put(dom.name, 0);
        }
        attributeUsage = new MapSet<String>();
    }

    private void incremantStatistics(String domain) {
        Integer tmp = domainUsage.get(domain);
        if (tmp == null) {
            tmp = 0;
        }
        domainUsage.put(domain, tmp+1);
    }

    private void countAttributes(Input in) {
        for (Map<String, Object> event : in.getEvents()) {
            for (Entry<String, Object> entry : event.entrySet()) {
                Set<String> got = attributeUsage.get(entry.getKey());
                got.add(entry.getValue().toString());
            }
        }
    }

    private Input removeUnnecessaryAttributes(Input partiallyProcessed, Input in) {
        for (Entry<String, TreeSet<String>> entry : attributeUsage.entrySet()) {
            if (entry.getValue().size() <= 1) {
                partiallyProcessed = remover.dropAttribute(in, entry.getKey());
            }
        }
        return partiallyProcessed;
    }

    private void countDomains(Input partiallyProcessed) {
        for (Attribute remaining_attr : partiallyProcessed.gAG().attributes) {
            String domain = remaining_attr.getdomain();
            iterateThroughDomainsHierarchy(domain, partiallyProcessed);
        }
    }

    private void iterateThroughDomainsHierarchy(String domain, Input partiallyProcessed) {
        Domain domObject = partiallyProcessed.findDomain(domain);
        incremantStatistics(domain);
        if (domObject != null) {
            String new_domain = domObject.getdomain();
            if (new_domain!=null && !new_domain.isEmpty()) {
                iterateThroughDomainsHierarchy(new_domain, partiallyProcessed);
            }
        }
    }

    public void removeUnecessaryDomains(Input partiallyProcessed, Input in) {
        for (Entry<String, Integer> stat : domainUsage.entrySet()) {
            if (stat.getValue() == 0) {
                Domain domObject = partiallyProcessed.findDomain(stat.getKey());
                if (domObject != null) {
                    partiallyProcessed.getDomains().remove(domObject);
                }
            }
        }
    }
}
