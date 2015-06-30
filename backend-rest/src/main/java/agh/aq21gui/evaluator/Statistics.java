/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.evaluator;

import agh.aq21gui.utils.Util;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 *
 * @author marcin
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonPropertyOrder(
        { "truePositive", 
        "trueNegative", 
        "falsePositive",
        "falseNegative",
        "all",
        "accuracy",
        "precision",
        "sensitivity",
        "specificity",
        "negativePredictiveValue",
        "falseNegativeRate",
        "falseDiscoveryRate",
        "fallOut",
        "f1Score"})
public class Statistics {
    private int truePositive=0;
    private int trueNegative=0;
    private int falsePositive=0;
    private int falseNegative=0;

    /**
     * @return the truePositive
     */
    @XmlElement(name="true_positive")
    public int getTruePositive() {
        return truePositive;
    }

    /**
     * @param truePositive the truePositive to set
     */
    public void setTruePositive(int truePositive) {
        this.truePositive = truePositive;
    }

    /**
     * @return the trueNegative
     */
    @XmlElement(name="true_negative")
    public int getTrueNegative() {
        return trueNegative;
    }

    /**
     * @param trueNegative the trueNegative to set
     */
    public void setTrueNegative(int trueNegative) {
        this.trueNegative = trueNegative;
    }

    /**
     * @return the falsePositive
     */
    @XmlElement(name="false_positive")
    public int getFalsePositive() {
        return falsePositive;
    }

    /**
     * @param falsePositive the falsePositive to set
     */
    public void setFalsePositive(int falsePositive) {
        this.falsePositive = falsePositive;
    }

    /**
     * @return the falseNegative
     */
    @XmlElement(name="false_negative")
    public int getFalseNegative() {
        return falseNegative;
    }

    /**
     * @param falseNegative the falseNegative to set
     */
    public void setFalseNegative(int falseNegative) {
        this.falseNegative = falseNegative;
    }

    public void increment(Statistics stats) {
        this.falseNegative+=stats.falseNegative;
        this.falsePositive+=stats.falsePositive;
        this.trueNegative+=stats.trueNegative;
        this.truePositive+=stats.truePositive;
    }

    void analyzeCase(boolean premiseMatches, boolean theoryMatches) {
        if (premiseMatches && theoryMatches) {
            ++truePositive;
        } else if (premiseMatches && !theoryMatches) {
            ++falsePositive;
        } else if (!premiseMatches && !theoryMatches) {
            ++trueNegative;
        } else if (!premiseMatches && theoryMatches) {
            ++falseNegative;
        }
    }
    
    @Override
    public String toString() {
        return Util.objectToJson(this);
    }
    
    private double getTrue() {
        return trueNegative+truePositive;
    }
    
    private double getFalse() {
        return falseNegative+falsePositive;
    }
    
    private double getP() {
        return falseNegative+truePositive;
    }
    
    private double getN() {
        return trueNegative+falsePositive; 
    }
    
    public double getSensitivity() {
        return safeDivide(truePositive, getP());
    }
    
    public double getSpecificity() {
        return safeDivide(trueNegative, getN());
    }
    
    public double getPrecision() {
        return safeDivide(truePositive, truePositive+falsePositive);
    }
    
    public double getNegativePredictiveValue() {
        return safeDivide(trueNegative, trueNegative+falsePositive);
    }
    
    public double getFallOut() {
        return safeDivide(falsePositive, getN());
    }
    
    public double getFalseNegativeRate() {
        return safeDivide(falseNegative, getP());
    }
    
    public double getFalseDiscoveryRate() {
        return safeDivide(falsePositive, truePositive+falsePositive);
    }
    
    public double getAccuracy() {
        return safeDivide(getTrue(), getAll());
    }

    public double getAll() {
        return getP()+getN();
    }
    
    public double getF1Score() {
        int _2tp = 2*truePositive;
        return safeDivide(_2tp, _2tp+falsePositive+falseNegative);
    }

    public static double safeDivide(double a, double b) {
        if (b==0.0 || Double.isNaN(b)) {
            return Double.NaN;
        } else {
            return a/b;
        }
    }
}
