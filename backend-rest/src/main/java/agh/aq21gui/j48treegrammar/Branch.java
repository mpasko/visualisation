/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.j48treegrammar;

/**
 *
 * @author marcin
 */
public class Branch {
    private String from;
    private String to;
    private String attribute;
    private String comparator;
    private String value;

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the comparator
     */
    public String getComparator() {
        return comparator;
    }

    /**
     * @param comparator the comparator to set
     */
    public void setComparator(String comparator) {
        this.comparator = comparator;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        StringBuilder bbuild = new StringBuilder();
        bbuild.append(this.getFrom()).append(" to ").append(this.getTo()).append(" is ");
        bbuild.append(this.getAttribute()).append(this.getComparator()).append(this.getValue());
        bbuild.append("\n");
        String branchstring = bbuild.toString();
        return branchstring;
    }
}
