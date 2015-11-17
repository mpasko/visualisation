/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.services.aq21.OutputParser;
import agh.aq21gui.utils.TreeNode;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Selector extends ClassDescriptor{
    public static Selector parse(String string) throws RecognitionException {
        String selectorString = bracketify(string).replaceAll("!=", "<>");
        TParser tokens = new OutputParser().prepareParser(selectorString);
        CommonTree cd_tree = (CommonTree) tokens.selector().getTree();
        TreeNode node = new TreeNode(cd_tree, TParser.SELECTOR);
        Selector cd = new Selector();
        cd.parseSelector(node);
        return cd;
    }
    
    public Selector() {
        super();
    }
    
    public Selector(String name, String comparator, String value) {
        super(name, comparator, value);
    }
    
    public Selector(String name, List<String> elems) {
        super(name, elems);
    }
}
