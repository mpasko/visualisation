/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.TreeNode;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public class Run extends Test{
    
    private String name = "run";
    private static int generator=0;
	
	public Run(){
		this.name = "run";
        if (generator++>0) {
            this.name+=generator;
        }
		this.ID = TParser.RUNS_PARAMS;
	}
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
	
	void parseRun(TreeNode runNode) {
		parseTest(runNode);
	}
}
