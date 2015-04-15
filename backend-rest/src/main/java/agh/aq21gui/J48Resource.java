/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.aq21.Aq21Invoker;
import agh.aq21gui.services.csv.Aq21ArchetypeConfig;
import agh.aq21gui.services.csv.J48ArchetypeConfig;
import agh.aq21gui.services.j48.J48Service;
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
@Path("j48")
public class J48Resource implements IResource{
    
    @POST
	@Path("postIt")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({
		MediaType.APPLICATION_JSON
	})
    @Override
    public Output performExperiment(Input input) {
		J48Service srv = new J48Service();
        return srv.convertAndRun(input);
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
        List<Test> runs = new J48ArchetypeConfig().createConfig(in);
        RunsGroup group = new RunsGroup();
        group.setRuns(runs);
        return group;
    }
    
    @Override
    public String getName() {
        return "J48";
    }
}
