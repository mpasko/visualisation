/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.services.csv.CSVConverter;
import agh.aq21gui.utils.Downloader;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author marcin
 */
@Path("csv")
public class CsvResource {
    @POST
	@Path("downloadCSV")
	@Consumes({MediaType.TEXT_PLAIN})
	@Produces({
		MediaType.APPLICATION_JSON
	})
	public Input downloadCSV(String url){
		Downloader down = new Downloader();
		down.download(url);
		CSVConverter conv = new CSVConverter();
		Input in = conv.convert(down.getContent());
		return in;
	}
	
	@POST
	@Path("fromCSV")
	@Consumes({MediaType.TEXT_PLAIN})
	@Produces({
		MediaType.APPLICATION_JSON
	})
	public Input fromCSV(String csv){
		CSVConverter conv = new CSVConverter();
		Input in = conv.convert(csv);
		return in;
	}
}
