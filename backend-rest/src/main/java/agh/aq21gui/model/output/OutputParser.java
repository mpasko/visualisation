/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.Aq21Resource;
import agh.aq21gui.aq21grammar.TLexer;
import agh.aq21gui.aq21grammar.TParser;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;

import agh.aq21gui.aq21grammar.TParser.output_return;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
/**
 *
 * @author marcin
 */
public class OutputParser {
	private final TLexer lexer;
	
	public OutputParser(){
		lexer = new TLexer();
	}
	
	public Output parse(String out){
		lexer.setCharStream(new ANTLRStringStream(out));
		lexer.reset();
		CommonTokenStream tokens = new CommonTokenStream(lexer);
//		System.out.println("lex:");
//		tokens.LT(1);
		TParser parser = new TParser(tokens);
		output_return out_ret;
		CommonTree tree;
		Output output = null;
		try{
//			System.out.println("parse:");
			out_ret = parser.output();
			tree = (CommonTree)out_ret.getTree();
//			System.out.println("tree:");
//			System.out.println(tree.toStringTree());
			
			output = new Output(tree);
			output.outHypo.content = "Raw string content is deprecated. Use json fields!";
		}catch(RecognitionException ex){
			Logger.getLogger(Aq21Resource.class.getName()).log(Level.SEVERE, null, ex);
		}
		 
		return output;
	}
}