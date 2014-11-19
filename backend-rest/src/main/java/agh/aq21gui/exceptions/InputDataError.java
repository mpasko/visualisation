/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.exceptions;

/**
 *
 * @author marcin
 */
public class InputDataError extends RuntimeException{
    public InputDataError(String explain_reason){
        super("Input data is incorrect! Reason: "+explain_reason);
    }
}
