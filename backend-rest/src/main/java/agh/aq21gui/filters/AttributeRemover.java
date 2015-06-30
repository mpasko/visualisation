/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.utils.Util;
import java.util.List;

/**
 *
 * @author marcin
 */
public class AttributeRemover {
    
    public Input dropAttributes(Input in, List<String> attributes) {
        // NOW ADDED
        if (attributes.isEmpty()) {
            return in;
        }
        
        Input copy = Util.deepCopyInput(in);
        for (String attribute : attributes) {
            dropAttributeOpt(copy, attribute);
        }
        return copy;
    }
    
    public Input dropAttribute(Input in, String attribute) {
        Input copy = Util.deepCopyInput(in);
        dropAttributeOpt(copy, attribute);
        return copy;
    }

    public void dropAttributeOpt(Input copy, String attribute) {
        int index = copy.findAttributeNumber(attribute);
        if (index==-1) {
            throw new RuntimeException("Attribute name: \""+attribute+"\" not found!");
        }
        copy.gAG().attributes.remove(index);
        for (Event event : copy.obtainEventsGroup().events) {
            List<String> list = event.getValues();
            list.remove(index);
            event.setValues(list);
        }
    }
}
