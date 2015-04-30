/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.evaluator;

import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.utils.NumericUtil;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class Classifier {
    private final Input input;
    
    public Classifier(Input globalData){
        input = globalData;
    }
    
    public StatsAgregator performStatistics(Collection<Hypothesis> hypos) {
        //Collection<Hypothesis> hypos = input.getOutputHypotheses();
        StatsAgregator stats = new StatsAgregator();
        for (Hypothesis hypo : hypos) {
            stats.addHypothesis(hypo.name, performStatistics(hypo));
        }
        return stats;
    }
    
    public Statistics performStatistics(Hypothesis hypo) {
        Statistics stats = new Statistics();
        for(Event event : input.obtainEventsGroup().events) {
            analyzeEvent(event, hypo, stats);
        }
        return stats;
    }

    public void analyzeEvent(Event event, Hypothesis hypo, Statistics stats) {
        ClassDescriptor hypothesisClass = hypo.getClasses().get(0);
        Map<String, Object> map = input.generateKeyValue(event);
        String eventClass = map.get(hypothesisClass.name).toString();
        if (!NumericUtil.isWildcard(eventClass)) {
            boolean premiseMatches = hypo.matchesEvent(map);
            boolean theoryMatches = hypothesisClass.matchesValue(eventClass);
            logSingleEvent(event, premiseMatches, theoryMatches);
            stats.analyzeCase(premiseMatches, theoryMatches);
        }
    }

    private void logSingleEvent(Event event, boolean premiseMatches, boolean theoryMatches) {
        /*
        System.out.println("Case:");
        System.out.print(event.toString());
        System.out.println(String.format("Outcome: premise=%s theory=%s\n", premiseMatches, theoryMatches));
        */
    }
}
