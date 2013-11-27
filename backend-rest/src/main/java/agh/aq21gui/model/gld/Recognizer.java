/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Selector;

/**
 *
 * @author marcin
 */
public interface Recognizer {
//	boolean accept(Value v);
	boolean accept(ClassDescriptor selector, Attribute attr, DomainsGroup dg);
}
