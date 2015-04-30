/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.evaluator.Classifier;
import agh.aq21gui.evaluator.StatsAgregator;
import agh.aq21gui.model.output.Output;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author marcin
 */
@Path("metrics")
public class MetricsResource {
    
    @POST
	@Path("analyze")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({
		MediaType.APPLICATION_JSON
	})
    public StatsAgregator analyze(Output out){
        Classifier cfier = new Classifier(out);
        return cfier.performStatistics(out.getOutputHypotheses());
    }
}
