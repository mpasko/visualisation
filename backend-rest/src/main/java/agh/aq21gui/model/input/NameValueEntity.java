/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author marcin
 */
public abstract class NameValueEntity implements IAQ21Serializable {
	public long id;
	public String name;
	public List<String> set_elements = new LinkedList<String>();

	public NameValueEntity() {
	}

	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	@XmlElement(name = "id")
	public void setId(long id) {
		this.id = id;
	}

	@XmlElement(name = "name")
	public void setName(String nm) {
		this.name = nm;
	}

	public void traverse(){
		if(id==-1);
	}
	
}