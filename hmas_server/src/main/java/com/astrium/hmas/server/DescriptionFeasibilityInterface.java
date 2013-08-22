package com.astrium.hmas.server;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author re-cetienne
 */
@Path("feas/os")
public class DescriptionFeasibilityInterface {
	
    /**
     * Map containing the URL parameters
     */ 
    @Context 
    volatile UriInfo ui;
    
    @Context
    volatile HttpContext httpContext;
    
//    private URI admin = httpContext.getUriInfo().getBaseUri();//.resolve("fasadmin/");
    
	@GET
	public Response getMethodParser()
	{
		MultivaluedMap<String, String> conf = ui.getQueryParameters();
		
		Document document = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder;
		
		
	
	         try {
	        	dBuilder = dbFactory.newDocumentBuilder();
				document = (Document) dBuilder.parse(new File("descriptionFeasibility.xml"));
		         OutputFormat format = new OutputFormat(document); 
		         StringWriter writer = new StringWriter();
		         XMLSerializer serial = new XMLSerializer (writer, format);
		         serial.serialize(document);
		         return Response.ok(writer.toString(), "application/atom+xml").build();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	        
	}
    
    @POST
    @Consumes(MediaType.APPLICATION_ATOM_XML)
//    @GET
    public Response postRequestParser(FormDataMultiPart formDataMultiPart)
    {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

}
