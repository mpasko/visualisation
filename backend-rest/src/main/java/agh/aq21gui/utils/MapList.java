/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class MapList <T>{
    private Map<String, LinkedList<T>> map = new HashMap<String, LinkedList<T>>();
    
    public LinkedList<T> get(String key) {
        insertEmptyIfNull(key);
        return map.get(key);
    }
    
    public void add(String key, T value) {
        insertEmptyIfNull(key);
        map.get(key).add(value);
    }

    private void insertEmptyIfNull(String key) {
        if (map.get(key)==null){
            map.put(key, new LinkedList<T>());
        }
    }

    public Iterable<String> keySet() {
        return map.keySet();
    }
}
