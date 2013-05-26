/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.JsonGenerator;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 *
 * @author marcin
 */

@Produces(MediaType.APPLICATION_JSON)
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper>
{
   ObjectMapper mapper;

   public ObjectMapperProvider(){
       mapper = new ObjectMapper();
       mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
	   mapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
	   mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_FIELDS, true);
	   mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
	   mapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);
	   //mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, false);
	   mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
	   mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
	   //mapper.configure(JsonGenerator.Feature., true);
   }
   @Override
   public ObjectMapper getContext(Class<?> type) {
       return mapper;
   }
}