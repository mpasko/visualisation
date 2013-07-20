/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class NoDatabaseConfiguredException extends Exception{
	@Override
	public String toString(){
		return "Unable to connect to the database engine!";
	}
	
	@XmlAttribute(name="name")
	public String getName(){
		return this.toString();
	}
	
	public void setName(String name){}
	
}
