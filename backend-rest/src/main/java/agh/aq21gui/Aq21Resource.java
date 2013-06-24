package agh.aq21gui;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.utils.CSVConverter;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.utils.OutputParser;
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
		Invoker inv = new Invoker();
		return inv.invoke(input);
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
		return run(input);
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
		Invoker inv = new Invoker();
		return inv.invoke(input);
    }
	
	@POST
	@Path("fromCSV")
	@Consumes({MediaType.TEXT_PLAIN})
	@Produces({
		MediaType.APPLICATION_JSON
	//	,MediaType.APPLICATION_XML
	})
	public Output fromCSV(String csv){
		Output out = new Output();
		CSVConverter conv = new CSVConverter();
		out.sEG(conv.convert(csv));
		return out;
	}
	
	@POST
	@Path("fromAQ21")
	@Consumes({MediaType.TEXT_PLAIN})
	@Produces({
		MediaType.APPLICATION_JSON
	//	,MediaType.APPLICATION_XML
	})
	public Input fromAQ21(String aq21){
		OutputParser parser = new OutputParser();
		String pure = CSVConverter.dewebify(aq21);
		Input out = parser.parse(pure);
		return out;
	}
	
	@POST
	@Path("debug")
	@Consumes({"text/plain"})
    @Produces({MediaType.APPLICATION_JSON})
	public Output getIt(String input) {
		OutputParser parser = new OutputParser();
		return parser.parse(input);
	}
	
	@GET
	@Path("stop")
	public String stop(){
		ServiceStopper stopper = new ServiceStopper();
		return stopper.stop();
	}
}
