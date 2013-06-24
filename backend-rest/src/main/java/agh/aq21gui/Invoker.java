/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.utils.OutputParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
	
	public static String streamToString(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			out.append(line).append("\n");
		}
		return out.toString();
	}
	
	public static void stringToStream(String in, OutputStream out) throws IOException{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
		for(String line : in.split("\n")){
			bw.write(line);
			bw.newLine();
		}
		bw.close();
	}
	
	public String run(String input) throws IOException, ProgramExecutionException{
		FileOutputStream fos = new FileOutputStream("input.aq21");
		fos.flush();
//		input = input.replace("\n", "\n\r");
		stringToStream(input, fos);
		Process process = runtime.exec(Configuration.AQ21PATH+" input.aq21");
		InputStream stdin = process.getInputStream();
//		OutputStream stdout = process.getOutputStream();
//		InputStream stderr = process.getErrorStream();
//		stringToStream(input, stdout);
//		String errorMessage = streamToString(stderr);
//		if(!errorMessage.isEmpty()){
//			throw new ProgramExecutionException(errorMessage);
//		}
		String result = streamToString(stdin);
		System.out.println("AQ21 Program Result is:");
		System.out.println(result);
		return result;
	}

	public Output invoke(Input input) {
		if (input == null) {
			Logger.getLogger(Aq21Resource.class.getName()).warning("AQ21 received null or empty object!");
			return null;
		}
		OutputParser parser = new OutputParser();
		AttributesGroup ag = (AttributesGroup) input.gAG();
		if (ag.attributes.get(0).name.equals("blabla")) {
			Logger.getLogger(Aq21Resource.class.getName()).info("Special case: 'blabla' attribute!");
			return parser.parse("blabla");
		}
		Logger.getLogger(Aq21Resource.class.getName()).info("Request accepted");
		String result;
		try {
			result = run(input.toString());
			return parser.parse(result);
		} catch (IOException ex) {
			Logger.getLogger(Aq21Resource.class.getName()).log(Level.WARNING, null, ex);
		} catch (ProgramExecutionException ex) {
			Logger.getLogger(Aq21Resource.class.getName()).log(Level.WARNING, null, ex);
		}
		return null;
	}
}
