package agh.aq21gui;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

public class Main {
	public static void main(String[] args) throws Exception {
		try {
                    XmlConfiguration configuration = new XmlConfiguration(Resource.newResource("jetty.xml").getInputStream());
                    Server server = (Server)configuration.configure();
                    server.start();
					ServiceStopper stopper = new ServiceStopper();
					stopper.waitForUserInput();
					server.stop();
					System.exit(0);
                    //server.join();
		} catch (Exception e) {
                    e.printStackTrace();
                    System.exit(100);
		}
	}
}
