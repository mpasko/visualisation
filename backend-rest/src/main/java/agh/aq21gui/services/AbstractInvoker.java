/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services;

import agh.aq21gui.Aq21Resource;
import agh.aq21gui.Configuration;
import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.aq21.OutputParser;
import agh.aq21gui.services.aq21.ProgramExecutionException;
import agh.aq21gui.utils.Util;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */

public abstract class AbstractInvoker {
    
    private Runtime runtime;
	static Lock lock = new ReentrantLock();
	
	public AbstractInvoker(){
		runtime = Runtime.getRuntime();
	}
	
	public String run(String params, String... inputs) throws IOException, ProgramExecutionException{
		String result="";
		lock.lock();
		try {
            String[] filenames = getInputFilenames();
            for (int i = 0; i < inputs.length; ++i) {
                FileOutputStream fos = new FileOutputStream(filenames[i]);
                fos.flush();

                Util.stringToStream(inputs[i], fos);
            }
			Process process = runtime.exec(getAppPath()+" "+params);
			InputStream stdin = process.getInputStream();


			result = Util.streamToString(stdin);

			FileOutputStream diagnostic = new FileOutputStream("output.log");
			diagnostic.flush();
			Util.stringToStream(result, diagnostic);
		} finally {
			lock.unlock();
		}
		return result;
	}
    
    public abstract String[] getInputFilenames();
    
    public abstract String getAppPath();
    
    public abstract Output invoke(Input in);
}
