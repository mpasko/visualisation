/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input.events;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class Generator {
    private static Map<String, Long> generators = new HashMap<String, Long>();
    
    public static long getNext(){
        return getNext("default");
    }

    public static long getNext(String scope) {
        if (generators.containsKey(scope)) {
            Long val = generators.get(scope)+1;
            generators.put(scope, val);
            return val;
        } else {
            generators.put(scope, 0L);
            return 0L;
        }
    }
    
    public static Long getNextObject(){
        return getNext();
    }
}
