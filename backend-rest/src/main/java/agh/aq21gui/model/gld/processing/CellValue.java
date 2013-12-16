/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld.processing;

import agh.aq21gui.model.output.ClassDescriptor;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class CellValue {
	
	private List<ClassDescriptor> descriptors = new LinkedList<ClassDescriptor>();

	public CellValue(ClassDescriptor ...desc) {
		this.descriptors.addAll(Arrays.asList(desc));
	}

	public CellValue() {
		
	}

	public boolean matches(ClassDescriptor other) {
		for (ClassDescriptor desc : getDescriptors()){
			if (desc.contains(other)){
				return true;
			}
		}
		return false;
	}
	
	public boolean compare(CellValue other){
		if (other.getDescriptors().size() != this.getDescriptors().size()) {
			//TODO change in future
			return false;
		}
		for (ClassDescriptor desc : getDescriptors()){
			if (!other.matches(desc)){
				return false;
			}
		}
		for (ClassDescriptor desc : other.getDescriptors()){
			if (!matches(desc)){
				return false;
			}
		}
		return true;
	}

	public void addAll(List<ClassDescriptor> classes) {
		this.getDescriptors().addAll(classes);
	}
	
	@Override
	public String toString(){
		StringBuilder build = new StringBuilder("|");
		build.append(this.getName());
		build.append("|");
		return build.toString();
	}

	public String getName(){
		StringBuilder build = new StringBuilder();
		int cnt = 0;
		for (ClassDescriptor desc : getDescriptors()){
			if (cnt != 0){
				build.append(",");
			}
			build.append(desc.toString());
			++cnt;
		}
		return build.toString();
	}
	
	/**
	 * @return the descriptors
	 */
	public List<ClassDescriptor> getDescriptors() {
		return descriptors;
	}

	/**
	 * @param descriptors the descriptors to set
	 */
	public void setDescriptors(List<ClassDescriptor> descriptors) {
		this.descriptors = descriptors;
	}
}
