/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author marcin
 */
@Path("security")
public class SignUp {
	public static String returnURL="";
	
	@POST
	@Path("login")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({
		MediaType.APPLICATION_JSON
	//	,MediaType.APPLICATION_XML
	})
	public Object login(Object in){
		return null;
	}
}
