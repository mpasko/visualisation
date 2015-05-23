/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.exceptions.ItemNotFoundException;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.TreeNode;
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
	protected String LABEL = "Domains";
	protected int CHILD_TOKEN = TParser.DOMAIN;
	
	public DomainsGroup(){
		domains = new LinkedList<Domain>();
	}
	
	@Override
	public String toString(){
        String out = "";
		if(domains.isEmpty()){
			out =  "";
		} else {
            StringBuilder builder = FormatterUtil.begin(LABEL);
            FormatterUtil.appendAll(builder, domains, 1);
            out =  FormatterUtil.terminate(builder);
        }
        return out;
	}

	public void parseDomains(TreeNode treeNode) {
		for(TreeNode node: treeNode.iterator(CHILD_TOKEN)){
			Domain domain = new Domain();
			domain.parseDomain(node);
			domains.add(domain);
		}
	}

	void traverse() {
		for(Domain d : this.domains){
			d.traverse();
		}
	}
	
	public Domain findDomain(String name){
        Domain found = null;
		for (Domain item : domains){
			if (item.getname().equalsIgnoreCase(name)){
				found = item;
                break;
			}
		}
		return found;
	}

    public void addDomain(String domainName, String value) {
        Domain domain = new Domain(domainName,value);
		domains.add(domain);
    }

    public void addDomain(String domainName, String value, String params) {
        Domain domain = new Domain(domainName,value);
        domain.setparameters(params);
		domains.add(domain);
    }

    public Domain getdomainObjectRecursively(Domain domain) {
        Domain doma = domain;
        if (doma == null) {
            throw new ItemNotFoundException("Domain not found!");
        }
        if (!doma.isTerminal()) {
            Domain domObject = this.findDomain(doma.getdomain());
            if (domObject == null) {
                throw new ItemNotFoundException("Domain not found!");
            }
            if (!doma.equals(domObject)) {
                doma = getdomainObjectRecursively(domObject);
            }
        }
        return doma;
    }

    public List<String> getRangeRecursively(Domain domain1) {
        List<String> range;
        if (domain1.set_elements == null || domain1.set_elements.isEmpty()) {
            Domain domObject = getdomainObjectRecursively(domain1);
            if (domObject == null) {
                throw new ItemNotFoundException("Domain not found!");
            }
            range = domObject.getRange();
        } else {
            range = domain1.set_elements;
        }
        return range;
    }

    public Domain findDomainObjectRecursively(String currentName) {
        return getdomainObjectRecursively(findDomain(currentName));
    }
}
