/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.exceptions;

/**
 *
 * @author marcin
 */
public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String item_not_found) {
        super(item_not_found);
    }
    
}
