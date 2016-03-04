/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.evaluator;

import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.output.Hypothesis;

/**
 *
 * @author marcin
 */
public class CounterExample {
    private Event event;
    private Hypothesis hypo;
    private String type;

    public CounterExample(Event event, Hypothesis hypo, String type) {
        this.event = event;
        this.hypo = hypo;
        this.type = type;
    }

    /**
     * @return the event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * @return the hypo
     */
    public Hypothesis getHypo() {
        return hypo;
    }

    /**
     * @param hypo the hypo to set
     */
    public void setHypo(Hypothesis hypo) {
        this.hypo = hypo;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
}
