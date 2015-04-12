/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.TreeNode;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlTransient
public class AttributesGroup implements IAQ21Serializable{
	public List<Attribute> attributes;
	protected String LABEL = "Domains";
	protected int CHILD_TOKEN = TParser.DOMAIN;
	
	public AttributesGroup(){
		LABEL = "Attributes";
		CHILD_TOKEN = TParser.ATTRIBUTE;
		attributes = new LinkedList<Attribute>();
	}
	
	@Override
	public String toString(){
		if(attributes.isEmpty()){
			return "";
		}
		StringBuilder builder = FormatterUtil.begin(LABEL);
		FormatterUtil.appendAll(builder, attributes, 1);
		return FormatterUtil.terminate(builder);
	}

	public void parseAttributes(TreeNode treeNode) {
		for(TreeNode attrTree: treeNode.iterator(CHILD_TOKEN)){
			Attribute attribute = new Attribute();
			attribute.parseAttribute(attrTree);
			this.attributes.add(attribute);
		}
	}

	void traverse() {
		for(Attribute a : attributes){
			a.traverse();
		}
	}

    public int getIndexOfAttribute(String name) {
        int i=0;
        for (Attribute attr: this.attributes) {
            if (attr.name.equalsIgnoreCase(name)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public Attribute getAttributeByName(String name) {
        int index = getIndexOfAttribute(name);
        return this.attributes.get(index);
    }

    public void replaceAttributeDomain(String name, Domain triggered) {
        this.getAttributeByName(name).setdomain(triggered.getName());
    }
}
