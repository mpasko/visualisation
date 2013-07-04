
package agh.aq21gui;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.management.Directory;
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
	
	private Input mockFactory(){
		Input input = new Input();
		input.addDomain("domain.name", "domain.sub", "domain.parameters");
		input.addAttribute("attribute.name", "attribut.domain", "attribute.param");
		
		RunsGroup runsGroup = new RunsGroup();
		runsGroup.addParameter("globalParam.name", "globalParam.value");
		Run run = new Run();
		run.addParameter("runParam.name", "runParam.value");
		runsGroup.runs.add(run);
		input.runsGroup = runsGroup;

		input.addEvent("event1.value1","event1.value2");
		input.addEvent("event2.value1","event2.value2");
		return input;
	}
	
	private Input workingFactory(){
		//Based on template1.aq21
		Input input = new Input();
		input.addDomain("color", "nominal", "{red, green, blue}");
		input.addAttribute("background", "color", "");
		input.addAttribute("number", "linear", "{ 0, 1, 2 }");
		input.addAttribute("length", "continuous", "0, 200");
		input.addAttribute("class", "nominal", "{c1, c2}");
		RunsGroup runs = new RunsGroup();
		Run run_c1 = new Run();
		run_c1.name = "Run_c1";
		run_c1.addParameter("Mode", "TF");
		run_c1.addParameter("Consequent", "[class=c1]");
		run_c1.addParameter("Ambiguity", "IncludeInPos");
		run_c1.addParameter("Trim", "Optimal");
		run_c1.addParameter("Compute_alternative_covers", "True");
		run_c1.addParameter("Maxstar", "1");
		run_c1.addParameter("Maxrule", "10");
		runs.runs.add(run_c1);
		
		Run run_c2 = new Run();
		run_c2.name = "Run_c2";
		run_c2.addParameter("Mode", "TF");
		run_c2.addParameter("Consequent", "[class=c2]");
		run_c2.addParameter("Ambiguity", "IncludeInPos");
		run_c2.addParameter("Trim", "Optimal");
		run_c2.addParameter("Compute_alternative_covers", "False");
		run_c2.addParameter("Maxstar", "1");
		run_c2.addParameter("Maxrule", "1");
		runs.runs.add(run_c2);
		
		Run run_all_in_pd = new Run();
		run_all_in_pd.name = "Run_All_in_PD";
		run_all_in_pd.addParameter("Mode", "PD");
		run_all_in_pd.addParameter("Consequent", "[class=*]");
		run_all_in_pd.addParameter("Ambiguity", "IncludeInPos");
		run_all_in_pd.addParameter("Trim", "Optimal");
		run_all_in_pd.addParameter("Compute_alternative_covers", "False");
		run_all_in_pd.addParameter("Maxstar", "1");
		run_all_in_pd.addParameter("Maxrule", "1");
		runs.runs.add(run_all_in_pd);
		
		Run run_multi_head = new Run();
		run_multi_head.name = "Run_Multi-head";
		run_multi_head.addParameter("Mode", "PD");
		run_multi_head.addParameter("Consequent", "[class=c1][length<=40]");
		run_multi_head.addParameter("Ambiguity", "IncludeInPos");
		run_multi_head.addParameter("Trim", "Optimal");
		run_multi_head.addParameter("Compute_alternative_covers", "False");
		run_multi_head.addParameter("Maxstar", "1");
		run_multi_head.addParameter("Maxrule", "1");
		runs.runs.add(run_multi_head);
		
		input.runsGroup = runs;
		
		input.addEvent("red","1","34.6","c1");
		input.addEvent("green","0","2.45","c2");
		input.addEvent("red","1","33.0","c1");
		input.addEvent("blue","0","33.5","c2");
		return input;
	}
	
	@GET
	@Path("/sampleJSON")
	@Produces(MediaType.APPLICATION_JSON)
	public Input getSampleInputJSON(){
		return mockFactory();
	}
	
	@GET
	@Path("/template1JSON")
	@Produces(MediaType.APPLICATION_JSON)
	public Input getTemplate1JSON(){
		return workingFactory();
	}
	
	@GET
	@Path("/templateDBResult")
	@Produces(MediaType.APPLICATION_JSON)
	public Directory getDirectory() {
		Directory dir = new Directory();
		dir.putExperiment("mock", mockFactory());
		dir.putExperiment("working", workingFactory());
		return dir;
	}
	
	@GET
	@Path("/sampleXML")
	@Produces(MediaType.APPLICATION_XML)
	public Input getSampleInputXML(){
		return mockFactory();
	}
	
	@GET
	@Path("/sampleAQ21")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSampleAQ21(){
		return mockFactory().toString();
	}
}
