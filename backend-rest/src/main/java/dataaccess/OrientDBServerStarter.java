/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import java.io.File;

/**
 *
 * @author marcin
 */
public class OrientDBServerStarter {
	OServer server;
	
	public void start() throws Exception{
		server = OServerMain.create();
		server.startup(new File("orientdb.config"));
		server.activate();
	}
	
	public void stop(){
		server.shutdown();
	}
}
