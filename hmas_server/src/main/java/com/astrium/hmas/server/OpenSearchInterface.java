/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astrium.hmas.server;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.multipart.FormDataMultiPart;
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
		/*String responseText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
		+ "<OpenSearchDescription xmlns=\"http://a9.com/-/spec/opensearch/1.1/\" " 
		+ "xmlns:eop=\"http://www.genesi-dr.eu/spec/opensearch/extensions/eop/1.0/\">" 
		+ "<ShortName>Web Search</ShortName>"
		+ "<Description>Use Example.com to search the Web.</Description>"
		+ "<Tags>example web</Tags>"
		+ "<Contact>admin@example.com</Contact>"
		+ "<Url type=\"application/atom+xml\" template=\"http://example.com/?q=%7BsearchTerms%7D&amp;orbit=%7Beop:orbitNumber?%7D&amp;acqstation=%7Beop:acquisitionStation?%7D&amp;pw=%7BstartPage?%7D&amp;format=atom\"/>"
		+ "<Url type=\"text/html\" template=\"http://example.com/?q={searchTerms}&amp;orbit={eop:orbitNumber?}&amp;acqstation={eop:acquisitionStation?}&amp;pw={startPage?}\"/>"
		+ "<LongName>Example.com Web Search</LongName>"
		+ "<Image height=\"64\" width=\"64\" type=\"image/png\">http://example.com/websearch.png</Image>"
		+ "<Developer>Example.com Development Team</Developer>"
		+ "<SyndicationRight>open</SyndicationRight>"
		+ "<AdultContent>false</AdultContent>"
		+ "<Language>en-us</Language>"
		+ "<OutputEncoding>UTF-8</OutputEncoding>"
		+ "<InputEncoding>UTF-8</InputEncoding>"
		+ "</OpenSearchDescription>";*/
		
		String responseText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
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
		+ "</OpenSearchDescription>";

		
		return Response.ok(responseText, "application/atom+xml").build();
		
	}
    
    @POST
    @Consumes(MediaType.APPLICATION_ATOM_XML)
//    @GET
    public Response postRequestParser(FormDataMultiPart formDataMultiPart)
    {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}