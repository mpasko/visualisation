/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.rubbish;

import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class MapAdapter extends XmlAdapter<Entries, Map<String,String>> {

	@Override
	public Map<String, String> unmarshal(Entries vt) throws Exception {
		List<Pair> entries = vt.content;
		Map<String,String> m = new HashMap<String, String>(entries.size());
		for(Pair e : entries){
			m.put(e.key, e.value);
		}
		return m;
	}

	@Override
	public Entries marshal(Map<String, String> bt) throws Exception {
		Entries entries = new Entries();
		for(Map.Entry<String,String> e : bt.entrySet()){
			Pair p = new Pair(e.getKey(),e.getValue());
			entries.content.add(p);
		}
		return entries;
	}
	
}
