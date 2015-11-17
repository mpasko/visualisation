/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.NameValueEntity;
import agh.aq21gui.services.aq21.OutputParser;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.TreeNode;
import agh.aq21gui.utils.Util;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class ClassDescriptor extends NameValueEntity {

    public String comparator = "";
    private String value = "";
    public String range_begin = "";
    public String range_end = "";

    public ClassDescriptor(String name, String comparator, String value) {
        this.name = name.toLowerCase();
        this.comparator = comparator;
        this.value = value.toLowerCase();
        if (this.value.contains("..")) {
            String[] strings = this.value.split("\\.\\.");
            this.range_begin = strings[0];
            this.range_end = strings[1];
        } else if (this.value.contains(",")) {
            for (String elem : this.value.split(",")) {
                this.set_elements.add(elem.toLowerCase());
            }
        }
    }

    public ClassDescriptor() {
    }

    public ClassDescriptor(String name, List<String> elems) {
        this.name = name.toLowerCase();
        this.comparator = "=";
        this.set_elements = new LinkedList<String>();
        for (String elem : elems) {
            this.set_elements.add(elem.toLowerCase());
        }
    }

    @XmlElement(name = "comparator")
    public void setComparator(String comp) {
        this.comparator = comp;
    }

    public String getComparator() {
        return this.comparator;
    }

    @XmlElement(name = "value")
    public void setValue(String val) {
        if (!val.isEmpty()) {
            this.value = val;
            if (val.contains(",")) {
                String trimmed = val.replaceAll(" ", "");
                if (trimmed.contains("{")) {
                    this.range_begin = trimmed.split(",")[0];
                    this.range_end = trimmed.split(",")[1];
                } else {
                    this.set_elements = Arrays.asList(trimmed.split(","));
                }
            } else {
                this.range_begin = "";
                this.set_elements = new LinkedList<String>();
            }
        }
    }

    public String getValue() {
        StringBuilder b = new StringBuilder();
        b.append(setToString());
        if (!range_begin.isEmpty()) {
            if (set_elements.size() >= 1) {
                b.append(",");
            }
            b.append(rangeToString());
        }
        if (b.length()>0) {
            this.value = b.toString();
        }
        return this.value;
    }

    @XmlElement(name = "range_begin")
    public void setRange_begin(String r_b) {
        this.range_begin = r_b;
    }

    public String getRange_begin() {
        return this.range_begin;
    }

    @XmlElement(name = "range_end")
    public void setRange_end(String r_e) {
        this.range_end = r_e;
    }

    public String getRange_end() {
        return this.range_end;
    }

    @XmlElement(name = "set_elements")
    public List<String> getSet_elements() {
        return set_elements;
    }

    public void setSet_elements(List<String> values) {
        this.set_elements = new LinkedList<String>(values);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(name);
        builder.append(comparator);
        builder.append(getValue());
        builder.append("]");
        return builder.toString();
    }

    public void parseSelector(TreeNode desc) {
        name = desc.childAt(0, TParser.ID).value();
        comparator = desc.childAt(1, TParser.EQUAL).value();
        if (desc.childCount() >= 3) {
            /*
            if (desc.tree().getChild(2).getType() == TParser.RANGE) {
                TreeNode range = desc.childAt(2, TParser.RANGE);
                range_begin = range.childAt(0, TreeNode.ANY_TYPE).value();
                range_end = range.childAt(1, TreeNode.ANY_TYPE).value();
                rangeToString();
            } else if (desc.tree().getChild(2).getType() == TParser.VALUE_SET) {
                //Logger.getLogger("Parser").info("Parsing value set");
                TreeNode set = desc.childAt(2, TParser.VALUE_SET);
                set_elements = new LinkedList<String>();
                for (TreeNode itemNode : set.iterator(TreeNode.ANY_TYPE)) {
                    set_elements.add(itemNode.value());
                }
                setToString();
            } else {
                value = desc.childAt(2, TreeNode.ANY_TYPE).value();
            }
            */
            if (desc.tree().getChild(2).getType() == TParser.VALUE_SET) {
                TreeNode node = desc.childAt(2, TParser.VALUE_SET);
                LinkedList<String> temporary = new LinkedList<String>();
                boolean rangeOpUsed = false;
                for (int i=0; i<node.childCount(); ++i) {
                    if (node.tree().getChild(i).getType() == TParser.RANGE) {
                        rangeOpUsed = true;
                    } else {
                        temporary.addLast(node.childAt(i, TreeNode.ANY_TYPE).value());
                    }
                }
                if (rangeOpUsed) {
                    range_end = temporary.removeLast();
                    range_begin = temporary.removeLast();
                } else if (temporary.size()==1) {
                    value = temporary.get(0); 
                }
                set_elements = new LinkedList<String>(temporary);
            } else {
                value = desc.childAt(2, TreeNode.ANY_TYPE).value();
            }
        }
    }

    public static ClassDescriptor parse(String string) throws RecognitionException {
        String cdString = bracketify(string).replaceAll("!=", "<>");
        TParser tokens = new OutputParser().prepareParser(cdString);
        CommonTree cd_tree = (CommonTree) tokens.class_description().getTree();
        ClassDescriptor cd = new ClassDescriptor();
        if (!string.replaceAll("\\w", "").equalsIgnoreCase("[]")) {
            TreeNode node = new TreeNode(cd_tree, TParser.CLASS_DESCRIPTION);
            cd.parseSelector(node);
        }
        return cd;
    }

    public boolean contains(ClassDescriptor other) {
        boolean outcome = false;
        if (equals(other)) {
            outcome = true;
        } else if (this.comparator.equals("=")) {
            outcome = false;
        }
        //TODO
        return outcome;
    }

    @Override
    public boolean equals(Object next) {
        boolean outcome;
        if (next == null) {
            outcome = false;
        } else if (next instanceof ClassDescriptor) {
            ClassDescriptor other = (ClassDescriptor) next;
            outcome = true;
            if (!this.name.equalsIgnoreCase(other.name)) {
                outcome = false;
            } else if (!this.comparator.equalsIgnoreCase(other.comparator)) {
                outcome = false;
            } else if (!this.getValue().equalsIgnoreCase(other.getValue())) {
                outcome = false;
            }
        } else {
            outcome = false;
        }
        return outcome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 67 * hash + (this.comparator != null ? this.comparator.hashCode() : 0);
        hash = 67 * hash + (this.getValue() != null ? this.value.hashCode() : 0);
        return hash;
    }

    private String setToString() {
        StringBuilder build = new StringBuilder();
        for (String element : set_elements) {
            if (build.length() > 0) {
                build.append(",");
            }
            build.append(element);
        }
        return build.toString();
    }

    private String rangeToString() {
        StringBuilder builder = new StringBuilder();
        builder.append(range_begin);
        builder.append("..");
        builder.append(range_end);
        return builder.toString();
    }

    public boolean matchesValue(String actualValue, List<String> linearOrder) {
//        if (NumericUtil.isWildcard(actualValue)) {
//            return false;
//        }
        boolean outcome = false;
        if (this.hasRange()||this.hasSet()) {
            if (this.hasRange()) {
                outcome |= matchesRange(actualValue, linearOrder);
            }
            if (this.hasSet()) {
                outcome |= matchesSet(actualValue);
            }
        } else {
            //Descriptor has often format i.e. colour>0.5
            //so right is always a value contained in ClassDescriptor
            double right = NumericUtil.tryParse(this.getValue());
            double left = NumericUtil.tryParse(actualValue);
            final boolean textFields = Double.isNaN(right) || Double.isNaN(left);
            if (textFields) {
                outcome = matchesTextValue(actualValue);
            } else {
                outcome = matchesDoubleValue(left, right);
            }
        }
        return outcome;
    }

    private double parseDouble(String value) throws RuntimeException {
        double right = NumericUtil.tryParse(value);
        if (Double.isNaN(right)) {
            throw new RuntimeException("Comparison is only supported for numeric values, found:" + value);
        }
        return right;
    }

    private double getDoubleValueOf(String str, List<String> linearOrder) {
        String string_value = str;
        if (NumericUtil.isNumber(string_value)) {  
            return NumericUtil.tryParse(getValue());
        } else {
            return Util.indexOfIgnoreCase(linearOrder, string_value);
        }
    }

    public double getDoubleValue(List<String> linearOrder) {
        return getDoubleValueOf(getValue(), linearOrder);
    }

    public boolean hasRange() {
        return this.range_end!=null && !this.range_end.isEmpty();
    }

    public boolean hasSet() {
        return this.set_elements!=null && this.set_elements.size()>1;
    }

    private boolean matchesRange(String actualValue, List<String> linearOrder) throws RuntimeException {
        double begin = getDoubleValueOf(range_begin, linearOrder);
        double end = getDoubleValueOf(range_end, linearOrder);
        double actual = getDoubleValueOf(actualValue, linearOrder);
        final boolean isBetween = ((begin <= actual) && (actual <= end)) || ((end <= actual) && (actual <= begin));
        boolean result = false;
        if (comparator.equals("=")) {
            result = isBetween;
        } else if (comparatorIsNonequality()) {
            result = !isBetween;
        }
        return result;
    }

    private boolean matchesSet(String actualValue) {
        boolean matches_any = false;
        for (String elem : set_elements) {
            boolean matches = elem.equalsIgnoreCase(actualValue);
            matches_any |= matches;
        }
        boolean result = false;
        if (comparator.equals("=")) {
            result = matches_any;
        } else if (comparatorIsNonequality()) {
            result = !matches_any;
        }
        return result;
    }

    private boolean matchesTextValue(String actualValue) throws RuntimeException {
        boolean result;
        result = false;
        if (NumericUtil.isWildcard(actualValue)) {
            result = true;
        } else if (comparator.equals("=")) {
            result = this.getValue().equalsIgnoreCase(actualValue);
        } else if (comparatorIsNonequality()) {
            result = !this.getValue().equalsIgnoreCase(actualValue);
        } else {
            final String messageFormat = "Comparison using %s is only supported for numeric values. Found:%s and:%s";
            final String message = String.format(messageFormat, comparator, this.getValue(), actualValue);
            throw new RuntimeException(message);
        }
        return result;
    }

    private boolean matchesDoubleValue(double left, double right) {
        boolean result;
        result = false;
        if (comparator.equals("<")) {
            result = left < right;
        } else if (comparator.equals(">")) {
            result = left > right;
        } else if (comparator.equals(">=")) {
            result = left >= right;
        } else if (comparator.equals("<=")) {
            result = left <= right;
        } else if (comparatorIsNonequality()) {
            result = Math.abs(right - left) > NumericUtil.EPSILON;
        } else if (comparator.equals("=")) {
            result = Math.abs(right - left) < NumericUtil.EPSILON;
        }
        return result;
    }

    @JsonIgnore
    public boolean isCustomValue() {
        return getValue().equalsIgnoreCase("*");
    }

    public boolean isGeneralizationOf(ClassDescriptor other, List<String> linearOrder) {
        boolean result;
        if (this.equals(other)) {
            result = true;
        } else {
            result = false;
            String comp1 = this.getComparator();
            String comp2 = other.getComparator();
            String val1 = this.getValue();
            String val2 = other.getValue();
            char dir1 = comp1.charAt(0);
            char dir2 = comp2.charAt(0);
            Double doubleval1 = NumericUtil.tryParse(val1);
            Double doubleval2 = NumericUtil.tryParse(val2);
            if (this.getRange_begin().isEmpty()) {
                if (dir1 == dir2) {
                    result = this.matchesValue(val2, linearOrder);
                } else {
                    result |= comp1.equals(">=") && comp2.equals("=") && (doubleval2 >= doubleval1);
                    result |= comp1.equals("<=") && comp2.equals("=") && (doubleval2 <= doubleval1);
                    result |= comparatorIsNonequality() && comp2.equals("<") && (doubleval2 <= doubleval1);
                    result |= comparatorIsNonequality() && comp2.equals(">") && (doubleval2 >= doubleval1);
                }
            } else {
                if (other.getRange_begin().isEmpty()) {
                    if (comp2.equals("=")) {
                        result = this.matchesRange(val2, linearOrder);
                    }
                } else {
                    result |= this.matchesRange(other.getRange_begin(), linearOrder);
                    result &= this.matchesRange(other.getRange_end(), linearOrder);
                }
            }
        }
        return result;
    }

    public boolean comparatorIsNonequality() {
        return comparator.equals("!=") || comparator.equals("<>");
    }

    public boolean matchesValueStrictly(String actualValue, List<String> linearOrder) {
        if (NumericUtil.isWildcard(actualValue)) {
            if (("?".equals(value)) && ("?".equals(actualValue)) && ("=".equals(comparator))) {
                return true;
            }
            return false;
        }
        return matchesValue(actualValue, linearOrder);
    }
}
