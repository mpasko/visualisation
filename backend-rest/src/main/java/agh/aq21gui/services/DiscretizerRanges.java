/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services;

import agh.aq21gui.utils.NumericUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class DiscretizerRanges {
    public List<Double> values = new ArrayList<Double>();
    public List<String> labels = new ArrayList<String>();
    public String attribute = "";
    
    public DiscretizerRanges(String attr_name, Double...values){
        this.attribute = attr_name;
        this.values = Arrays.asList(values);
    }
    
    public DiscretizerRanges(String attr_name, Collection<Double> values){
        this.attribute = attr_name;
        this.values = new LinkedList<Double>(values);
    }

    public DiscretizerRanges(String attr_name, Integer...values) {
        this.attribute = attr_name;
        this.values = NumericUtil.intListToDoubleList(Arrays.asList(values));
    }
}
