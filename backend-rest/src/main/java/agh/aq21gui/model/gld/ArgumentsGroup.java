/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import java.util.List;

/**
 *
 * @author marcin
 */
public class ArgumentsGroup {

	private List<Argument> arguments;
	
	ArgumentsGroup(List<Argument> list) {
		this.arguments = list;
	}
	
	public List<Argument> getArguments(){
		return arguments;
	}

	void print() {
		for(Argument arg : arguments){
			arg.print();
			System.out.print(", ");
		}
		System.out.println();
	}
}
