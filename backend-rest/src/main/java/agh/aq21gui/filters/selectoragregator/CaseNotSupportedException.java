/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters.selectoragregator;

import agh.aq21gui.model.output.Selector;
import java.util.Locale;

/**
 *
 * @author marcin
 */
public class CaseNotSupportedException extends RuntimeException{

    public CaseNotSupportedException(Selector next, Selector actual) {
        super(String.format(Locale.US, "Case not supported during selector aggregation: %s, %s", next.toString(), actual.toString()));
    }
    
}
