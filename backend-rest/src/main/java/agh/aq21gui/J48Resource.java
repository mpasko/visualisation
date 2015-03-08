/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.aq21.Invoker;
import agh.aq21gui.services.j48.J48Service;
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
public class J48Resource {
    
    @POST
	@Path("postIt")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({
		MediaType.APPLICATION_JSON
	})
    public Output postIt(Input input) {
		J48Service srv = new J48Service();
        return srv.convertAndRun(input);
    }
}
