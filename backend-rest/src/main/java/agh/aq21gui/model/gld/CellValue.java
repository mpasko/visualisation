/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.model.output.ClassDescriptor;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class CellValue {
	
	private List<ClassDescriptor> descriptors = new LinkedList<ClassDescriptor>();

	public CellValue(ClassDescriptor desc) {
		this.descriptors.add(desc);
	}

	public CellValue() {
		
	}

	public boolean matches(ClassDescriptor other) {
		for (ClassDescriptor desc : descriptors){
			if (desc.contains(other)){
				return true;
			}
		}
		return false;
	}
	
	public boolean compare(CellValue other){
		if (other.descriptors.size() != this.descriptors.size()) {
			//TODO change in future
			return false;
		}
		for (ClassDescriptor desc : descriptors){
			if (!other.matches(desc)){
				return false;
			}
		}
		for (ClassDescriptor desc : other.descriptors){
			if (!matches(desc)){
				return false;
			}
		}
		return true;
	}

	public void addAll(List<ClassDescriptor> classes) {
		this.descriptors.addAll(classes);
	}
	
	@Override
	public String toString(){
		StringBuilder build = new StringBuilder("|");
		for (ClassDescriptor desc : descriptors){
			build.append(desc.toString());
			build.append(",");
		}
		build.append("|");
		return build.toString();
	}
}
