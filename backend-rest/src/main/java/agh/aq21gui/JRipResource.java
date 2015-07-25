/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import agh.aq21gui.services.csv.JRipArchetypConfig;
import agh.aq21gui.services.j48.J48Service;
import agh.aq21gui.services.jripp.JrippService;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author legion-primary
 */
@Path("jripp")
public class JRipResource implements IResource{

    @POST
	@Path("postIt")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({
		MediaType.APPLICATION_JSON
	})
    @Override
    public Output performExperiment(Input input) {
	JrippService srv = new JrippService();
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
        List<Test> runs = new JRipArchetypConfig().createConfig(in);
        RunsGroup group = new RunsGroup();
        group.setRuns(runs);
        return group;
    }
    
    @Override
    public String getName() {
        return "JRip";
    }
}
