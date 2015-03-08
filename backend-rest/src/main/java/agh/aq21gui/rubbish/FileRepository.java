/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.rubbish;

import agh.aq21gui.services.aq21.Invoker;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.management.Directory;
import agh.aq21gui.model.management.InputPair;
import agh.aq21gui.model.management.OutputPair;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.aq21.OutputParser;
import agh.aq21gui.utils.Util;
import dataaccess.Repository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class FileRepository extends Repository{
	
	private final FilenameFilter resultsFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".res");
		}
	};
	
	private final FilenameFilter experimentsFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".exp");
		}
	};
	
	private File folder;
	
	public FileRepository(){
		folder = new File("C:\\aq21repository\\");
	}

	@Override
	public Directory getDirectory() throws Exception {
		ArrayList<String> names = new ArrayList<String>(Arrays.asList(folder.list()));
		Directory dir = new Directory();
		for (String name : folder.list(experimentsFilter)){
			dir.putExperiment(name, getExperiment(name));		
		}
		for (String name : folder.list(resultsFilter)){
			dir.putExperiment(name, getExperiment(name));		
		}
		return dir;
	}

	@Override
	public void saveExperiment(InputPair experiment) {
		final Output out = (Output) experiment.getValue();
		saveInFile(out, experiment.getName()+".exp");
	}

	@Override
	public void saveResult(OutputPair result) {
		final Output out = result.getValue();
		saveInFile(out, result.getName()+".exp");
	}

	@Override
	public Input getExperiment(String name) {
		return readFromFile(name, ".exp");
	}

	@Override
	public Output getResult(String name) {
		return readFromFile(name, ".exp");
	}

	@Override
	public void onStop() {
		
	}

	public Output readFromFile(String name, String extension) {
		FileInputStream fis = null;
		Output exp = null;
		try {
			fis = new FileInputStream("C:\\aq21repository\\"+name+extension);
			OutputParser parser = new OutputParser();
			exp = parser.parse(Util.streamToString(fis));
		} catch (IOException ex) {
			Logger.getLogger(FileRepository.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
				Logger.getLogger(FileRepository.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return exp;
	}

	public void saveInFile(final Output out, String name) {
		FileOutputStream fos = null;
		String content = out.toString();
		try {
			fos = new FileOutputStream("C:\\aq21repository\\"+name);
			Util.stringToStream(content, fos);
		} catch (IOException ex) {
			Logger.getLogger(FileRepository.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				fos.close();
			} catch (IOException ex) {
				Logger.getLogger(FileRepository.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public void dropDataBase() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
