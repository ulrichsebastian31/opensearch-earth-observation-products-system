/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package com.astrium.hmas.server;

import com.astrium.hmas.bean.*;

import com.astrium.hmas.bean.EarthObservation.MetaDataProperty;
import com.astrium.hmas.bean.EarthObservation.MetaDataProperty.EarthObservationMetaData;
import com.astrium.hmas.bean.EarthObservation.MetaDataProperty.EarthObservationMetaData.DownlinkedTo;
import com.astrium.hmas.bean.EarthObservation.MetaDataProperty.EarthObservationMetaData.DownlinkedTo.DownlinkInformation;
import com.astrium.hmas.bean.EarthObservation.MetaDataProperty.EarthObservationMetaData.Processing;
import com.astrium.hmas.bean.EarthObservation.MetaDataProperty.EarthObservationMetaData.Processing.ProcessingInformation;
import com.astrium.hmas.bean.EarthObservationEquipment.Instrument;
import com.astrium.hmas.bean.EarthObservationEquipment.Platform;
import com.astrium.hmas.bean.EarthObservationEquipment.Instrument.InstrumentType;
import com.astrium.hmas.bean.EarthObservationEquipment.Platform.PlatformType;
import com.astrium.hmas.bean.EarthObservationEquipment.Sensor;
import com.astrium.hmas.bean.EarthObservationEquipment.Sensor.SensorType;
import com.astrium.hmas.bean.Feed.Entry;
import com.astrium.hmas.bean.Footprint.MultiExtentOf;
import com.astrium.hmas.bean.MultiSurface.SurfaceMember;
import com.astrium.hmas.bean.MultiSurface.SurfaceMember.Polygon.Exterior;
import com.astrium.hmas.bean.Polygon.Exterior.LinearRing;
import com.astrium.hmas.bean.Polygon.Exterior.LinearRing.PosList;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.multipart.FormDataMultiPart;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author re-cetienne
 */
