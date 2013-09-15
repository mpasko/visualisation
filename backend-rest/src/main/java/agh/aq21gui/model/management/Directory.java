/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.management;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */

@XmlRootElement
public class Directory {
	private int id=0;
	
	private List<InputPair> experiments;
	private List<OutputPair> results;
	
	@XmlElement(name="experiments")
	public List<InputPair> getExperiments(){
		return experiments;
	}
	
	public void setExperiments(List<InputPair> experiments){
		this.experiments = experiments;
	}
	
	@XmlElement(name="results")
	public List<OutputPair> getResults(){
		return results;
	}
	
	public void setResults(List<OutputPair> results){
		this.results = results;
	}
	
	public int getdbid(){
		return id;
	}
	
	public void setdbid(int id){
		this.id = id;
	}
	
	public void putResult(String name, Output value){
		results.add(new OutputPair(name, value));
	}
	
	public void putExperiment(String name, Input value){
		experiments.add(new InputPair(name, value));
	}
	
	public Directory(){
		experiments = new LinkedList<InputPair>();
		results = new LinkedList<OutputPair>();
	}
	
	public void traverse(){
		for(InputPair exp : this.experiments){
			exp.traverse();
		}
		
		for(OutputPair out : this.results){
			out.traverse();
		}
	}
}
