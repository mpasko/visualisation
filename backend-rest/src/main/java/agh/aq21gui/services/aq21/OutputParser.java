/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.aq21;

import agh.aq21gui.Aq21Resource;
import agh.aq21gui.aq21grammar.TLexer;
import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.aq21grammar.TParser.output_return;
import agh.aq21gui.model.output.Output;
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
public class OutputParser {
	private final TLexer lexer;
	
	public OutputParser(){
		lexer = new TLexer();
	}
	
	public Output parse(String out){
        CommonTree tree = genericParse(out);
        Output output = new Output(tree,out);
		return output;
	}

    public CommonTree genericParse(String out) {
        TParser parser = prepareParser(out);
        CommonTree tree = null;
        try{
//            System.out.println("Parsing:");
//            System.out.println(out);
            output_return out_ret = parser.output();
            tree = (CommonTree)out_ret.getTree();
//            System.out.println("tree:");
//            System.out.println(tree.toStringTree());
        }catch(RecognitionException ex){
            //Logger.getLogger(Aq21Resource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        return tree;
    }

    public TParser prepareParser(String out) {
        String processed = out.toLowerCase().replace("..", "@");
//        System.out.println(processed);
        lexer.setCharStream(new ANTLRStringStream(processed));
        lexer.reset();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        //		System.out.println("lex:");
        //		tokens.LT(1);
        TParser parser = new TParser(tokens);
        return parser;
    }
}
