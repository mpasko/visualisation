/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.converters;

import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.utils.NumericUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import weka.core.DenseInstance;
import weka.core.Instances;

/**
 *
 * @author marcin
 */
public class Aq21InputToWeka {

    public Instances aq21ToWeka(Input input) {
        ArrayList<agh.aq21gui.model.input.Attribute> aq21Attrs = new ArrayList<Attribute>(input.getAttributes());
        ArrayList<weka.core.Attribute> wekaAttrs = convertAttributes(aq21Attrs, input);
        final List<Map<String, Object>> aq21Events = input.getEvents();
        Instances instances = new Instances("test", wekaAttrs, aq21Events.size());
        for (Event aq21Event : input.gEG().events) {
            convertEvent(aq21Event, aq21Attrs, wekaAttrs, instances);
        }
        return instances;
    }

    void convertEvent(Event aq21Event, ArrayList<agh.aq21gui.model.input.Attribute> aq21Attrs, ArrayList<weka.core.Attribute> wekaAttrs, Instances instances) {
        final List<String> eventValues = aq21Event.getValues();
        int num = eventValues.size();
        final DenseInstance denseInstance = new DenseInstance(num);
        for (int index = 0; index < num; ++index) {
            final Attribute aq21Attr = aq21Attrs.get(index);
            String name = aq21Attr.name;
            String value = eventValues.get(index);
            weka.core.Attribute wekaAttr = wekaAttrs.get(index);
            trySetValue(value, denseInstance, wekaAttr, name);
        }
        instances.add(denseInstance);
    }

    ArrayList<weka.core.Attribute> convertAttributes(List<agh.aq21gui.model.input.Attribute> aq21Attrs, Input input) {
        ArrayList<weka.core.Attribute> wekaAttrs = new ArrayList<weka.core.Attribute>();
        int serial = 0;
        for (Attribute aq21Attr : aq21Attrs) {
            weka.core.Attribute wekaAttr;
            final String domain = aq21Attr.getdomainNameRecursively(input.gDG());
            if (domain.equalsIgnoreCase("nominal") || domain.equalsIgnoreCase("linear")) {
                List<String> elems = aq21Attr.getRangeRecursively(input.gDG());
                wekaAttr = new weka.core.Attribute(aq21Attr.getName(), elems, serial);
            } else {
                wekaAttr = new weka.core.Attribute(aq21Attr.getName());
            }
            wekaAttrs.add(wekaAttr);
            serial++;
        }
        return wekaAttrs;
    }

    private void trySetValue(String value, final DenseInstance denseInstance, weka.core.Attribute wekaAttr, String name) {
        if (!NumericUtil.isWildcard(value)){
            try {
                double doubleValue = NumericUtil.tryParse(value);
                if (!Double.isNaN(doubleValue)) {
                    denseInstance.setValue(wekaAttr, doubleValue);
                } else {
                    denseInstance.setValue(wekaAttr, value);
                }
            } catch (Exception ex) {
                System.out.println("name=" + name + ", value=" + value + " wekaattr=" + wekaAttr.type());
                ex.printStackTrace();
            }
        }
    }
    
}
