/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.services.aq21.OutputParser;
import agh.aq21gui.utils.TreeNode;
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
public class Parameter implements IAQ21Serializable {

    public String name;
    public String value;
    public long id;
    private String parent = "global";
    private static Long generator;

    static {
        generator = new Long(0);
    }
    private List<ClassDescriptor> descriptors = new LinkedList<ClassDescriptor>();

    @XmlElement(name = "id")
    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    @XmlElement(name = "name")
    public void setName(String nm) {
        this.name = nm;
    }

    public String getName() {
        return this.name;
    }

    @XmlElement(name = "value")
    public void setValue(String val) {
        this.value = val;
        try {
            if (val.contains("[") || val.contains("]")) {
                this.descriptors = parseDescriptors(val);
            }
        } catch (RecognitionException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getValue() {
        return this.value;
    }

    @XmlElement(name = "parent")
    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Parameter() {
        this.id = generator;
        generator++;
    }

    public Parameter(String parent) {
        this();
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(" = ").append(value).append('\n');
        return builder.toString();
    }

    public List<ClassDescriptor> getDescriptors() {
        if (this.descriptors.isEmpty()) {
            try {
                final OutputParser outputParser = new OutputParser();
                CommonTree tree = (CommonTree) outputParser.prepareParser(value).value().getTree();
                parseValue(tree);
            } catch (Throwable ex) {
            }
        }
        return this.descriptors;
    }

    void parseParam(TreeNode paramNode) {
        name = paramNode.childAt(0, TParser.ID).value();
        CommonTree paramTree = (CommonTree) paramNode.tree();
        if (paramTree.getChildCount() == 1) {
            return;
        }
        CommonTree valueNode = (CommonTree) paramTree.getChild(1);
        parseValue(valueNode);
    }

    void traverse() {
        if (id == -1) {
            id -= 1;
        }
    }

    private void parseValue(CommonTree valueNode) {
        if (valueNode.getType() == TParser.VALUE) {
            value = valueNode.getChild(0).getText();
        }
        if (valueNode.getType() == TParser.CLASSES) {
            StringBuilder builder = new StringBuilder();
            TreeNode classes = new TreeNode(valueNode, TParser.CLASSES);
            for (TreeNode desc : classes.iterator(TParser.CLASS_DESCRIPTION)) {
                ClassDescriptor descriptor = new ClassDescriptor();
                descriptor.parseSelector(desc);
                this.descriptors.add(descriptor);
                builder.append(descriptor.toString());
            }
            value = builder.toString();
        }
    }

    public void setDescriptors(List<ClassDescriptor> descriptors) {
        if (descriptors.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (ClassDescriptor descriptor : descriptors) {
                builder.append(descriptor.toString());
            }
            this.value = builder.toString();
            this.descriptors = descriptors;
        }
    }

    @JsonIgnore
    public boolean isTrue() {
        return this.value.equalsIgnoreCase("true");
    }

    public List<ClassDescriptor> parseDescriptors(String val) throws RecognitionException {
        LinkedList<ClassDescriptor> descs = new LinkedList<ClassDescriptor>();
        if (val.length() > 0) {
            String[] strings = val.split("(?<=\\])(?=\\[)");
            for (String str : strings) {
                if (!str.replaceAll("\\w", "").equals("[]")) {
                    descs.add(ClassDescriptor.parse(str));
                }
            }
        }
        return descs;
    }
}
