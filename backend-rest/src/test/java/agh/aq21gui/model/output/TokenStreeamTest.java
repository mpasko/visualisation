/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.Aq21Resource;
import agh.aq21gui.Invoker;
import agh.aq21gui.aq21grammar.TLexer;
import agh.aq21gui.aq21grammar.TParser;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import agh.aq21gui.aq21grammar.TParser.output_return;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.antlr.runtime.Token;

/**
 *
 * @author marcin
 */
public class TokenStreeamTest {
	
	public TokenStreeamTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	private class TokenMemory{
		private int number;
		private TokenMemory next;
		
		public TokenMemory(int number){
			this.number = number;
			this.next = null;
		}
		
		public TokenMemory(int number, TokenMemory t){
			this.number = number;
			this.next = t;
		}
		
		public boolean check(int depth){
			if(depth == 0){
				return true;
			}
			if (next != null){
				return (number==next.number)
						&& next.check(depth-1);
			}else{
				return false;
			}
		}
	}
	
	// TODO add test methods here.
	// The methods must be annotated with annotation @Test. For example:
	//
	//@Test
	public void examineStream() throws IOException {
		TLexer lexer = new TLexer();
		FileInputStream stream = new FileInputStream("test_inputs/template1.txt");
		String out = Invoker.streamToString(stream);
		lexer.setCharStream(new ANTLRStringStream(out));
		lexer.reset();
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		System.out.println("lex:");
		Token t = tokens.LT(1);
		TokenMemory mem = new TokenMemory(t.getType());
		tokens.consume();
		while (!mem.check(20)){
			System.out.println("Next token:");
			System.out.println( t.toString() );
			System.out.println( t.getType() );
			t =tokens.LT(1);
			tokens.consume();
			mem = new TokenMemory(t.getType(), mem);
		}
	}
	
	//@Test
	public void examineParser(){
/*		TParser parser = new TParser(tokens);
		TParser.output_return out_ret;
		Tree tree;
		try{
			System.out.println("parse:");
			out_ret = parser.output();
			tree = (Tree)out_ret.getTree();
			System.out.println("tree:");
			System.out.println(tree.toStringTree());
		}catch(RecognitionException ex){
			Logger.getLogger(Aq21Resource.class.getName()).log(Level.SEVERE, null, ex);
		}
*/
	}
}
