/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.utils.TreeNode;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Attribute extends Domain{

    public Attribute(){}
    
    public Attribute(String name, String value) {
        super(name,value);
    }

	void parseAttribute(TreeNode node) {
		parseDomain(node);
	}
}
