/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.aq21grammar;

/**
 *
 * @author marcin
 */
public class ParsingException extends RuntimeException {
    public ParsingException(Throwable cause) {
        super("Parsing exception", cause);
    }
}
