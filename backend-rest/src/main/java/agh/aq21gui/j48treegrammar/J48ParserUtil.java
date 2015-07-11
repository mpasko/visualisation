/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.j48treegrammar;

import agh.aq21gui.utils.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author marcin
 */
public class J48ParserUtil {
	private final Tj48TreeLexer lexer;
	
	public J48ParserUtil(){
		lexer = new Tj48TreeLexer();
	}
    
    public J48Tree parse(String dotty){
        CommonTree grammartree = genericParse(dotty);
        return new J48Tree(grammartree);
    }
    
    public CommonTree genericParse(String out) {
        Tj48TreeParser parser = prepareParser(out);
        CommonTree tree = null;
        System.out.println(Util.attachLines(out));
        try{
//			System.out.println("parse:");
            Tj48TreeParser.output_return out_ret = parser.output();
            tree = (CommonTree)out_ret.getTree();
            //System.out.println("tree:");
            //System.out.println(tree.toStringTree());
        }catch(RecognitionException ex){
            //Logger.getLogger(J48ParserUtil.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(Util.attachLines(out));
            throw new RuntimeException(ex);
        }
        return tree;
    }

    public Tj48TreeParser prepareParser(String out) {
        String processed = out.toLowerCase().replace("->", "&");
        lexer.setCharStream(new ANTLRStringStream(processed));
        lexer.reset();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        //		System.out.println("lex:");
        //		tokens.LT(1);
        Tj48TreeParser parser = new Tj48TreeParser(tokens);
        return parser;
    }
}
