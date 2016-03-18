/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services;

import agh.aq21gui.services.aq21.ProgramExecutionException;
import agh.aq21gui.utils.Util;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author marcin
 */
public class ExecInvoker {
    
	private static Lock lock = new ReentrantLock();
    private final Runtime runtime;
    
    public ExecInvoker() {
        runtime = Runtime.getRuntime();
    }

    public String run(String appPath, String params, ExecFileRequest... inputs)
        throws IOException, ProgramExecutionException 
    {
        String result = "";
        lock.lock();
        try {
            for (int i = 0; i < inputs.length; ++i) {
                FileOutputStream fos = new FileOutputStream(inputs[i].filename);
                fos.flush();
                Util.binaryToStream(inputs[i].content, fos);
            }
            Process process = runtime.exec(appPath + " " + params);
            InputStream stdin = process.getInputStream();
            InputStream stderr = process.getErrorStream();
            result = Util.streamToString(stdin)+"\n"+Util.streamToString(stderr);
            FileOutputStream diagnostic = new FileOutputStream("output.log");
            diagnostic.flush();
            Util.stringToStream(result, diagnostic);
        } finally {
            lock.unlock();
        }
        return result;
    }
    
    public String run(String appPath, String params, String filename, String input)
        throws IOException, ProgramExecutionException 
    {
        return this.run(appPath, params, new ExecFileRequest(filename, params));
    }
    
    public static class ExecFileRequest {
        public String filename;
        public String content;

        public ExecFileRequest(String filename, String data) {
            this.filename = filename;
            content = data;
        }
    }
}
