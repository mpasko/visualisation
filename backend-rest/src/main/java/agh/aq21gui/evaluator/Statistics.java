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

/**
 *
 * @author marcin
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
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
    
    public double getTrue() {
        return trueNegative+truePositive;
    }
    
    public double getFalse() {
        return falseNegative+falsePositive;
    }
    
    private double getP() {
        return falseNegative+truePositive;
    }
    
    private double getN() {
        return trueNegative+falsePositive; 
    }
    
    public double getSensitivity() {
        return truePositive/getP();
    }
    
    public double getSpecificity() {
        return trueNegative/getN();
    }
    
    public double getPrecision() {
        return truePositive/(truePositive+falsePositive);
    }
    
    public double getNegativePredictiveValue() {
        return trueNegative/(trueNegative+falsePositive);
    }
    
    public double getFallOut() {
        return falsePositive/getN();
    }
    
    public double getFalseNegativeRate() {
        return falseNegative/getP();
    }
    
    public double getFalseDiscoveryRate() {
        return falsePositive/(truePositive+falsePositive);
    }
    
    public double getAccuracy() {
        return getTrue()/(getAll());
    }

    public double getAll() {
        return getP()+getN();
    }
    
    public double getF1Score() {
        int _2tp = 2*truePositive;
        return _2tp/(_2tp+falsePositive+falseNegative);
    }
}
