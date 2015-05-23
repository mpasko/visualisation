package agh.aq21gui;

import agh.aq21gui.algorithms.GLDOptimizer;
import agh.aq21gui.algorithms.OptimizationAlgorithm;
import agh.aq21gui.algorithms.SimulatedAnnealing;
import agh.aq21gui.model.gld.GLDInput;
import agh.aq21gui.model.gld.GLDOutput;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.management.Directory;
import agh.aq21gui.model.management.InputPair;
import agh.aq21gui.model.management.OutputPair;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.aq21.Aq21FunctionalityWrapper;
import agh.aq21gui.services.csv.CSVConverter;
import agh.aq21gui.services.aq21.OutputParser;
import agh.aq21gui.services.csv.Aq21ArchetypeConfig;
import dataaccess.Repository;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
public class Aq21Resource implements IResource{
    public static final String DATABASE = "database";
	
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
	})
    @Override
    public Output performExperiment(Input input) {
		return new Aq21FunctionalityWrapper().enhancedInvoke(input);
    }
    
    @Override
    public String getName() {
        return "AQ21";
    }
	
	@POST
	@Path("fromAQ21")
	@Consumes({MediaType.TEXT_PLAIN})
	@Produces({
		MediaType.APPLICATION_JSON
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
	})
	@Produces({MediaType.TEXT_PLAIN})
	public String toAQ21(Output aq21){
		return aq21.toString();
	}
    
    @POST
    @Path("generateConfig")
	@Consumes({
		MediaType.APPLICATION_JSON
	})
	@Produces({
		MediaType.APPLICATION_JSON
	})
    public RunsGroup generateConfig(Input in) {
        List<Test> runs = new Aq21ArchetypeConfig().createConfig(in);
        RunsGroup group = new RunsGroup();
        group.setRuns(runs);
        return group;
    }
	
	@GET
	@Path("browse")
    @Produces({MediaType.APPLICATION_JSON})
	public Directory browseIt() throws Exception{
		Logger.getLogger(DATABASE).info("browse start");
		try{
			Repository repo = Repository.getRepository();
			Logger.getLogger(DATABASE).info("get repo");
			Directory dir = repo.getDirectory();
			Logger.getLogger(DATABASE).info("get dir");
			if(dir==null){
				Logger.getLogger(DATABASE).severe("Error: dir is null!");
			}
			return dir;
		}catch (Exception e) {
			Logger.getLogger(DATABASE).log(Level.SEVERE, e.getMessage());
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
			final Input experiment = repo.getExperiment(name);
			//System.out.println("Experiment -reloaded:");
			//System.out.println(experiment.toString());
			return experiment;
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
			//System.out.println("Experiment -before save:");
			//System.out.println(input.toString());
			Repository repo = Repository.getRepository();
			repo.saveExperiment(input);
		}catch (Exception e) {
			Logger.getLogger(DATABASE).log(Level.SEVERE, e.getMessage());
			throw e;
		}
	}
	
	@POST
	@Path("saveResult")
	@Consumes({MediaType.APPLICATION_JSON})
	public void saveRes(OutputPair output) throws Exception{
		try{
			Repository repo = Repository.getRepository();
			repo.saveResult(output);
		}catch (Exception e) {
			Logger.getLogger(DATABASE).log(Level.SEVERE, e.getMessage());
			throw e;
		}
	}
	
	@GET
	@Path("drop")
	public String dropDatabase() throws Exception{
		try{
			Repository repo = Repository.getRepository();
			repo.dropDataBase();
		}catch (Exception e) {
			Logger.getLogger(DATABASE).log(Level.SEVERE, e.getMessage());
			throw e;
		}
		return "";
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
		final OptimizationAlgorithm algorithm = new SimulatedAnnealing();
		algorithm.setProperties(input.getSAProperties());
		GLDOptimizer optimizer = new GLDOptimizer(input, algorithm);
		return optimizer.optimize();
	}
	
	@GET
	@Path("stop")
	public String stop(){
		ServiceStopper stopper = new ServiceStopper();
		return stopper.stop();
	}
}
