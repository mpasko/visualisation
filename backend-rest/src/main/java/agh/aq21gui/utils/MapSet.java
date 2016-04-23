/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

/**
 *
 * @author marcin
 */
public class MapSet <T>{
    private Map<String, TreeSet<T>> map = new HashMap<String, TreeSet<T>>();
    
    public TreeSet<T> get(String key) {
        insertEmptyIfNull(key);
        return map.get(key);
    }
    
    public void add(String key, T value) {
        insertEmptyIfNull(key);
        map.get(key).add(value);
    }

    private void insertEmptyIfNull(String key) {
        if (map.get(key)==null){
            map.put(key, new TreeSet<T>());
        }
    }

    public Iterable<String> keySet() {
        return map.keySet();
    }

    public Iterable<Entry<String, TreeSet<T>>> entrySet() {
        return this.map.entrySet();
    }
}
