/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.exceptions;

/**
 *
 * @author marcin
 */
public class IncorrectInputException extends RuntimeException {

    public IncorrectInputException(String describe_why) {
        super(describe_why);
    }
    
}
