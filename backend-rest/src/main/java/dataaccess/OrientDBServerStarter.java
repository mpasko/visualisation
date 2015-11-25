/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import agh.aq21gui.Configuration;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import java.io.File;
import java.util.Locale;

/**
 *
 * @author marcin
 */
public class OrientDBServerStarter {
	OServer server;
	
	public void start() throws Exception{
		server = OServerMain.create();
		server.startup(new File(getPath()));
		server.activate();
	}
	
	public void stop(){
		server.shutdown();
		server = null;
	}

    private String getPath() {
        final String osName = System.getProperty("os.name");
        String path;
        if (osName.toLowerCase(Locale.US).contains("windows")) {
            path = "orientdb.win.config";
        } else {
            path = "orientdb.lin.config";
        }
        return path;
    }
}
