/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.converters;

import agh.aq21gui.j48treegrammar.Branch;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author marcin
 */
public class PathsAggregate {
    private LinkedList<Entry<String, TreePath>> entries;
    private TreeMap<String,LinkedList<TreePath>> map;
    
    public PathsAggregate(){
        entries = new LinkedList<Entry<String,TreePath>>();
        map = new TreeMap<String, LinkedList<TreePath>>();
    }

    public void put(TreePath path) {
        entries.add(new AbstractMap.SimpleEntry<String, TreePath>(path.claz, path));
        LinkedList<TreePath> list = getPathList(path.claz);
        list.add(path);
    }

    void printPaths() {
        for (Entry<String, TreePath> entry : this.entrySet()) {
            TreePath path = entry.getValue();
            String claz = entry.getKey();
            System.out.println("Path:\nclass=" + claz + "\n");
            for (Branch branch : path.path) {
                System.out.println(branch.toString());
            }
        }
    }

    /**
     * @return the paths
     */
    public LinkedList<Entry<String, TreePath>> entrySet() {
        return entries;
    }

    public LinkedList<TreePath> getPathList(String claz) {
        LinkedList<TreePath> list = map.get(claz);
        if (list==null) {
            list = new LinkedList<TreePath>();
            map.put(claz,list);
        }
        return list;
    }
    
    public List<String> getClasses(){
        LinkedList<String> list = new LinkedList<String>(map.keySet());
        return list;
    }
    
}
