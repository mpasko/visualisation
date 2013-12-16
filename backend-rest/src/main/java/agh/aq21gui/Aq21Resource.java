package agh.aq21gui;

import agh.aq21gui.algorithms.GLDOptimizer;
import agh.aq21gui.algorithms.SimulatedAnnealing;
import agh.aq21gui.model.gld.GLDInput;
import agh.aq21gui.model.gld.GLDOutput;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.management.Directory;
import agh.aq21gui.model.management.InputPair;
import agh.aq21gui.model.management.OutputPair;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.utils.CSVConverter;
import agh.aq21gui.utils.OutputParser;
import dataaccess.Repository;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * Root resource (exposed at "aq21" path)
 * @author marcin
 */
@Path("aq21")
public class Aq21Resource {
	
	@Context
    private SecurityContext security;
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
	public Input fromCSV(String csv){
		CSVConverter conv = new CSVConverter();
		Input in = conv.convert(csv);
		return in;
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
	@Path("toAQ21")
	@Consumes({
		MediaType.APPLICATION_JSON
	//	,MediaType.APPLICATION_XML
	})
	@Produces({MediaType.TEXT_PLAIN})
	public String toAQ21(Output aq21){
		return aq21.toString();
	}
	
	@GET
	@Path("browse")
    @Produces({MediaType.APPLICATION_JSON})
	public Directory browseIt() throws Exception{
		Logger.getLogger("database").info("browse start");
		try{
			Repository repo = Repository.getRepository();
			Logger.getLogger("database").info("get repo");
			Directory dir = repo.getDirectory();
			Logger.getLogger("database").info("get dir");
			if(dir==null){
				Logger.getLogger("database").severe("Error: dir is null!");
			}
			return dir;
		}catch (Exception e) {
			Logger.getLogger("database").log(Level.SEVERE, "Error: {0}", e.getMessage());
            //System.exit(100);
			throw e;
		}
		//return null;
	}
	
	@GET
	@Path("browseExperiment/{name}")
	@Produces({MediaType.APPLICATION_JSON})
	public Input getExperiment(@PathParam("name")String name) throws Exception{
		try {
			Repository repo = Repository.getRepository();
			return repo.getExperiment(name);
		} catch (Exception ex) {
			Logger.getLogger(Aq21Resource.class.getName()).log(Level.SEVERE, null, ex);
			throw ex;
		}
	}
	
	@GET
	@Path("browseResult/{name}")
	@Produces({MediaType.APPLICATION_JSON})
	public Output getResult(@PathParam("name")String name) throws Exception{
		try {
			Repository repo = Repository.getRepository();
			return repo.getResult(name);
		} catch (Exception ex) {
			Logger.getLogger(Aq21Resource.class.getName()).log(Level.SEVERE, null, ex);
			throw ex;
		}
	}
	
	@POST
	@Path("saveExperiment")
	@Consumes({MediaType.APPLICATION_JSON})
	public void saveExp(InputPair input) throws Exception{
		try{
			Repository repo = Repository.getRepository();
	//		System.out.println("To save in database:");
	//		System.out.println(input.toString());
			repo.saveExperiment(input);
		}catch (Exception e) {
			Logger.getLogger("database").log(Level.SEVERE, "Error: {0}", e.getMessage());
            //e.printStackTrace();
            //System.exit(100);
			throw e;
		}
	}
	
	@POST
	@Path("saveResult")
	@Consumes({MediaType.APPLICATION_JSON})
	public void saveRes(OutputPair output) throws Exception{
		try{
			Repository repo = Repository.getRepository();
	//		System.out.println("To save in database:");
	//		System.out.println(input.toString());
			repo.saveResult(output);
		}catch (Exception e) {
			Logger.getLogger("database").log(Level.SEVERE, "Error: {0}", e.getMessage());
            //e.printStackTrace();
            //System.exit(100);
			throw e;
		}
	}
	
	@POST
	@Path("debug")
	@Consumes({"text/plain"})
    @Produces({MediaType.APPLICATION_JSON})
	public Output getIt(String input) {
		OutputParser parser = new OutputParser();
		return parser.parse(input);
	}
	
	@POST
	@Path("gld")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public GLDOutput optimizeGLD(GLDInput input){
		GLDOptimizer optimizer = new GLDOptimizer(input, new SimulatedAnnealing());
		return optimizer.optimize();
	}
	
	@GET
	@Path("stop")
	public String stop(){
		ServiceStopper stopper = new ServiceStopper();
		return stopper.stop();
	}
}
