package agh.aq21gui;

import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.OutputParser;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "aq21" path)
 * @author marcin
 */
@Path("aq21")
public class Aq21Resource {
	
	private Output invoke(Input input){
		if(input == null){
			Logger.getLogger(Aq21Resource.class.getName()).warning("AQ21 received null or empty object!");
			return null;
		}
		OutputParser parser = new OutputParser();
//		if (input.attributesGroup instanceof AttributesGroup){
			AttributesGroup ag = (AttributesGroup) input.gAG();
			if (ag.attributes.get(0).name.equals("blabla")){
				Logger.getLogger(Aq21Resource.class.getName()).info("Special case: 'blabla' attribute!");
				return parser.parse("blabla");
			}
//		}
		Logger.getLogger(Aq21Resource.class.getName()).info("Request accepted");
		Invoker invoker = new Invoker();
		String result;
		try {
//			System.out.println("Received AQ21 format:");
//			System.out.println(input.toString());
			result = invoker.invoke(input.toString());
			return parser.parse(result);
		} catch (IOException ex) {
			Logger.getLogger(Aq21Resource.class.getName()).log(Level.WARNING, null, ex);
		} catch (ProgramExecutionException ex) {
			Logger.getLogger(Aq21Resource.class.getName()).log(Level.WARNING, null, ex);
		}
		return null;
	}
	/**
     * Method handling HTTP POST requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
	 * @param input Input
     * @return Output
     */
    @POST
	@Path("postIt")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({
		MediaType.APPLICATION_JSON
	//	,MediaType.APPLICATION_XML
	})
    public Output postIt(Input input) {
		return invoke(input);
    }
	
	/* Get cannot consume any arguments */
	/* *x/ 
	@GET
	@Path("getIt")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({
		MediaType.APPLICATION_JSON
	//	,MediaType.APPLICATION_XML
	})
    public Output getIt(Input input) {
		return invoke(input);
    }
	/* */ 
	
	@PUT
	@Path("putIt")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({
		MediaType.APPLICATION_JSON
	//	,MediaType.APPLICATION_XML
	})
    public Output putIt(Input input) {
		return invoke(input);
    }
	
	@POST
	@Path("debug")
	@Consumes({"text/plain"})
    @Produces({MediaType.APPLICATION_JSON})
	public Output getIt(String input) {
		OutputParser parser = new OutputParser();
		return parser.parse(input);
	}
}
