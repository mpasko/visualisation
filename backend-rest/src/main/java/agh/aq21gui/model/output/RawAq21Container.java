/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

/**
 *
 * @author marcin
 */
public class RawAq21Container {
	private String raw_data="";
	
	public RawAq21Container(String data){
		this.raw_data = data;
	}
	
	public String getRaw(){
		return raw_data;
	}
	
	public void setRaw(String data){
		this.raw_data = data;
	}
}
