/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.j48treegrammar;

import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
public class ParserManualTests {
    public static void main(String[] args) throws Exception{
        branchDescTest(" [label=\"= 0.5\"]");
        branchTest("A->B");
        branchTest("N0->N1 [label=\"= 0.5\"]");
    }

    private static void branchTest(String n0N1) throws Exception{
        System.out.println("trying: "+n0N1);
        J48ParserUtil parser = new J48ParserUtil();
        Tj48TreeParser tparser1 = parser.prepareParser(n0N1);
        CommonTree branchtree = (CommonTree)tparser1.branch().getTree();
        System.out.println(branchtree.toStringTree());
        
    }
    
    private static void branchDescTest(String n0N1) throws Exception{
        System.out.println("trying: "+n0N1);
        J48ParserUtil parser = new J48ParserUtil();
        Tj48TreeParser tparser1 = parser.prepareParser(n0N1);
        CommonTree branchtree = (CommonTree)tparser1.branch_desc().getTree();
        System.out.println(branchtree.toStringTree());
        
    }
}
