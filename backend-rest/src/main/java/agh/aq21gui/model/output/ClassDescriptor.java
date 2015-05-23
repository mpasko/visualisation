/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.model.input.NameValueEntity;
import agh.aq21gui.services.aq21.OutputParser;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.TreeNode;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class ClassDescriptor extends NameValueEntity{
	public String comparator="";
	private String value="";
	public String range_begin="";
	public String range_end="";
    
    public ClassDescriptor(String name, String comparator, String value) {
		this.name = name.toLowerCase();
		this.comparator = comparator;
		this.value = value.toLowerCase();
    }

    public ClassDescriptor() {
        
    }
    
    public ClassDescriptor(String name, List<String> elems){
		this.name = name.toLowerCase();
		this.comparator = "=";
		this.set_elements = new LinkedList<String>();
        for (String elem : elems) {
            this.set_elements.add(elem.toLowerCase());
        }
    }
	
	@XmlElement(name="comparator")
	public void setComparator(String comp){
		this.comparator = comp;
	}
	
	public String getComparator(){
		return this.comparator; 
	}
	
	@XmlElement(name="value")
	public void setValue(String val){
		if (!val.isEmpty()){
			this.value = val;
			this.range_begin="";
			this.set_elements=new LinkedList<String>();
		}
	}
	
	public String getValue(){
		if (value.isEmpty() && (set_elements.size()>=1) ) {
			setToString();
		}
		if (value.isEmpty() && (!range_begin.isEmpty()) ) {
			rangeToString();
		}
		return this.value; 
	}
	
	@XmlElement(name="range_begin")
	public void setRange_begin(String r_b){
		this.range_begin = r_b;
	}
	
	public String getRange_begin(){
		return this.range_begin; 
	}
	
	@XmlElement(name="range_end")
	public void setRange_end(String r_e){
		this.range_end = r_e;
	}
	
	public String getRange_end(){
		return this.range_end; 
	}
	
	@XmlElement(name="set_elements")
	public List<String> getSet_elements(){
		return set_elements;
	}
	
	public void setSet_elements(List<String> values){
		this.set_elements = new LinkedList<String>(values);
	}
	
	@Override
	public String toString(){
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
		if(desc.tree().getChild(2).getType()==TParser.RANGE){
			TreeNode range = desc.childAt(2, TParser.RANGE);
			range_begin = range.childAt(0, TreeNode.ANY_TYPE).value();
			range_end = range.childAt(1, TreeNode.ANY_TYPE).value();
			rangeToString();
		}else if(desc.tree().getChild(2).getType()==TParser.VALUE_SET){
			//Logger.getLogger("Parser").info("Parsing value set");
			TreeNode set = desc.childAt(2, TParser.VALUE_SET);
			set_elements = new LinkedList<String>();
			for(TreeNode itemNode : set.iterator(TreeNode.ANY_TYPE)){
				set_elements.add(itemNode.value());
			}
			setToString();
		}else{
			value = desc.childAt(2, TreeNode.ANY_TYPE).value();
		}
	}
    
    public static ClassDescriptor parse(String string) throws RecognitionException {
        String cdString = bracketify(string);
        TParser tokens = new OutputParser().prepareParser(cdString);
        CommonTree cd_tree = (CommonTree) tokens.class_description().getTree();
        TreeNode node = new TreeNode(cd_tree, TParser.CLASS_DESCRIPTION);
        ClassDescriptor cd = new ClassDescriptor();
        cd.parseSelector(node);
        return cd;
    }

	public boolean contains(ClassDescriptor other) {
        boolean outcome = false;
		if (equals(other)){
			outcome = true;
		} else if (this.comparator.equals("=")){
			outcome = false;
		}
		//TODO
		return outcome;
	}
	
	@Override
	public boolean equals(Object next){
        boolean outcome = false;
		if (next==null){
			outcome = false;
		} else if (next instanceof ClassDescriptor) {
			ClassDescriptor other = (ClassDescriptor)next;
            outcome = true;
			if (!this.name.equalsIgnoreCase(other.name)){
				outcome = false;
			} else if (!this.comparator.equalsIgnoreCase(other.comparator)){
				outcome = false;
			} else if (!this.getValue().equalsIgnoreCase(other.getValue())){
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
	
	private void setToString(){
		StringBuilder build = new StringBuilder();
		for (String element:set_elements) {
			if (build.length()>0) {
				build.append(",");
			}
			build.append(element);
		}
		value = build.toString();
	}

	private void rangeToString() {
		StringBuilder builder = new StringBuilder();
		builder.append(range_begin);
		builder.append("..");
		builder.append(range_end);
		value = builder.toString();
	}

    public boolean matchesValue(String actualValue) {
        boolean outcome = false;
        if (value.isEmpty()) {
            if (!range_begin.isEmpty()) {
                outcome = matchesRange(actualValue);
            } else if (this.set_elements.size()>=1) {
                outcome = matchesSet(actualValue);
            }
        } else {
            //Descriptor has often format i.e. colour>0.5
            //so right is always a value contained in ClassDescriptor
            double right = NumericUtil.tryParse(this.getValue());
            double left = NumericUtil.tryParse(actualValue);
            final boolean textFields = Double.isNaN(right)||Double.isNaN(left);
            final boolean comaratorIsNonequality = comparator.equals("!=") || comparator.equals("<>");
            if (textFields) {
                outcome = matchesTextValue(actualValue, comaratorIsNonequality);
            } else {
                outcome = matchesDoubleValue(left, right, comaratorIsNonequality);
            }
        }
        return outcome;
    }

    private double parseDouble(String value) throws RuntimeException {
        double right = NumericUtil.tryParse(value);
        if (Double.isNaN(right)) {
            throw new RuntimeException("Comparison is only supported for numeric values, found:"+value);
        }
        return right;
    }

    private boolean matchesRange(String actualValue) throws RuntimeException {
        double begin = parseDouble(range_begin);
        double end = parseDouble(range_end);
        double actual = parseDouble(actualValue);
        final boolean isBetween = ((begin<actual)&&(actual<end)) || ((end<actual)&&(actual<begin));
        boolean result = false;
        if (comparator.equals("=")) {
            result = isBetween;
        } else if (comparator.equals("!=") || comparator.equals("<>")) {
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
        } else if (comparator.equals("!=") || comparator.equals("<>")) {
            result = !matches_any;
        }
        return result;
    }

    private boolean matchesTextValue(String actualValue, final boolean comaratorIsNonequality) throws RuntimeException {
        boolean result;
        result = false;
        if (NumericUtil.isWildcard(actualValue)) {
            result = true;
        } else if (comparator.equals("=")) {
            result = this.getValue().equalsIgnoreCase(actualValue);
        } else if (comaratorIsNonequality) {
            result = !this.getValue().equalsIgnoreCase(actualValue);
        } else  {
            final String messageFormat = "Comparison using %s is only supported for numeric values. Found:%s and:%s";
            final String message = String.format(messageFormat, comparator, this.getValue(), actualValue);
            throw new RuntimeException(message);
        }
        return result;
    }

    private boolean matchesDoubleValue(double left, double right, final boolean comaratorIsNonequality) {
        boolean result;
        result = false;
        if (comparator.equals("<")) {
            result = left<right;
        } else if (comparator.equals(">")) {
            result = left>right;
        } else if (comparator.equals(">=")) {
            result = left>=right;
        } else if (comparator.equals("<=")) {
            result = left<=right;
        } else if (comaratorIsNonequality) {
            result = Math.abs(right-left)>NumericUtil.EPSILON;
        } else if (comparator.equals("=")) {
            result = Math.abs(right-left)<NumericUtil.EPSILON;
        }
        return result;
    }

    public boolean isCustomValue() {
        return getValue().equalsIgnoreCase("*");
    }
}
