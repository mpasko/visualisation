/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class ServiceStopper {
	
	public static Thread waiter;
	public static boolean webStopped = false;
	public static Lock lock = new ReentrantLock();
		
	public String stop(){
		Logger.getLogger("ServiceStopper").info("Trying to stop service by website...");
		try{
			lock.lock();
			webStopped = true;
		}finally{
			lock.unlock();
		}
		return "";
	}
	
	public void waitForUserInput(){
		try {
			boolean expr = true;
			while(expr){
				Thread.sleep(2000);
				try{
					lock.lock();
					expr = (!webStopped) && (System.in.available()==0);
				}finally{	
					lock.unlock();
				}
			}
		}catch (IOException ex) {
			Logger.getLogger(ServiceStopper.class.getName()).log(Level.SEVERE, null, ex);
		}catch (InterruptedException ex) {
			Logger.getLogger(ServiceStopper.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
