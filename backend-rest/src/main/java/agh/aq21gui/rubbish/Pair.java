/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.rubbish;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author marcin
 */
public class Pair {
	@XmlTransient
    public String key;
         
    @XmlValue
    public String value;
	
	public Pair(){};
	public Pair(String key, String value){
		this.key=key;
		this.value=value;
	}
}
