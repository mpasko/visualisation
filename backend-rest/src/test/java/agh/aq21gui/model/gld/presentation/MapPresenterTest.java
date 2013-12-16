/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld.presentation;

import agh.aq21gui.model.gld.processing.CellValue;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.stubs.Utils;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author marcin
 */
public class MapPresenterTest {
	
	public MapPresenterTest() {
	}

	/**
	 * Test of getValues method, of class MapPresenter.
	 */
	@Test
	public void testGetValues() {
		System.out.println("getValues");
		
		ClassDescriptor a = Utils.valueSelector("a", "=", "1");
		ClassDescriptor b = Utils.valueSelector("b", "=", "2");
		ClassDescriptor b2 = Utils.valueSelector("b", "=", "2");
		ClassDescriptor c = Utils.valueSelector("c", "=", "3");
		ClassDescriptor c2 = Utils.valueSelector("c", "=", "3");
		ClassDescriptor d = Utils.valueSelector("d", "=", "4");
		List<CellValue> mesh_cells = new LinkedList<CellValue>();
		mesh_cells.add(new CellValue(a));
		mesh_cells.add(new CellValue(b));
		mesh_cells.add(new CellValue(c,d));
		mesh_cells.add(new CellValue(b2,c2,d));
		MapPresenter instance = new MapPresenter();
		Map<ClassDescriptor,MapPresenter.NumberList> result;
		result = (Map<ClassDescriptor,MapPresenter.NumberList>) instance.getValues(mesh_cells);
		System.out.println(result);
		assertEquals(4, result.keySet().size());
		assertEquals(1, result.get(a).list.size());
		assertEquals(2, result.get(d).list.size());
		assertEquals(2, result.get(b).list.size());
		assertEquals(2, result.get(c).list.size());
	}
}
