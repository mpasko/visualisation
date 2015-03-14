/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.stubs;

import agh.aq21gui.services.aq21.Aq21Invoker;
import agh.aq21gui.algorithms.GLDConverter;
import agh.aq21gui.algorithms.GLDState;
import agh.aq21gui.model.gld.Argument;
import agh.aq21gui.model.gld.ArgumentsGroup;
import agh.aq21gui.model.gld.GLDInput;
import agh.aq21gui.model.gld.GLDOutput;
import agh.aq21gui.model.gld.Value;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.management.Directory;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.services.aq21.OutputParser;
import agh.aq21gui.utils.Util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class StubFactory {

	public static Input getInput() {
		Input input = new Input();
		input.addDomain("color", "nominal", "{red, green, blue}");
		input.addAttribute("background", "color", "");
		input.addAttribute("number", "linear", "{ 0, 1, 2 }");
		input.addAttribute("length", "continuous", "0, 200");
		input.addAttribute("class", "nominal", "{c1, c2}");
		input.addEvent("red", "1", "34.6", "c1");
		input.addEvent("green", "0", "2.45", "c2");
		input.addEvent("red", "1", "33.0", "c1");
		input.addEvent("blue", "0", "33.5", "c2");
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
		return input;
	}
	
	public static Output getOutput() {
		return new Aq21Invoker().invoke(getInput());
	}

	public static Directory getDirectory() {
		Directory dir = new Directory();
		dir.putExperiment("working", StubFactory.getInput());
		return dir;
	}

	public static Output createSample2nominal() {
		Output sample2nominal = new Output();
		sample2nominal.addAttribute("1", "nominal", "a,b,c");
		sample2nominal.addAttribute("2", "nominal", "d,e");
		final Selector selector1 = Utils.listSelector("1", Util.strings("b", "c"));
		final Selector selector2 = Utils.valueSelector("2", "=", "d");
		List<Hypothesis> hyps = new LinkedList<Hypothesis>();
		hyps.add(Utils.hypothesis(selector1, selector2));
		sample2nominal.setOutputHypotheses(hyps);
		return sample2nominal;
	}

	public static GLDState getGLDState() {
		GLDOutput initialData = getGLDOutput();
		initialData.setColumns(getArgumentsGroupSample("column").getArguments());
		initialData.setRows(getArgumentsGroupSample("row").getArguments());
		GLDState instance = GLDState.build(initialData);
		return instance;
	}

	public static Output getSimpleData() {
		Output out = new Output();
		List<Hypothesis> hypos = new LinkedList<Hypothesis>();
		Hypothesis hypo = new Hypothesis();
		hypos.add(hypo);
		out.setOutputHypotheses(hypos);
		return out;
	}

	public static GLDOutput getGLDOutput() {
		Output data = getSimpleData();
		GLDOutput initialData = new GLDOutput(data);
		return initialData;
	}
	
	public static GLDOutput getGLDOutputComplex() {
		Output data = getSimpleData();
		GLDOutput initialData = new GLDOutput(data);
		initialData.sColAG(getArgumentsGroupSample("col"));
		initialData.sRowAG(getArgumentsGroupSample("row"));
		return initialData;
	}
	
	public static GLDOutput getGLDOutputBaloons() {
		Output data = getBaloonsOutput();
		final GLDInput input = new GLDInput();
		input.setData(data);
		GLDConverter conv = new GLDConverter(input);
		GLDOutput initialData = conv.convert();
		return initialData;
	}

	public static ArgumentsGroup getArgumentsGroupSample(String prefix) {
		System.out.println("getCoordSequence");
		List<Argument> list = new LinkedList<Argument>();
		for (int i = 0; i < 3; ++i) {
			Argument arg = new Argument();
			arg.name = prefix + i;
			List<Value> values = new LinkedList<Value>();
			for (int j = 0; j < 3; ++j) {
				Value val = new Value("" + j);
				val.setName("value" + j);
				values.add(val);
			}
			arg.setValues(values);
			list.add(arg);
		}
		ArgumentsGroup instance = new ArgumentsGroup(list);
		return instance;
	}

	public static Output getBaloonsOutput() {
		String out = "domains\n" 
				+ "{\n" 
				+ "\tdomain1 nominal  { YELLOW, PURPLE }\n" 
				+ "\tdomain2 nominal  { SMALL, LARGE }\n" 
				+ "\tdomain3 nominal  { STRETCH, DIP }\n" 
				+ "\tdomain4 nominal  { ADULT, CHILD }\n" 
				+ "\tdomain5 nominal  { T, F }\n" 
				+ "}\n" 
				+ "attributes\n" 
				+ "{\n" 
				+ "\tattribute1 domain1 epsilon = 0.5 cost = 1\n" 
				+ "\tattribute2 domain2 epsilon = 0.5 cost = 1\n" 
				+ "\tattribute3 domain3 epsilon = 0.5 cost = 1\n" 
				+ "\tattribute4 domain4 epsilon = 0.5 cost = 1\n" 
				+ "\tattribute5 domain5 epsilon = 0.5 cost = 1\n" 
				+ "}\n" 
				+ "runs\n" 
				+ "{\n" 
				+ "   c1\n" 
				+ "{\n" 
				+ "      mode = tf\n" 
				+ "      consequent = [attribute5=*]\n" 
				+ "      ambiguity = includeinpos\n"
				+ "      trim = optimal\n" 
				+ "      compute_alternative_covers = true\n" 
				+ "      maxstar = 1\n" 
				+ "      maxrule = 10\n" 
				+ "}\n" 
				+ "}\n" 
				+ "Output_Hypotheses c1_000\n" 
				+ "{\n" 
				+ "  positive_events           = 12\n" 
				+ "  negative_events           = 8\n" 
				+ "[attribute5=T] \n" 
				+ "       # Rule 1\n" 
				+ "   <-- [attribute4=ADULT : 8,0,100%,8,0,100%]\n" 
				+ "        : p=8,np=4,u=4,cx=7,c=1,s=8 # 15\n" 
				+ "\n" 
				+ "       # Rule 2\n" 
				+ "   <-- [attribute3=STRETCH : 8,0,100%,8,0,100%]\n" 
				+ "        : p=8,np=8,enp=8,n=0,en=0,u=4,cx=7,c=1,s=8 # 16\n" 
				+ "\n" 
				+ "}\n" 
				+ "Output_Hypotheses c1_001\n" 
				+ "{\n" 
				+ "  positive_events           = 8\n" 
				+ "  negative_events           = 12\n" 
				+ "[attribute5=F] \n" 
				+ "       # Rule 1\n" 
				+ "   <-- [attribute3=DIP : 8,4,66%,8,4,66%]\n" 
				+ "       [attribute4=CHILD : 8,4,66%,8,0,100%]\n" 
				+ "        : p=8,np=8,u=8,cx=14,c=1,s=8 # 27\n" 
				+ "}";
		OutputParser parser = new OutputParser();
		Output outp = parser.parse(out);
		return outp;
	}
	
	public static Output getACOutput() {
		String out = "domains\n"+
		"{\n"+
		"c nominal  { 1, 2 }\n"+
		"x1 nominal  { 0, 1 }\n"+
		"x2 nominal  { 0, 1 }\n"+
		"x3 nominal  { 0, 1, 2 }\n"+
		"x4 nominal  { 0, 1 }\n"+
		"}\n"+
		"attributes\n"+
		"{\n"+
		"x1 x1 epsilon = 0.5 cost = 1\n"+
		"x2 x2 epsilon = 0.5 cost = 1\n"+
		"x3 x3 epsilon = 0.5 cost = 1\n"+
		"x4 x4 epsilon = 0.5 cost = 1\n"+
		"c c epsilon = 0.5 cost = 1\n"+
		"}\n"+
		"runs\n"+
		"{\n"+
		"random_seed = 975313573\n"+
		"Run1\n"+
		"{\n"+
		"consequent = [c=1]\n"+
		"learn_rules_mode = standard\n"+
		"maxstar = 2     maxrule = 5     ambiguity = IncludeInPos\n"+
		"trim = Optimal\n"+
		"exceptions = false\n"+
		"mode = tf\n"+
		"minimum_u = 1\n"+
		"optimize_ruleset = true\n"+
		"continuous_optimization_probe = 5\n"+
		"truncate = true\n"+
		"display_selectors_coverage = true\n"+
		"display_values_coverage = false\n"+
		"display_events_covered = false\n"+
		"display_alternative_covers = true\n"+
		"max_alternatives = 10\n"+
		"attribute_selection_method = none\n"+
		"handling_unknown_values = program_selected_method\n"+
		"LEF_star\n"+
		"{\n"+
		"MaxNewPositives, 0.3\n"+
		"MinNumSelectors, 0.3\n"+
		"MinComplexity, 0.3\n"+
		"MinCost, 0.3\n"+
		"}\n"+
		"LEF_partial_star\n"+
		"{\n"+
		"MaxNewPositives, 0\n"+
		"MinNumSelectors, 0\n"+
		"MinComplexity, 0\n"+
		"MinCost, 0\n"+
		"}\n"+
		"LEF_sort\n"+
		"{\n"+
		"MaxPositives, 0\n"+
		"}\n"+
		"}\n"+
		"}\n"+		
		"Output_Hypotheses Run1\n"+
		"{\n"+
		"positive_events           = 4\n"+
		"negative_events           = 2\n"+
		"[c=1]\n"+
		"<-- [x2=0 : 3,1,75%,3,1,75%]\n"+
		"[x3=0,2 : 4,1,80%,3,0,100%]\n"+
		": p=3,np=1,u=2,cx=16,c=1,s=3 # 49\n"+
		"<-- [x1=1 : 2,0,100%,2,0,100%]\n"+
		": p=2,np=1,enp=0,n=0,en=0,u=1,cx=7,c=1,s=2 # 50\n"+
		"}\n";
		OutputParser parser = new OutputParser();
		Output outp = parser.parse(out);
		return outp;
	}

	public static Output getIrisOutput() {
		Output outp = null;
        Input input = getIrisInput();
        Aq21Invoker invoker = new Aq21Invoker();
        outp = invoker.invoke(input);
		return outp;
	}

    public static Input getIrisInput() {
        try {
            OutputParser parser = new OutputParser();
            FileInputStream stream = new FileInputStream("iris.aq21");
            String out2 = Util.streamToString(stream);
            final Input input = parser.parse(out2);
            return input;
        } catch (IOException ex) {
			System.out.println("Unable to load test data from file \"iris.aq21\"");
        }
        return null;
    }
	
}
