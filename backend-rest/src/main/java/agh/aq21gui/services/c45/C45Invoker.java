/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.c45;

import agh.aq21gui.Configuration;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.AbstractInvoker;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class C45Invoker extends AbstractInvoker{

    @Override
    public String[] getInputFilenames() {
        return new String[] {"input.names", "input.data"};
    }

    @Override
    public String getAppPath() {
        return Configuration.C45PATH;
    }
    
    public String formatAttributes(Input in, Test t) {
        String clss = t.grepClassName();
        StringBuilder builder = new StringBuilder();
        ClassDescriptor desc = t.grepClassDescriptor();
        formatAttribute(builder, clss, desc.getName(), desc.getSet_elements());
        //Attribute classAttr = null;
        for (Attribute attr: in.getAttributes()) {
            if (attr.name.equalsIgnoreCase(clss)) {
                //classAttr = attr;
            } else {
                builder.append(attr.name).append(": ");
                final Domain domain = attr.getdomainObjectRecursively(in.obtainDomainsGroup());
                formatAttribute(builder, attr.name, domain.name, domain.getRange());
            }
        }
        return builder.toString();
    }

    public Output invokeSingle(Input in, Test t) {
        String attributes = formatAttributes(in,t);
        String data = "";
        String params = "";
        String result = "";
        data = in.obtainEventsGroup().toString();
        try {
            result = run(params, attributes, data);
        } catch (Exception ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, ex);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Output invoke(Input in) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void formatAttribute(StringBuilder builder, String name, String domain, List<String> range) {
        builder.append(name);
        builder.append(": ");
        if (domain.equalsIgnoreCase("continuous")){
            builder.append("continuous");
        } else {
            int i = 0;
            for (String rangeItem : range) {
                if (i > 0) {
                    builder.append(rangeItem);
                }
                ++i;
            }
        }
        builder.append("\n");
    }
    
}
