/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld.presentation;

import agh.aq21gui.model.gld.processing.CellValue;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
public interface Presenter {
	
	public Object getValues(Iterable<CellValue> mesh_cells);
}
