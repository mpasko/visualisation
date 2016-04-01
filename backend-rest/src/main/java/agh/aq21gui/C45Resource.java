/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.AlgorithmIOWrapper;
import agh.aq21gui.services.c45.C45FunctionalityWrapper;
import agh.aq21gui.services.csv.C45ArchetypeConfig;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource for J48 (C4.5) Algorithm
 * @author marcin
 */
@Path("c45")
public class C45Resource implements IResource{
    
    AlgorithmIOWrapper wrapper = new AlgorithmIOWrapper(getName());
    
    @POST
	@Path("postIt")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({
		MediaType.APPLICATION_JSON
	})
    @Override
    public Output performExperiment(Input input) {
        //System.out.println("perform experiment input:\n");
        //Printer.printLines(input.toString(), this.getClass());
        input = wrapper.inputPreProcessing(input);
		C45FunctionalityWrapper srv = new C45FunctionalityWrapper();
        Output output = srv.run(input);
        output = wrapper.outputPostProcessing(output);
        return output;
    }
    
    @POST
    @Path("generateConfig")
	@Consumes({
		MediaType.APPLICATION_JSON
	})
	@Produces({
		MediaType.APPLICATION_JSON
	})
    @Override
    public RunsGroup generateConfig(Input in) {
        List<Test> runs = new C45ArchetypeConfig().createConfig(in);
        RunsGroup group = new RunsGroup();
        group.setRuns(runs);
        wrapper.addDefaultValues(in, group);
        return group;
    }
    
    @Override
    public String getName() {
        return "C45";
    }
}
