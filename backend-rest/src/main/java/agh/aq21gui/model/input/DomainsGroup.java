/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
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
		if(domains.isEmpty()){
			return "";
		}
		StringBuilder builder = FormatterUtil.begin(LABEL);
		FormatterUtil.appendAll(builder, domains, 1);
		return FormatterUtil.terminate(builder);
	}

	public void parseDomains(TreeNode treeNode) {
		for(TreeNode node: treeNode.iterator(CHILD_TOKEN)){
			Domain domain = new Domain();
			domain.parseDomain(node);
			domains.add(domain);
		}
	}
}
