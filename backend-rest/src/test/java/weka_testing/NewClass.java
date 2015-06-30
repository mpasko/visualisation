/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka_testing;

import agh.aq21gui.JRipResource;
import agh.aq21gui.converters.Aq21InputToWeka;
import agh.aq21gui.filters.AttributeRemover;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.services.csv.JRipArchetypConfig;
import agh.aq21gui.services.jripp.JrippService;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.Util;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import weka.classifiers.rules.JRip;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

/**
 *
 * @author legion-primary
 */
public class NewClass {
    public static void main(String[] args) throws IOException, Exception {
        CSVLoader loader = new CSVLoader();
        loader.setFile(new File("C:\\Users\\micha_000\\Desktop\\git\\visualisation\\backend-rest\\experiment_inputs\\adi.csv"));
        
        
        Instances instances = loader.getDataSet();
        
        instances.setClassIndex(0);
        
        
        Input input = StubFactory.loadAdiData();
        JRipResource config = new JRipResource();
        RunsGroup runsGroup = config.generateConfig(input);
        runsGroup.enforceClassForAll("stop", null);
        runsGroup.enforceModeForAll("");
        runsGroup.enforceParameter("prune", "true");
        input.setRunsGroup(runsGroup);
        
        //System.out.println(input.toString());
        input = new AttributeRemover().dropAttributes(input, new LinkedList<String>());
        Instances ourInstances = new Aq21InputToWeka().aq21ToWeka(input);
        String stried = ourInstances.toString();
        ourInstances.setClassIndex(0);
        JRip jripp = new JRip();    
        
        jripp.setOptions(new String[] {"-S", "1", "-O","2", "-N","2.0","-F", "3"});
        jripp.buildClassifier(ourInstances);
        
    }
}
