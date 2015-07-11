/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.csv;

import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.input.EventsGroup;
import agh.aq21gui.model.input.Input;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class CSVConverterTest {
    
    public CSVConverterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of dewebify method, of class CSVConverter.
     */
    @Test
    public void testDewebify() {
        System.out.println("dewebify");
        String in = "\\n\\r\"a\"";
        String expResult = "\n\ra";
        String result = CSVConverter.dewebify(in);
        assertEquals(expResult, result);
    }

    /**
     * Test of predictDomains method, of class CSVConverter.
     */
    @Test
    public void testPredictDomains() {
        System.out.println("predictDomains");
        final String ASASAS = "asasas";
        EventsGroup events = new EventsGroup();
        events.addEvent("?","?","?","1");
        events.addEvent("?","0.5","?","1");
        events.addEvent("?","?",ASASAS,"1");
        List<String> offeredNames = Arrays.asList("int","double","str");
        CSVConverter instance = new CSVConverter();
        LinkedList<PredictedDomain> result = instance.predictDomains(events, offeredNames);
        
        PredictedDomain intDomain = result.get(0);
        PredictedDomain doubleDomain = result.get(1);
        PredictedDomain strDomain = result.get(2);
        
        assertEquals(PredictedDomain.DomainType.INTEGER, intDomain.type);
        assertEquals(PredictedDomain.DomainType.CONTINUOUS, doubleDomain.type);
        assertEquals(PredictedDomain.DomainType.NOMINAL, strDomain.type);
        
        Domain intGenerated = intDomain.generate();
        Domain doubleGenerated = doubleDomain.generate();
        Domain strGenerated = strDomain.generate();
        Domain lastIntGenerated = result.get(3).generate();
        
        assertEquals(Integer.MIN_VALUE+", "+Integer.MAX_VALUE, intGenerated.getparameters());
        assertEquals("0.5, 0.5", doubleGenerated.getparameters());
        assertEquals("{"+ASASAS+", undefined}", strGenerated.getparameters());
        assertEquals("1, 1", lastIntGenerated.getparameters());
    }

}
