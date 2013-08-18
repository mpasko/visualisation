package agh.aq21gui;

//import com.sun.grizzly.http.embed.GrizzlyWebServer;
//import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;
//import com.sun.jersey.spi.container.servlet.ServletContainer;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;
import org.eclipse.jetty.server.Handler;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

	public static final URI BASE_URI = UriBuilder.fromUri("http://localhost/").port(9998).build();

	/*
	 protected static SelectorThread startServer() throws IOException {
	 final Map<String, String> initParams = new HashMap<String, String>();

	 initParams.put("com.sun.jersey.config.property.packages", 
	 "agh.aq21gui");
	 initParams.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");

	 System.out.println("Starting grizzly...");
	 SelectorThread threadSelector = GrizzlyWebContainerFactory.create(BASE_URI, initParams);
		
	 return threadSelector;
	 }
	 */
	final static String webadapter = "/web";
	final static String jerseyadapter = "/jersey";
	/*
	 public static void main(String[] args) throws IOException, InterruptedException {		
	 //        SelectorThread threadSelector = startServer();
		
	 System.out.println(String.format("Jersey app started with WADL available at "
	 + "%sjersey/application.wadl\nHit enter to stop it...",
	 BASE_URI));
		
	 //		GrizzlyWebServer server = new GrizzlyWebServer(9998);
	 //		server.getSelectorThread().getKeepAliveStats().disable();
		
	 //		ServletAdapter staticAdapter = new ServletAdapter();
	 //		staticAdapter.setProperty( "load-on-startup", 0 );
	 //		staticAdapter.setContextPath("/");
	 //		staticAdapter.addRootFolder("/");
		
	 //		staticAdapter.setHandleStaticResources(true);
	 //		adapter.setServletInstance(new ServletTest(webadapter));
	 //		staticAdapter.setContextPath(webadapter);
	 //		server.addGrizzlyAdapter(staticAdapter, new String[]{webadapter});
		
	 //		ServletAdapter serviceAdapter = new ServletAdapter();
	 //		serviceAdapter.setServletInstance(new ServletContainer());
	 //serviceAdapter.setProperty("javax.ws.rs.Application", "org.antares.rs.JerseyAdaptor");
	 //		serviceAdapter.addInitParameter("com.sun.jersey.config.property.packages", "agh.aq21gui");
	 //		serviceAdapter.addInitParameter(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
	 //		serviceAdapter.setProperty("load-on-startup", 1 );
		
	 //		serviceAdapter.setContextPath(jerseyadapter);
	 //		server.addGrizzlyAdapter(serviceAdapter, new String[]{jerseyadapter});
	 //		server.getSelectorThread().setKeepAliveTimeoutInSeconds(0);
	 //      server.start();
		
	 URI uri= UriBuilder.fromUri("http://localhost/").port(9998).path(webadapter).path("frontend/index.html").build();
	 Thread.sleep(1000);
	 //		Desktop.getDesktop().browse(uri);
	 ServiceStopper stopper = new ServiceStopper();
	 stopper.waitForUserInput();
	 //		server.stop();
	 //        threadSelector.stopEndpoint();
	 }    
	 */
	final static String webappDirLocation = "/";

	public static void main(String[] args) throws Exception {
		try {
			Server server = new Server(9998);

			/*      WebAppContext root = new WebAppContext(".", ".");
			 root.setServer(server);
			 root.setContextPath(".");
			 root.setDescriptor(webappDirLocation+"/WEB-INF/web.xml");
			 root.setResourceBase(webappDirLocation);

			 root.setParentLoaderPriority(true);
			 */
			Handler def = new ContextHandler("/");

			ResourceHandler resource = new ResourceHandler();
			resource.setWelcomeFiles(new String[]{"/frontend/index.html"});
			resource.setResourceBase(".");
			ContextHandler web = new ContextHandler("/web");
			web.setHandler(resource);

			ServletContextHandler servlet = new ServletContextHandler();
			servlet.setContextPath("/jersey");
			Map<String, Object> initMap = new HashMap<String, Object>();
			initMap.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
			initMap.put("com.sun.jersey.config.property.packages", "agh.aq21gui");
			servlet.addServlet(new ServletHolder(new ServletContainer(new PackagesResourceConfig(initMap))), "/*");

			ContextHandler rest = new ContextHandler("/");
			rest.setHandler(servlet);

			ContextHandlerCollection handlers = new ContextHandlerCollection();
			handlers.setHandlers(new Handler[]{servlet,web});
		//	handlers.setParallelStart(true);
			server.setHandler(handlers);

			server.start();
			System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
			System.err.println(server.dump());
			URI uri = UriBuilder.fromUri("http://localhost/").port(9998).path(webadapter).path("frontend/index.html").build();
			Thread.sleep(1000);
//			Desktop.getDesktop().browse(uri);
			ServiceStopper stopper = new ServiceStopper();
			stopper.waitForUserInput();
			server.stop();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}

	private static class ServletTest extends javax.servlet.http.HttpServlet {

		public ServletTest(String webadapter) {
		}

		@Override
		public void doGet(HttpServletRequest request, HttpServletResponse response) {
		}
	}
}