@Path("os")
public class OpenSearchCatalogueInterface {

    
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
		try {
			
			
			ObjectFactory objFactory = new ObjectFactory();
			Feed feed = (Feed) objFactory.createFeed();
			
			//feed elements
			feed.setTitle("Catalogue Search Feed for ASAR Image Mode source packets Level 0 (ASA_IM__0P)");
			feed.setId("http://eo-virtual-archive4.esa.int/search/ASA_IM__0P/atom/");
			
			//entries -> part of feed
			List<Entry> entries = feed.getEntry();
			Entry entry = objFactory.createFeedEntry();
			//entry elements
			entry.setId("http://eo-virtual-archive4.esa.int/search/ASA_IM__0P/ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1/atom");
			entry.setTitle("ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");
			entry.setPublished("2012-03-06T00:10:33.000Z");
			entry.setUpdated("2013-05-16T22:25:57.124Z");
			entry.setDate("2010-01-22T01:44:41.316Z/2010-01-22T01:44:57.596Z");
			
			//georss:Where and element
			Where where = objFactory.createWhere();
			Polygon polygon = objFactory.createPolygon();
			com.astrium.hmas.bean.Polygon.Exterior exterior = objFactory.createPolygonExterior();
			LinearRing linearRing = objFactory.createPolygonExteriorLinearRing();
			PosList poslist = objFactory.createPolygonExteriorLinearRingPosList();
			poslist.setSrsDimension((short)2);
			poslist.setValue("42.2879 128.7535 42.4897 127.4932 41.4254 127.1936 41.2326 128.3991 42.2879 128.7535");
			linearRing.setPosList(poslist);
			exterior.setLinearRing(linearRing);
			polygon.setExterior(exterior);
			where.setPolygon(polygon);
			entry.setWhere(where);
			
			//earthObservation and element
			EarthObservation earthObs = objFactory.createEarthObservation();
			PhenomenonTime phenomenonTime = objFactory.createPhenomenonTime();
			TimePeriod timePeriod = objFactory.createTimePeriod();
			timePeriod.setId("tp_ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");
			//TODO begin and end position
			timePeriod.setBeginPosition("2010-01-22T01:44:41.316Z");
			timePeriod.setEndPosition("2010-01-22T01:44:57.596Z");
			phenomenonTime.setTimePeriod(timePeriod);
			earthObs.setPhenomenonTime(phenomenonTime);
			
			ResultTime resultTime = objFactory.createResultTime();
			TimeInstant timeInstant = objFactory.createTimeInstant();
			timeInstant.setId("ad_ ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");
			//TODO timePosition
			resultTime.setTimeInstant(timeInstant);
			earthObs.setResultTime(resultTime);
			
			Procedure procedure = objFactory.createProcedure();
			
			EarthObservationEquipment eoEquipment = objFactory.createEarthObservationEquipment();
			eoEquipment.setId("eq_ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");
			Platform platform = objFactory.createEarthObservationEquipmentPlatform();
			PlatformType platformType = objFactory.createEarthObservationEquipmentPlatformPlatform();
			platformType.setShortName("ENVISAT");
			platform.setPlatform(platformType);
			eoEquipment.setPlatform(platform);
			
			Instrument instrument = objFactory.createEarthObservationEquipmentInstrument();
			InstrumentType instrumentType = objFactory.createEarthObservationEquipmentInstrumentInstrument();
			instrumentType.setShortName("ASAR");
			instrument.setInstrument(instrumentType);
			eoEquipment.setInstrument(instrument);
			
			Sensor sensor = objFactory.createEarthObservationEquipmentSensor();
			SensorType sensorType = objFactory.createEarthObservationEquipmentSensorSensor();
			sensorType.setSensorType("RADAR");
			sensor.setSensor(sensorType);
			eoEquipment.setSensor(sensor);
			
			procedure.setEarthObservationEquipment(eoEquipment);
			
			earthObs.setProcedure(procedure);
			
			//feature of interest and elements
			FeatureOfInterest featureOfInterest = objFactory.createFeatureOfInterest();
			Footprint footprint = objFactory.createFootprint();
			MultiExtentOf multiExtentOf = objFactory.createFootprintMultiExtentOf();
			MultiSurface multiSurface = objFactory.createMultiSurface();
			SurfaceMember surfaceMember = objFactory.createMultiSurfaceSurfaceMember();
			com.astrium.hmas.bean.MultiSurface.SurfaceMember.Polygon polygon2 = objFactory.createMultiSurfaceSurfaceMemberPolygon();
			Exterior exterior2 = objFactory.createMultiSurfaceSurfaceMemberPolygonExterior();
			com.astrium.hmas.bean.MultiSurface.SurfaceMember.Polygon.Exterior.LinearRing linearRing2 = objFactory.createMultiSurfaceSurfaceMemberPolygonExteriorLinearRing();
			linearRing2.setPosList("42.2879 128.7535 42.4897 127.4932 41.4254 127.1936 41.2326 128.3991 42.2879 128.7535");
			exterior2.setLinearRing(linearRing2);
			polygon2.setExterior(exterior2);
			polygon2.setId("p_ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");
			surfaceMember.setPolygon(polygon2);
			multiSurface.setSurfaceMember(surfaceMember);
			multiSurface.setSrsName("EPSG:4326");
			multiSurface.setId("ms_ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");
			multiExtentOf.setMultiSurface(multiSurface);
			footprint.setMultiExtentOf(multiExtentOf);
			footprint.setId("fp_ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");
			featureOfInterest.setFootprint(footprint);
			
			earthObs.setFeatureOfInterest(featureOfInterest);
			
			//earth observation metadata properties and elements
			MetaDataProperty metaDataProperty = objFactory.createEarthObservationMetaDataProperty();
			EarthObservationMetaData eoMetaData = objFactory.createEarthObservationMetaDataPropertyEarthObservationMetaData();
			eoMetaData.setIdentifier("ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");
			eoMetaData.setAcquisitionType("NOMINAL");
			eoMetaData.setProductType("ASA_IM__0P");
			eoMetaData.setStatus("ARCHIVED");
			DownlinkedTo downlinkedTo = objFactory.createEarthObservationMetaDataPropertyEarthObservationMetaDataDownlinkedTo();
			DownlinkInformation downlinkInformation = objFactory.createEarthObservationMetaDataPropertyEarthObservationMetaDataDownlinkedToDownlinkInformation();
			downlinkInformation.setAcquisitionStation("PDHS-E");
			Processing processing = objFactory.createEarthObservationMetaDataPropertyEarthObservationMetaDataProcessing();
			ProcessingInformation processingInformation = objFactory.createEarthObservationMetaDataPropertyEarthObservationMetaDataProcessingProcessingInformation();
			processingInformation.setProcessingCenter("PDHS-E");
			processing.setProcessingInformation(processingInformation);
			downlinkedTo.setDownlinkInformation(downlinkInformation);
			eoMetaData.setProcessing(processing);
			eoMetaData.setDownlinkedTo(downlinkedTo);
			metaDataProperty.setEarthObservationMetaData(eoMetaData);
			
			earthObs.setMetaDataProperty(metaDataProperty);
			
			entry.setEarthObservation(earthObs);
		
			entries.add(entry);
		
			JAXBContext jaxbContext = JAXBContext.newInstance("com.astrium.hmas.bean");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
		
		
			marshaller.marshal(feed, System.out);
			return Response.ok(feed, "application/atom+xml").build();
		} catch (JAXBException e) {
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