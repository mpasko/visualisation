/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 *
 * @author marcin
 */
public class CorsResponseFilter implements ContainerResponseFilter {

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        final ResponseBuilder resp = Response.fromResponse(response.getResponse());
        resp.header("Access-Control-Allow-Origin", "*");
        resp.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        //resp.header("Access-Control-Allow-Headers", "origin, content-type, accept").build();
        final String reqHead = request.getHeaderValue("Access-Control-Request-Headers");
        if (null != reqHead && !reqHead.equals(null)) {
            resp.header("Access-Control-Allow-Headers", reqHead);
        }
        response.setResponse(resp.build());
        return response;
    }
}
