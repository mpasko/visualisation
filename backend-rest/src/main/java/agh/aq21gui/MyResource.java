
package agh.aq21gui;

import agh.aq21gui.model.gld.GLDOutput;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.management.Directory;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.OutputParser;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/myresource")
public class MyResource {

    // TODO: update the class to suit your needs
    
    // The Java method will process HTTP GET requests
    @GET 
	// The Java class will be hosted at the URI path "/myresource"
	@Path("/getIt")
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("text/plain")
    public String getIt() {
        return "Got it!";
    }
	
	@GET
	@Path("/template1JSON")
	@Produces(MediaType.APPLICATION_JSON)
	public Input getTemplate1JSON(){
		return StubFactory.getInput();
	}
	
	@GET
	@Path("/template1XML")
	@Produces(MediaType.APPLICATION_XML)
	public Input getTemplate1XML(){
		return StubFactory.getInput();
	}
	
	@GET
	@Path("/templateDBResult")
	@Produces(MediaType.APPLICATION_JSON)
	public Directory getDBDirectory() {
		Directory dir = StubFactory.getDirectory();
		return dir;
	}
	
	@GET
	@Path("templateGLDOutput")
	@Produces(MediaType.APPLICATION_JSON)
	public GLDOutput getGLD(){
		return StubFactory.getGLDOutputBaloons();
	}
}
