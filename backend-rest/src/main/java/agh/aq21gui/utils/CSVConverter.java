/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.EventsGroup;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.input.Test;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class CSVConverter {

	private Test generateRun(Attribute attr) {
		Run r = new Run();
		r.setName("c1");
		r.addParameter("mode", "tf");
		r.addParameter("consequent", String.format("[%s=*]", attr.getname()));
		r.addParameter("ambiguity", "includeinpos");
		r.addParameter("trim", "optimal");
		r.addParameter("compute_alternative_covers", "true");
		r.addParameter("maxstar", "1");
		r.addParameter("maxrule", "10");
		return r;
	}
	
	private enum DomainType{
		CONTINUOUS("continuous"),
		INTEGER("integer"),
		LINEAR("linear"),
		NOMINAL("nominal");
		
		public String value;
		private DomainType(String value){
			this.value = value;
		}
	}
		
		public static boolean isInteger(String value){
			try{
				Integer.parseInt(value);
				return true;
			}catch(NumberFormatException ex){
				return false;
			}
		}
		
		public static boolean isNumber(String value){
			try{
				Double.parseDouble(value);
				return true;
			}catch(NumberFormatException ex){
				return false;
			}
		}
	
	private class PredictedDomain{
		public DomainType type;
		public List<String> values;
		public Double max;
		public Double min;
		public int number;
		
		public PredictedDomain(int id){
			number = id;
			type = DomainType.INTEGER;
			values = new LinkedList<String>();
			max = Double.MIN_VALUE;
			min = Double.MAX_VALUE;
		}
		
		public void VerifyNew(String value){
			switch (type){
				case INTEGER:
					if(!isInteger(value)){
						type = DomainType.CONTINUOUS;
					}else if(!isNumber(value)){
						type = DomainType.NOMINAL;
					}
				case CONTINUOUS:
					if(!isNumber(value)){
						type = DomainType.NOMINAL;
					}
					break;
				case NOMINAL:
					break;
			}
			if (isNumber(value)) {
				Double val = Double.parseDouble(value);
				if(val>max){
					max=val;
				}
				if(val<min){
					min=val;
				}
			}
			if(!values.contains(value)){
				values.add(value);
			}
		}
		
		public Domain generate(){
			Domain dom = new Domain();
			dom.setdomain(type.value);
			dom.setname("domain"+number);
			switch(type){
				case INTEGER:
				case CONTINUOUS:
					dom.setRange(min,max);
					break;
				case NOMINAL:
					dom.setRange(this.values);
					break;
			}
			return dom;
		}
	}
	
	public static String dewebify(String in){
		System.out.println(in);		
		String nls = in.replace("\\n", "\n");
		nls = nls.replace("\\r", "\r");
		nls = nls.replace("\"", "");
		System.out.println("After dewebify:");
		System.out.println(nls);		
		return nls;
	}
	
	public DomainsGroup predictDomains(EventsGroup events){
		DomainsGroup domains = new DomainsGroup();
		Event event0 = events.events.get(0);
		int len = event0.getValues().size();
		LinkedList<PredictedDomain> columns = new LinkedList<PredictedDomain>();
		for (int i=1; i<=len; ++i){
			columns.add(new PredictedDomain(i));
		}
		for (Event e : events.events){
			for (int i=0; i<len; ++i){
				columns.get(i).VerifyNew(e.getValues().get(i));
			}
		}
		for (int i=0; i<len; ++i){
			domains.domains.add(columns.get(i).generate());
		}
		return domains;
	}
	
	public AttributesGroup predictAttributes(DomainsGroup domains){
		AttributesGroup attributes = new AttributesGroup();
		int i = 1;
		for(Domain d : domains.domains){
			Attribute attr = new Attribute();
			attr.setname("attribute"+i);
			attr.setdomain(d.getname());
			attributes.attributes.add(attr);
			++i;
		}
		return attributes;
	}
	
	public Input convert(String csv){
		String nls = dewebify(csv);
		nls = nls.replace(" ","");
		nls = nls.replaceAll("\t","");
		
		EventsGroup eventsGroup = new EventsGroup();
		String[] lines = nls.split("\\n");
		for (String line : lines){
			String[] events = line.split(",");
            
            
			if(!events[0].isEmpty()){
                
                for (int i =0;i<events.length;i++) {
                    if (CSVConverter.isNumber(events[i]) && events[i].startsWith(".")) {
                        events[i] = "0" + events[i];
                    }
                }
				eventsGroup.addEvent(events);
			}
		}
		DomainsGroup domains = predictDomains(eventsGroup);
		AttributesGroup attributes = predictAttributes(domains);
		Input input = new Input();
		input.sEG(eventsGroup);
		input.sDomainsGroup(domains);
		input.sAttributesGroup(attributes);
		List<Test> runs = new LinkedList<Test>();
		runs.add(generateRun(attributes.attributes.get(attributes.attributes.size()-1)));
		input.runsGroup.setRuns(runs);
		return input;
	}
}
