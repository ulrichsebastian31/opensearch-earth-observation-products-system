/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Path("cat/os")
public class OpenSearchInterface {

    
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
				document = (Document) dBuilder.parse(new File("description.xml"));
				 /*TransformerFactory tf = TransformerFactory.newInstance();
		         Transformer transformer = tf.newTransformer();
		         transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");*/
		         OutputFormat format = new OutputFormat(document); 
		         StringWriter writer = new StringWriter();
		         XMLSerializer serial = new XMLSerializer (writer, format);
		         serial.serialize(document);
		         //transformer.transform(new DOMSource((Node) document), new StreamResult(writer));
		         //String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		         //XMLOutputter outputter = new XMLOutputter(((Object) document).getPrettyFormat());
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
	        
	   
		
		/*String responseText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
		+ "<OpenSearchDescription xmlns=\"http://a9.com/-/spec/opensearch/1.1/\" " 
		+ "xmlns:eo=\"http://a9.com/-/opensearch/extensions/eo/1.0/\" "
		+ "xmlns:param=\"http://a9.com/-/spec/opensearch/extensions/parameters/1.0/\">"
		+ "<ShortName>Web Search</ShortName>" 
		+ "<LongName>Example.com Web Search</LongName>" 
		+ "<Description>Use Example.com to search the Web.</Description> "
		+ "<Tags>example web</Tags>" 
		+ "<Contact>admin@example.com</Contact>" 
		+ "<Url type=\"application/atom+xml\" template=\"http://example.com/myatom/?pw={startPage?}&amp;acq={eo:acquisitionStation?}&amp;start={time:start?}&amp;end={time:end?}\"/>"
		+ "<param:Parameter name=\"start\" value=\"{time:start}\" minimum=\"0\" "
		+ "minInclusive=\"2011-01-01T00:00:00Z\" maxExclusive=\"2012-01-01T00:00:00Z\"/>"
		+ "<param:Parameter name=\"end\" value=\"{time:end}\" minimum=\"0\""
		+ " minInclusive=\"2011-01-01T00:00:00Z\" maxExclusive=\"2012-01-01T00:00:00Z\"/>"
		+ "<param:Parameter name=\"acq\" value=\"eo:acquisitionStation\" minimum=\"0\"" 
		+ " title=\"Acquisiton Station\">"
		+ "<param:Option value=\"PDHS-K\" label=\"Kiruna\"/>"
		+ "		<param:Option value=\"PDHS-E\" label=\"ESRIN\"/>"
		+ "</param:Parameter>"
		+ "<Attribution>Copyright 2005, Example.com, Inc.</Attribution>" 
		+ "<SyndicationRight>open</SyndicationRight>"
		+ "</OpenSearchDescription>";*/

	}
    
    @POST
    @Consumes(MediaType.APPLICATION_ATOM_XML)
//    @GET
    public Response postRequestParser(FormDataMultiPart formDataMultiPart)
    {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}