/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

import agh.aq21gui.model.gld.Recognizer;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.output.ClassDescriptor;

/**
 *
 * @author marcin
 */
public class NegatedRecognizer implements Recognizer{
	private final Recognizer source;
	
	public NegatedRecognizer(Recognizer source){
		this.source = source;
	}

	@Override
	public boolean accept(ClassDescriptor selector, Attribute attr, DomainsGroup dg) {
		return !source.accept(selector, attr, dg);
	}

	@Override
	public String generateName() {
		StringBuilder builder = new StringBuilder("not (");
		builder.append(source.generateName());
		builder.append(")");
		return builder.toString();
	}

	@Override
	public Recognizer getCounterRecognizer() {
		return source;
	}
	
}
