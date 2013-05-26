
package agh.aq21gui;

import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.OutputHypotheses;
import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import javax.ws.rs.core.MediaType;
import junit.framework.TestCase;


public class Aq21ResourceTest extends TestCase {

    private SelectorThread threadSelector;
    
    private WebResource r;

    public Aq21ResourceTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        threadSelector = Main.startServer();

        Client c = Client.create();
        r = c.resource(Main.BASE_URI);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        threadSelector.stopEndpoint();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    public void testAq21Resource() {
		Input input = new Input();
		input.addAttribute("blabla", "", "");
		Builder builder = r.path("aq21").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		Output responseMsg = builder.post(Output.class,input);
		OutputHypotheses oh = responseMsg.outputHypotheses.get(0);
        assertEquals("blabla", oh.content);
    }
	
	public void testJSON() {
		/* now unnecessary */
	}
}
