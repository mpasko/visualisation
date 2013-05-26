
package agh.aq21gui;

import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;


public class Main {

    public static final URI BASE_URI = UriBuilder.fromUri("http://localhost/").port(9998).build();

    protected static SelectorThread startServer() throws IOException {
        final Map<String, String> initParams = new HashMap<String, String>();

        initParams.put("com.sun.jersey.config.property.packages", 
                "agh.aq21gui");
		initParams.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");

        System.out.println("Starting grizzly...");
        SelectorThread threadSelector = GrizzlyWebContainerFactory.create(BASE_URI, initParams);
		
        return threadSelector;
    }
	
    
    public static void main(String[] args) throws IOException {		
//        SelectorThread threadSelector = startServer();
		
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sjersey/application.wadl\nHit enter to stop it...",
                BASE_URI));
		
		GrizzlyWebServer server = new GrizzlyWebServer(9998);
		server.getSelectorThread().getKeepAliveStats().disable();
		
		ServletAdapter staticAdapter = new ServletAdapter();
		staticAdapter.setProperty( "load-on-startup", 1 );
		staticAdapter.setContextPath("/");
		staticAdapter.addRootFolder("/");
		final String webadapter = "/web";
		staticAdapter.setHandleStaticResources(true);
//		adapter.setServletInstance(new ServletTest(webadapter));
		staticAdapter.setContextPath(webadapter);
		server.addGrizzlyAdapter(staticAdapter, new String[]{webadapter});
		
		ServletAdapter serviceAdapter = new ServletAdapter();
		serviceAdapter.setServletInstance(new ServletContainer());
		//serviceAdapter.setProperty("javax.ws.rs.Application", "org.antares.rs.JerseyAdaptor");
		serviceAdapter.addInitParameter("com.sun.jersey.config.property.packages", "agh.aq21gui");
		serviceAdapter.addInitParameter(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
		final String jerseyadapter = "/jersey";
		serviceAdapter.setContextPath(jerseyadapter);
		server.addGrizzlyAdapter(serviceAdapter, new String[]{jerseyadapter});
		server.getSelectorThread().setKeepAliveTimeoutInSeconds(0);
        server.start();
		
        System.in.read();
		server.stop();
//        threadSelector.stopEndpoint();
    }    

	private static class ServletTest extends javax.servlet.http.HttpServlet {
		public ServletTest(String webadapter) {
		}
		
		@Override
		public void doGet(HttpServletRequest request, HttpServletResponse response){
			
		}
	}
}
