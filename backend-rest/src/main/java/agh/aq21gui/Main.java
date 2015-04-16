package agh.aq21gui;

import agh.aq21gui.model.output.Output;
import agh.aq21gui.stubs.StubFactory;
import java.io.IOException;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

public class Main {
	public static void main(String[] args) throws Exception {
        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("I", "iris-sample", false, "Iris sample output");
        options.addOption("A", "ac-sample", false, "Ac sample output");
        options.addOption("B", "baloons-sample", false, "Baloons sample output");
        options.addOption("S", "simple-sample", false, "Simple sample output");
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("iris-sample")) {
            printAsJsonString(StubFactory.getIrisOutput());
            return;
        } else if (cmd.hasOption("ac-sample")) {
            printAsJsonString(StubFactory.getACOutput());
            return;
        } else if (cmd.hasOption("baloons-sample")) {
            printAsJsonString(StubFactory.getBaloonsOutput());
            return;
        } else if (cmd.hasOption("simple-sample")) {
            printAsJsonString(StubFactory.getSimpleData());
            return;
        }
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

    private static void printAsJsonString(Output sample) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String out = mapper.writeValueAsString(sample);
        System.out.println(out);
    }
}
