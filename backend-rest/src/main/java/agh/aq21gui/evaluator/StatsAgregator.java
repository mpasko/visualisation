/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.evaluator;

import agh.aq21gui.utils.Util;
import java.util.HashMap;
import java.util.Map;
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
    
    @XmlElement(name="totall")
    public Statistics getTotall(){
        Statistics totall = new Statistics();
        for(Statistics stats : getParticular().values()) {
            totall.increment(stats);
        }
        return totall;
    }

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
}
