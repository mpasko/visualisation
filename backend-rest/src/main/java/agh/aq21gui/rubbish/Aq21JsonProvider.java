/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.rubbish;

import agh.aq21gui.model.input.Input;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
//import org.eclipse.persistence.jaxb.MarshallerProperties;

/**
 *
 * @author marcin
 */
//@Provider
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
public class Aq21JsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
	
	@Context
	protected Providers providers;
	

	@Override
	public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
		return Input.class == getDomainClass(type1);
	}

	@Override
	public Object readFrom(Class<Object> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException, WebApplicationException {
		try{
			Class<?> domainClass = getDomainClass(type1);
			Unmarshaller unmarshaller = getJAXBContext(domainClass, mt).createUnmarshaller();
			unmarshaller.setProperty("eclipselink.media-type", MediaType.APPLICATION_JSON);
			unmarshaller.setProperty("eclipselink.json.include-root", false);
			StreamSource source;
			Map<String, String> parameters = mt.getParameters();
			if(parameters.containsKey("charset")){
				String charset = parameters.get("charset");
				Reader entityReader = new InputStreamReader(in, charset);
				source = new StreamSource(entityReader);
			}else{
				source = new StreamSource(in);
			}
			return unmarshaller.unmarshal(source, domainClass);
		}catch(JAXBException ex){
			throw new WebApplicationException(ex);
		}
	}

	@Override
	public boolean isWriteable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
		return isReadable(type, type1, antns, mt);
	}

	@Override
	public long getSize(Object t, Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
		return -1;
	}

	@Override
	public void writeTo(Object t, Class<?> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, Object> mm, OutputStream out) throws IOException, WebApplicationException {
		try{
			Class<?> domainClass = getDomainClass(type1);
			Marshaller marshaller = getJAXBContext(domainClass, mt).createMarshaller();
//			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
			marshaller.setProperty("eclipselink.json.include-root", false);
			Map<String, String> parameters = mt.getParameters();
			if(parameters.containsKey("charset")){
				String charset = parameters.get("charset");
				marshaller.setProperty(Marshaller.JAXB_ENCODING, charset);
			}
			marshaller.marshal(t, out);
		}catch(JAXBException ex){
			Logger.getLogger("Provider").log(Level.SEVERE, "Object class:{0}", type.getCanonicalName());
			throw new WebApplicationException(ex);
		}
	}
	
	private JAXBContext getJAXBContext(Class<?> type, MediaType mediaType)
        throws JAXBException {
        ContextResolver<JAXBContext> resolver
            = providers.getContextResolver(JAXBContext.class, mediaType);
        JAXBContext jaxbContext;
        if(null == resolver || null == (jaxbContext = resolver.getContext(type))) {
            return JAXBContext.newInstance(type);
        } else {
            return jaxbContext;
        }
    }
	
	private Class<?> getDomainClass(Type genericType) {
        if(genericType instanceof Class) {
            return (Class<?>) genericType;
        } else if(genericType instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
        } else {
            return null;
        }
    }
}
