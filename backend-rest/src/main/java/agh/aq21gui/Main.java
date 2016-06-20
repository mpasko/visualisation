package agh.aq21gui;

import agh.adidata.scripts.AllRunner;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.Util;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
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
        
        options.addOption("T", "all-tests", false, "Run all test scripts");
        options.addOption("H", "humanize", false, "Convert rules back to human readable format");
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
        } else if (cmd.hasOption("all-tests")) {
            AllRunner.runAll(cmd.hasOption("humanize"));
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

    private static void printAsJsonString(Output sample) {
        String out = Util.objectToJson(sample);
        System.out.println(out);
    }
}
