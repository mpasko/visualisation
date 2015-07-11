/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.aq21;

import agh.aq21gui.Aq21Resource;
import agh.aq21gui.Configuration;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.AbstractInvoker;
import agh.aq21gui.utils.Util;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        //System.out.println(Util.attachLines(input.toString())+"\n----------------------------");
		if (input == null) {
			Logger.getLogger(Aq21Resource.class.getName()).warning("AQ21 received null or empty object!");
			return null;
		}
		OutputParser parser = new OutputParser();
		//AttributesGroup ag = (AttributesGroup) input.gAG();

		//Logger.getLogger(Aq21Resource.class.getName()).info("Request accepted");
		String result="";
		try {
			result = run(AQ21INPUT, input.toString());
			return parser.parse(result);
		} catch (Exception ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, ex);
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
        return Configuration.AQ21PATH;
    }
}
