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
import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class OrientDBRepository extends Repository {
	
	private OrientDBServerStarter starter;
	
	public OrientDBRepository () throws Exception{
		
		starter = new OrientDBServerStarter();
		starter.start();
		//sessionFactory=null;
		//this.starter = starter;
	}
	
	public void generateSchemaForAll(OObjectDatabaseTx db){
		/*
		Reflections ref = new Reflections("agh.aq21gui.model.input");
		Set<Class<? extends IAQ21Serializable>> classes = ref.getSubTypesOf(IAQ21Serializable.class);
		for (Class<? extends IAQ21Serializable> claz : classes){
			
		}
		*/ 
		// REGISTER THE CLASS ONLY ONCE AFTER THE DB IS OPEN/CREATED
		db.getEntityManager().registerEntityClasses("agh.aq21gui.model.input");
		db.getEntityManager().registerEntityClasses("agh.aq21gui.model.output");
		db.getEntityManager().registerEntityClasses("agh.aq21gui.model.management");
		
		/*
		int experiments_CLID=db.getClusterIdByName("Experiments");
		if(experiments_CLID==-1){
			db.addCluster("Experiments", OStorage.CLUSTER_TYPE.PHYSICAL);
		}
		OClass schema = db.getMetadata().getSchema().getClass(Input.class);
		if(schema.getDefaultClusterId()!=experiments_CLID){
			schema.addClusterId(experiments_CLID);
			schema.setDefaultClusterId(experiments_CLID);
		}
		*/ 
		db.getMetadata().getSchema().reload();
		db.setLazyLoading(false);
		//db.setRetainObjects(false);
	}
	
	
	@Override
	public Directory getDirectory() throws Exception{
		OObjectDatabaseTx db= OObjectDatabasePool.global().acquire("remote:localhost/aq21db", "root", "ala123");
  
		generateSchemaForAll(db);
		
		//db.getEntityManager().registerEntityClass(InputPair.class);
		Directory dir = new Directory();
		try {
			//Logger.getLogger("database").info("before browseClass");
			for (InputPair experiment : db.browseClass(InputPair.class).setFetchPlan("*:-1")){
				/*
				if(experiment == null){
					Logger.getLogger("database").severe("Error: experiment is null!");
				}else if(experiment.getValue()==null){
					Logger.getLogger("database").severe("Error: experiment.value is null!");
				}else{ */
					experiment=db.detachAll(experiment, true);
					dir.getExperiments().add(experiment);
					/*
					Logger.getLogger("database").info("new experiment added:");
					System.out.println(experiment.toString());
				}
				*/ 
			}
			for(OutputPair result : db.browseClass(OutputPair.class).setFetchPlan("*:-1")){
				if(result == null){
					Logger.getLogger("database").severe("Error: experiment is null!");
				}else{
					result = db.detachAll(result, true);
					dir.getResults().add(result);
				}
			}
		} catch(Exception e) {
			//Logger.getLogger("database").severe(e.getMessage());
			throw e;
		} finally {
			db.close();
			//Logger.getLogger("database").info("database successfully closed");
		}
		//db.detachAll(dir, true);
		dir.traverse();
		return dir;
	}
	
	@Override
	public void saveExperiment(InputPair experiment){
		OObjectDatabaseTx db= OObjectDatabasePool.global().acquire("remote:localhost/aq21db", "root", "ala123");
  
		generateSchemaForAll(db);
  
		try {
			boolean exists=false;
			for (InputPair exp : db.browseClass(InputPair.class).setFetchPlan("*:-1")){
				if(exp.getName().equals(experiment.getName())){
					exp.setValue(experiment.getValue());
					db.save(exp);
					exists=true;
				}
			}
			if(!exists){
				db.save(experiment);
			}
		} finally {
			db.close();
		}
	}
	
	@Override
	public void saveResult(OutputPair result){
		OObjectDatabaseTx db= OObjectDatabasePool.global().acquire("remote:localhost/aq21db", "root", "ala123");
		generateSchemaForAll(db);
  
		try {
			boolean exists=false;
			for (OutputPair res : db.browseClass(OutputPair.class).setFetchPlan("*:-1")){
				if(res.getName().equals(result.getName())){
					res.setValue(result.getValue());
					db.save(res);
					exists=true;
				}
			}
			if(!exists){
				db.save(result);
			}
		} finally {
			db.close();
		}
	}
	
	@Override
	public void onStop(){
		this.starter.stop();
	}

	@Override
	public Input getExperiment(String name) {
		OObjectDatabaseTx db= OObjectDatabasePool.global().acquire("remote:localhost/aq21db", "root", "ala123");
		generateSchemaForAll(db);
		Input value = null;
		for (InputPair exp : db.browseClass(InputPair.class).setFetchPlan("*:-1")){
				if(exp.getName().equals(name)){
					value=exp.getValue();
					value = db.detachAll(value, true);
				}
		}
		return value;
	}

	@Override
	public Output getResult(String name) {
		OObjectDatabaseTx db= OObjectDatabasePool.global().acquire("remote:localhost/aq21db", "root", "ala123");
		generateSchemaForAll(db);
		Output value = null;
		for (OutputPair exp : db.browseClass(OutputPair.class).setFetchPlan("*:-1")){
				if(exp.getName().equals(name)){
					value=exp.getValue();
					value = db.detachAll(value, true);
				}
		}
		return value;
	}
}
