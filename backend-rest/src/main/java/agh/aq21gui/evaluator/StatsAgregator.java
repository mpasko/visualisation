/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.evaluator;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.utils.Util;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class StatsAgregator {
    private Map<String, Statistics> particular = new HashMap<String, Statistics>();

    /**
     * @return the particular
     */
    @XmlElement(name="particular")
    public Map<String, Statistics> getParticular() {
        return particular;
    }

    /**
     * @param particular the particular to set
     */
    public void setParticular(Map<String, Statistics> particular) {
        this.particular = particular;
    }
    
    public void addHypothesis(String name, Statistics stats) {
        particular.put(name, stats);
    }
    
    @Override
    public String toString() {
        return Util.objectToJson(this);
    }
    
    public String generateCounterMeasuresReport(Input whole) {
        StringBuilder build = new StringBuilder();
        for (Entry<String, Statistics> entry : particular.entrySet()) {
            build.append(entry.getKey()).append(":\n");
            for (CounterExample example : entry.getValue().collectCounterExamples()) {
                build.append(example.getType()).append("\n");
                Hypothesis hypo = example.getHypo();
                build.append(hypo.toString()).append("\n");
                Map<String, Object> keyValue = whole.generateKeyValue(example.getEvent());
                String event = Util.formatEventByKeyParams(hypo.getKeyParameters(), keyValue);
                build.append(event).append("\n");
            }
        }
        return build.toString();
    }
}
