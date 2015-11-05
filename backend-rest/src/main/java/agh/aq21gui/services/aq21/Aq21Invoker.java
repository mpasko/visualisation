/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.aq21;

import agh.aq21gui.Configuration;
import agh.aq21gui.aq21grammar.ParsingException;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.AbstractInvoker;
import agh.aq21gui.utils.Printer;
import java.io.IOException;

/**
 *
 * @author marcin
 */
public class Aq21Invoker extends AbstractInvoker{
    
    private static final String AQ21INPUT = "input.aq21";
	
	public Aq21Invoker(){
	}

    @Override
	public Output invoke(Input input) {
		if (input == null) {
			Printer.logException(this.getClass(), "AQ21 received null or empty object!", new RuntimeException());
			return null;
		}
        //System.out.println(Util.attachLines(input.toString(), this.getClass()));
		OutputParser parser = new OutputParser();
		//AttributesGroup ag = (AttributesGroup) input.gAG();

		//Logger.getLogger(Aq21Resource.class.getName()).info("Request accepted");
		String result="";
		try {
			result = run(AQ21INPUT, input.toString());
			return parser.parse(result);
        } catch (IOException ex) {
            Printer.logException(this.getClass(), "Error accessing input or output files", ex);
        } catch (ProgramExecutionException ex) {
            Printer.logException(this.getClass(), "AQ21 binary runtime failure", ex);
		} catch (ParsingException ex) {
            Printer.printLines(result, this.getClass());
            Printer.logException(this.getClass(), "Error during AQ21 result parsing", ex);
		}
		Output stub = new Output();
		stub.setRaw(result);
		return stub;
	}

    @Override
    public String[] getInputFilenames() {
        return new String[] {AQ21INPUT};
    }

    @Override
    public String getAppPath() {
        final String osName = System.getProperty("os.name");
        String path;
        if (true || osName.toLowerCase().contains("windows")) {
            path = Configuration.AQ21WINPATH;
        } else {
            path = Configuration.AQ21LINPATH;
        }
        return path;
    }
}
