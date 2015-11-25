/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils.chain;

import java.util.Locale;

/**
 *
 * @author marcin
 */
public class CaseNotSupportedException extends RuntimeException{

    public CaseNotSupportedException(Object item) {
        super(String.format(Locale.US, "Case not supported during selector aggregation: %s", item.toString()));
    }
    
}
