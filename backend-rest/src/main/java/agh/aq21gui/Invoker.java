/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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
			out.append(line);
		}
		return out.toString();
	}
	
	public static void stringToStream(String in, OutputStream out) throws IOException{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
		bw.write(in);
		bw.close();
	}
	
	public String invoke(String input) throws IOException, ProgramExecutionException{
		FileOutputStream fos = new FileOutputStream("input.aq21");
		fos.flush();
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
		return streamToString(stdin);
	}
}
