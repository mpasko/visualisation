/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlTransient
public class DomainsGroup implements IAQ21Serializable{
	public List<Domain> domains;
	
	public DomainsGroup(){
		domains = new LinkedList<Domain>();
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("Domains\n{\n");
		for(Domain domain : domains){
			builder.append(domain.toString());
		}
		builder.append("}\n");
		return builder.toString();
	}
}
