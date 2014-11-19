/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.j48treegrammar;

/**
 *
 * @author marcin
 */
public class ParseError extends RuntimeException {

    public ParseError(String explanation) {
        super(explanation);
    }
    
}
