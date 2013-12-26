/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.utils.OutputParser;
import agh.aq21gui.utils.Util;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class Invoker {
	
	Runtime runtime;
	
	public Invoker(){
		runtime = Runtime.getRuntime();
	}
	
	public String run(String input) throws IOException, ProgramExecutionException{
		FileOutputStream fos = new FileOutputStream("input.aq21");
		fos.flush();
		
		Util.stringToStream(input, fos);
		Process process = runtime.exec(Configuration.AQ21PATH+" input.aq21");
		InputStream stdin = process.getInputStream();

		String result = Util.streamToString(stdin);
		
		FileOutputStream diagnostic = new FileOutputStream("output.log");
		diagnostic.flush();
		Util.stringToStream(result, diagnostic);
		return result;
	}

	public Output invoke(Input input) {
		if (input == null) {
			Logger.getLogger(Aq21Resource.class.getName()).warning("AQ21 received null or empty object!");
			return null;
		}
		OutputParser parser = new OutputParser();
		AttributesGroup ag = (AttributesGroup) input.gAG();

		Logger.getLogger(Aq21Resource.class.getName()).info("Request accepted");
		String result="";
		try {
			result = run(input.toString());
			return parser.parse(result);
		} catch (Exception ex) {
			Logger.getLogger(Aq21Resource.class.getName()).log(Level.WARNING, null, ex);
		}
		Output stub = new Output();
		stub.setRaw(result);
		return stub;
	}
}
