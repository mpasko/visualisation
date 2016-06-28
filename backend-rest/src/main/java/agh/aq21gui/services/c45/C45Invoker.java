/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.c45;

import agh.aq21gui.Configuration;
import agh.aq21gui.converters.J48TreeToRuleSet;
import agh.aq21gui.j48treegrammar.J48Tree;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Parameter;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.AbstractInvoker;
import agh.aq21gui.services.ExecInvoker;
import agh.aq21gui.services.ExecInvoker.ExecFileRequest;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.Printer;
import agh.aq21gui.utils.Util;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class C45Invoker implements AbstractInvoker{
    
    private static String LINE_SEPARATOR = String.valueOf((char)10);

    @Override
    public String getAppPath() {
        return Configuration.C45PATH;
    }

    public Output invokeSingle(Input input, Test t) {
        String clss = t.grepClassName();
        Input in = swapConsequentToTheEnd(input, clss);
        boolean prune = false;
        Parameter param = t.findParam("prune");
        if (param != null) {
            prune = (param.isTrue());
        }
        String names = formatAttributes(in, clss);
        String params = "-f input";
        String data = formatData(in.getEvents(), in.getAttributes(), clss);
        String result = "";
        try {
            ExecFileRequest datafile = new ExecInvoker.ExecFileRequest("input.data", data);
            ExecFileRequest namesfile = new ExecInvoker.ExecFileRequest("input.names", names);
            result = new ExecInvoker().run(getAppPath(), params, datafile, namesfile);
            Printer.printLines(result, this.getClass());
        } catch (Throwable ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, ex);
        }
        String inputFilename = prune?"input.tree":"input.unpruned";
        J48Tree tree = new C45BinTreeReader(in, t).getTree(inputFilename);
        List<Hypothesis> hypothesis = new J48TreeToRuleSet().treeToRules(tree, t.grepClassName());
        Output out = new Output();
        out.setOutputHypotheses(hypothesis);
        out.setRaw(result);
        return out;
    }

    @Override
    public Output invoke(Input in) {
        Output result = new Output(in);
        result.setOutputHypotheses(new LinkedList<Hypothesis>());
        for (Test run : in.runsGroup.runs){
            Output partialResult = invokeSingle(in, run);
            result.merge(partialResult);
        }
        return result;
    }
    
    public String formatAttributes(Input in, String className) {
        StringBuilder builder = new StringBuilder();
        Domain classDomain = in.findDomainObjectRecursively(className);
        formatAttribute(builder, classDomain, classDomain.getRange());
        for (Attribute attr: in.getAttributes()) {
            String name = attr.name;
            if (!name.equalsIgnoreCase(className)) {
                final Domain domain = in.findDomainObjectRecursively(name);
                builder.append(name).append(": ");
                formatAttribute(builder, domain, domain.getRange());
            }
        }
        return builder.toString();
    }

    private void formatAttribute(StringBuilder builder, Domain domain, List<String> range) {
        if (domain.isContinuous()||domain.isInteger()){
            builder.append("continuous");
        } else {
            int i = 0;
            for (String rangeItem : range) {
                if (i > 0) {
                    builder.append(", ");
                }
                ++i;
                builder.append(rangeItem);
            }
        }
        builder.append('.').append(LINE_SEPARATOR);
    }

    private String formatData(List<Map<String, Object>> events, List<Attribute> attributes, String className) {
        StringBuilder build = new StringBuilder();
        for (Map<String, Object> event : events) {
            for (Attribute attr : attributes) {
                String name = attr.getName();
                boolean not_class = !name.equalsIgnoreCase(className);
                if (not_class) {
                    build.append(event.get(name));
                    build.append(", ");
                }
            }
            /*
            for (Entry<String, Object> entry : event.entrySet()) {
                boolean not_class = !entry.getKey().equalsIgnoreCase(className);
                boolean not_id = !entry.getKey().equalsIgnoreCase("id");
                if (not_class && not_id) {
                    build.append(entry.getValue());
                    build.append(", ");
                }
            }
            */
            build.append(event.get(className));
            build.append(LINE_SEPARATOR);
        }
        return build.toString();
    }
    
    public static void main(String args[]) {
        Output out = new C45Invoker().invoke(StubFactory.loadAdiUpdatedData());
        Printer.printLines(out.toString(), C45Invoker.class);
    }

    private Input swapConsequentToTheEnd(Input input, String clas) {
        Input in = Util.deepCopyInput(input);
        Attribute clsAttr = in.findAttribute(clas);
        List<Attribute> attributes = in.gAG().attributes;
        int prev_index = attributes.indexOf(clsAttr);
        //int new_index = attributes.size()-1;
        attributes.remove(clsAttr);
        in.gAG().attributes.add(clsAttr);
        for (Event event: in.obtainEventsGroup().events) {
            List<String> values = event.getValues();
            String old = values.remove(prev_index);
            values.add(old);
        }
        return in;
    }
}
