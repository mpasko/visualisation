/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.management.Directory;
import agh.aq21gui.model.management.InputPair;
import agh.aq21gui.model.management.OutputPair;
import agh.aq21gui.model.output.Output;
//import org.hibernate.SessionFactory;

/**
 *
 * @author marcin
 */
public abstract class Repository {
	
	
	private static Repository __sole_instance = null;
	
	public static Repository getRepository() throws Exception{
		return getRepository("aq21db");
	}

	static Repository getRepository(String dbname) throws Exception{
		if(__sole_instance == null){
			__sole_instance = new OrientDBRepository(dbname);
		}
		return __sole_instance;
	}
		
	public abstract Directory getDirectory() throws Exception;
	
	public abstract void saveExperiment(InputPair experiment);
	
	public abstract void saveResult(OutputPair result);
	
	public abstract Input getExperiment(String name);
	
	public abstract Output getResult(String name);
	
	public abstract void onStop();
	
	public abstract void dropDataBase() throws Exception;
}
