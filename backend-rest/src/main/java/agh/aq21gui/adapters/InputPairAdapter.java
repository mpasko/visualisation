/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.adapters;

import agh.aq21gui.model.management.InputPair;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author marcin
 */
public class InputPairAdapter extends XmlAdapter<String, InputPair> {

	@Override
	public InputPair unmarshal(String vt) throws Exception {
		throw new UnsupportedOperationException("Unable to parse link. Operation unsupported");
	}

	@Override
	public String marshal(InputPair bt) throws Exception {
		return bt.getLink();
	}
	
}
