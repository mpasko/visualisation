/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.TreeNode;
import java.util.LinkedList;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
//@XmlRootElement
public class Event {
	public LinkedList<String> values;
	
	public Event(){
		values = new LinkedList<String>();
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<values.size(); ++i){
			String value = values.get(i);
			builder.append(value);
			if(i<values.size()-1){
				builder.append(", ");
			}else{
				builder.append('\n');
			}
		}
		return builder.toString();
	}

	void parseRow(TreeNode eventNode) {
		for(Object obj : eventNode.tree().getChildren()){
			CommonTree cell = (CommonTree) obj;
			values.add(cell.getText());
		}
	}
}
