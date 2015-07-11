/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.csv;

import agh.aq21gui.model.input.Domain;
import agh.aq21gui.utils.NumericUtil;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
class PredictedDomain {
    public DomainType type;
    public List<String> values;
    public Double max;
    public Double min;
    public int number;
    private final CSVConverter domainPredictor;
    
    public enum DomainType {

        CONTINUOUS("continuous"),
        INTEGER("integer"),
        LINEAR("linear"),
        NOMINAL("nominal");
        public String value;

        private DomainType(String value) {
            this.value = value;
        }
    }

    public PredictedDomain(int id, final CSVConverter domainPredictor) {
        this.domainPredictor = domainPredictor;
        number = id;
        type = DomainType.INTEGER;
        values = new LinkedList<String>();
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
    }

    public void VerifyNew(String value) {
        switch (type) {
            case INTEGER:
                if (!NumericUtil.isInteger(value)) {
                    type = DomainType.CONTINUOUS;
                } else if (!NumericUtil.isNumber(value)) {
                    type = DomainType.NOMINAL;
                }
            case CONTINUOUS:
                if (!NumericUtil.isNumber(value)) {
                    type = DomainType.NOMINAL;
                }
                break;
            case NOMINAL:
                break;
        }
        if (!NumericUtil.isWildcard(value)) {
            if (NumericUtil.isNumber(value)) {
                Double val = Double.parseDouble(value);
                if (val > max) {
                    max = val;
                }
                if (val < min) {
                    min = val;
                }
            }
            if (!values.contains(value)) {
                values.add(value);
            }
        }
    }

    public Domain generate() {
        Domain dom = new Domain();
        dom.setdomain(type.value);
        dom.setname("domain" + number);
        if (min>max) {
            double tmp = min;
            min = max;
            max = tmp;
        }
        switch (type) {
            case INTEGER:
                if (min<=Double.MIN_VALUE) {
                    min = (double)Integer.MIN_VALUE;
                }
                if (max>=Double.MAX_VALUE) {
                    max = (double)Integer.MAX_VALUE;
                }
            case CONTINUOUS:
                dom.setRange(min, max);
                break;
            case NOMINAL:
                if (this.values.size() < 2) {
                    this.values.add("undefined");
                }
                dom.setRange(this.values);
                break;
        }
        return dom;
    }

    public double getMean() {
        return (min + max) / 2.0;
    }
    
    public boolean isContinuous(){
        return this.type == DomainType.CONTINUOUS;
    }
    
    public boolean isInteger(){
        return this.type == DomainType.INTEGER;
    }
}
