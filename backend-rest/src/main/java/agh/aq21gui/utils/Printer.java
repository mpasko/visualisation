/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class Printer {
    
    private static Lock lock = new ReentrantLock();
    
    public static String attachLines(String in, Class<?> invokedIn) {
        StringBuilder b = new StringBuilder();
        int i=0;
        b.append("Raised from: ").append(invokedIn.getCanonicalName()).append("\n");
        for (String line : in.split("\n")) {
            b.append(++i);
            b.append("\t");
            b.append(line);
            b.append("\n");
        }
        return b.toString();
    }
    
    public static void printLines(String text, Class<?> invoker) {
        try {
            lock.lock();
            System.out.println(attachLines(text, invoker));
        } finally {
            lock.unlock();
        }
    }
    
    public static void logException(Class<?> invoker, String message, Throwable exception) {
        try {
            lock.lock();
            Logger.getLogger(invoker.getName()).log(Level.SEVERE, message, exception);
        } finally {
            lock.unlock();
        }
    }
}
