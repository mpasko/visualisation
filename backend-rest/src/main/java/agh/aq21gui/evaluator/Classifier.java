/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.evaluator;

import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.utils.NumericUtil;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class Classifier {
    private final Input input;
    
    private boolean questionAsFalse = false;
    
    public Classifier(Input globalData){
        input = globalData;
    }
    
    public void setQuestionAsFalse(boolean _qasf) {
        this.questionAsFalse = _qasf;
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
        String className = hypothesisClass.name.toLowerCase(Locale.getDefault());
        String eventClass = map.get(className).toString().toLowerCase(Locale.getDefault());
        Domain classDom = input.findDomainObjectRrecursively(hypothesisClass.getName());
        if (!NumericUtil.isWildcard(eventClass)) {
            boolean premiseMatches;
            boolean classMatches;
            if (questionAsFalse) {
                premiseMatches = hypo.matchesEventStrictly(map, input);
                classMatches = hypothesisClass.matchesValueStrictly(eventClass, classDom.set_elements);
            } else {
                premiseMatches = hypo.matchesEvent(map, input);
                classMatches = hypothesisClass.matchesValue(eventClass, classDom.set_elements);
            }
            logSingleEvent(event, premiseMatches, classMatches);
            if (premiseMatches != classMatches) {
                String type = "";
                if (premiseMatches && !classMatches) {
                    type = "FalsePositive";
                } else if (!premiseMatches && classMatches) {
                    type = "FalseNegative";
                }
                saveCounterExample(event, hypo, type, stats);
            }
            stats.analyzeCase(premiseMatches, classMatches);
        }
    }

    private void logSingleEvent(Event event, boolean premiseMatches, boolean theoryMatches) {
        /*
        System.out.println("Case:");
        System.out.print(event.toString());
        System.out.println(String.format("Outcome: premise=%s theory=%s\n", premiseMatches, theoryMatches));
        */
    }

    private void saveCounterExample(Event event, Hypothesis hypo, String type, Statistics stats) {
        CounterExample counterExample = new CounterExample(event, hypo, type);
        stats.addCounterExample(counterExample);
    }
}
