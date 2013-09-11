package com.astrium.hmas.server.OpenSearchServer;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OpenSearchCatalogueInterface.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class is a server which handles 
 *   																	the construction of the XML response
 *   																	after a Catalogue Search request
 *
 * --------------------------------------------------------------------------------------------------------
 *
 * =================================================================
 *             (C) COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *             This software is supplied by EADS Astrium Limited on the express terms
 *             that it is to be treated as confidential and that it may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.astrium.hmas.bean.catalogue.EarthObservation;
import com.astrium.hmas.bean.catalogue.EarthObservationEquipment;
import com.astrium.hmas.bean.catalogue.EarthObservationResult;
import com.astrium.hmas.bean.catalogue.FeatureOfInterest;
import com.astrium.hmas.bean.catalogue.Feed;
import com.astrium.hmas.bean.catalogue.Footprint;
import com.astrium.hmas.bean.catalogue.MultiSurface;
import com.astrium.hmas.bean.catalogue.ObjectFactory;
import com.astrium.hmas.bean.catalogue.PhenomenonTime;
import com.astrium.hmas.bean.catalogue.Polygon;
import com.astrium.hmas.bean.catalogue.Procedure;
import com.astrium.hmas.bean.catalogue.Result;
import com.astrium.hmas.bean.catalogue.ResultTime;
import com.astrium.hmas.bean.catalogue.TimeInstant;
import com.astrium.hmas.bean.catalogue.TimePeriod;
import com.astrium.hmas.bean.catalogue.Where;
import com.astrium.hmas.bean.catalogue.EarthObservation.MetaDataProperty;
import com.astrium.hmas.bean.catalogue.EarthObservation.MetaDataProperty.EarthObservationMetaData;
import com.astrium.hmas.bean.catalogue.EarthObservation.MetaDataProperty.EarthObservationMetaData.ArchivedIn;
import com.astrium.hmas.bean.catalogue.EarthObservation.MetaDataProperty.EarthObservationMetaData.DownlinkedTo;
import com.astrium.hmas.bean.catalogue.EarthObservation.MetaDataProperty.EarthObservationMetaData.Processing;
import com.astrium.hmas.bean.catalogue.EarthObservation.MetaDataProperty.EarthObservationMetaData.ArchivedIn.ArchivingInformation;
import com.astrium.hmas.bean.catalogue.EarthObservation.MetaDataProperty.EarthObservationMetaData.DownlinkedTo.DownlinkInformation;
import com.astrium.hmas.bean.catalogue.EarthObservation.MetaDataProperty.EarthObservationMetaData.Processing.ProcessingInformation;
import com.astrium.hmas.bean.catalogue.EarthObservationEquipment.AcquisitionParameters;
import com.astrium.hmas.bean.catalogue.EarthObservationEquipment.Instrument;
import com.astrium.hmas.bean.catalogue.EarthObservationEquipment.Platform;
import com.astrium.hmas.bean.catalogue.EarthObservationEquipment.Sensor;
import com.astrium.hmas.bean.catalogue.EarthObservationEquipment.AcquisitionParameters.Acquisition;
import com.astrium.hmas.bean.catalogue.EarthObservationEquipment.Instrument.InstrumentType;
import com.astrium.hmas.bean.catalogue.EarthObservationEquipment.Platform.PlatformType;
import com.astrium.hmas.bean.catalogue.EarthObservationEquipment.Sensor.SensorType;
import com.astrium.hmas.bean.catalogue.EarthObservationEquipment.Sensor.SensorType.WavelengthInformation;
import com.astrium.hmas.bean.catalogue.EarthObservationEquipment.Sensor.SensorType.WavelengthInformation.WavelengthInformationType;
import com.astrium.hmas.bean.catalogue.Feed.Entry;
import com.astrium.hmas.bean.catalogue.Feed.Generator;
import com.astrium.hmas.bean.catalogue.Footprint.MultiExtentOf;
import com.astrium.hmas.bean.catalogue.MultiSurface.SurfaceMember;
import com.astrium.hmas.bean.catalogue.MultiSurface.SurfaceMember.Polygon.Exterior;
import com.astrium.hmas.bean.catalogue.Polygon.Exterior.LinearRing;
import com.astrium.hmas.bean.catalogue.Polygon.Exterior.LinearRing.PosList;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.multipart.FormDataMultiPart;

import java.util.List;
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

	@GET
	public Response getMethodParser() {
		/*
		 * Get the parameters from the query
		 */
		MultivaluedMap<String, String> conf = ui.getQueryParameters();

		try {
			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			e.printStackTrace();
		}

		/*
		 * Connection to the database
		 */
		System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");
		try {

			System.out.println("PostgreSQL JDBC Driver Registered!");

			Connection connection = null;
			Statement st = null;
			ResultSet rs = null;

			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/osresult", "postgres", "fightclub09");

			st = connection.createStatement();
			/*
			 * SQL query
			 */
			String query = "SELECT * FROM result";
			/*
			 * full query according to the different request parameters : we
			 * retrieve the different parameters value from the url query
			 */

			/*
			 * if (conf.get("start") != null && conf.get("stop") != null){ query
			 * += "WHERE archivingDate > " + conf.get("time:start") +
			 * "AND archivingDate < " + conf.get("time:end"); }
			 */
			String box = conf.get("bbox").get(0);

			String[] bbox = box.split(",");
			/*
			 * The results have to overlap the AOI (bbox)
			 */
			query += " WHERE ST_Contains(st_geomfromtext('POLYGON((" + bbox[1] + " " + bbox[0] + ", " + bbox[1] + " " + bbox[2] + ", " + bbox[3] + " "
					+ bbox[2] + ", " + bbox[3] + " " + bbox[0] + ", " + bbox[1] + " " + bbox[0] + "))',4326),\"upperRight\")"
					+ " OR ST_Contains(st_geomfromtext('POLYGON((" + bbox[1] + " " + bbox[0] + ", " + bbox[1] + " " + bbox[2] + ", " + bbox[3] + " " + bbox[2]
					+ ", " + bbox[3] + " " + bbox[0] + ", " + bbox[1] + " " + bbox[0] + "))',4326),\"upperLeft\")"
					+ " OR ST_Contains(st_geomfromtext('POLYGON((" + bbox[1] + " " + bbox[0] + ", " + bbox[1] + " " + bbox[2] + ", " + bbox[3] + " " + bbox[2]
					+ ", " + bbox[3] + " " + bbox[0] + ", " + bbox[1] + " " + bbox[0] + "))',4326),\"lowerLeft\")"
					+ " OR ST_Contains(st_geomfromtext('POLYGON((" + bbox[1] + " " + bbox[0] + ", " + bbox[1] + " " + bbox[2] + ", " + bbox[3] + " " + bbox[2]
					+ ", " + bbox[3] + " " + bbox[0] + ", " + bbox[1] + " " + bbox[0] + "))',4326),\"lowerRight\")";

			if (conf.get("id") != null) {

				query += " AND identifier = '" + conf.get("id").get(0) + "'";

			} else {
			}

			if (conf.get("acqstation") != null) {

				query += " AND \"acquisitionStation\" = '" + conf.get("acqstation").get(0) + "'";

			} else {
			}

			if (conf.get("plat") != null) {

				query += " AND platform = '" + conf.get("plat").get(0) + "'";

			} else {
			}

			if (conf.get("orbittype") != null) {

				query += " AND \"orbitType\" = '" + conf.get("orbittype").get(0) + "'";

			} else {
			}

			if (conf.get("instrument") != null) {

				query += " AND instrument = '" + conf.get("instrument").get(0) + "'";

			} else {
			}

			if (conf.get("sensortype") != null) {

				query += " AND \"sensorType\" = '" + conf.get("sensortype").get(0) + "'";

			} else {
			}

			if (conf.get("sensormode") != null) {

				query += " AND \"sensorMode\" = '" + conf.get("sensormode").get(0) + "'";

			} else {
			}

			// TODO set or range
			if (conf.get("resolution") != null) {

				query += " AND resolution = '" + conf.get("resolution").get(0) + "'";

			} else {
			}

			if (conf.get("swathid") != null) {

				query += " AND \"swathId\" = '" + conf.get("swathid").get(0) + "'";

			} else {
			}

			// TODO set or range
			if (conf.get("wavelength") != null) {

				query += " AND \"waveLength\" = '" + conf.get("wavelength").get(0) + "'";

			} else {
			}

			if (conf.get("spectralrange") != null) {

				query += " AND \"spectralRange\" = '" + conf.get("spectralrange").get(0) + "'";

			} else {
			}

			// TODO set or range
			if (conf.get("orbitnumber") != null) {

				query += " AND \"orbitNumber\" = '" + conf.get("orbitnumber").get(0) + "'";

			} else {
			}

			if (conf.get("orbitdirection") != null) {

				query += " AND \"orbitDirection\" = '" + conf.get("orbitdirection").get(0) + "'";

			} else {
			}

			// TODO set or range
			if (conf.get("track") != null) {

				query += " AND track = '" + conf.get("track").get(0) + "'";

			} else {
			}

			// TODO set or range
			if (conf.get("frame") != null) {

				query += " AND frame = '" + conf.get("frame").get(0) + "'";

			} else {
			}

			if (conf.get("type") != null) {

				query += " AND type = '" + conf.get("type").get(0) + "'";

			} else {
			}

			if (conf.get("acqtype") != null) {

				query += " AND \"acquisitionType\" = '" + conf.get("acqtype").get(0) + "'";

			} else {
			}

			if (conf.get("archcenter") != null) {

				query += " AND \"archivingCenter\" = '" + conf.get("archcenter").get(0) + "'";

			} else {
			}

			// TODO DAATES!!! --> process and archiving!!
			if (conf.get("processcenter") != null) {

				query += " AND \"processingCenter\" = '" + conf.get("processcenter").get(0) + "'";

			} else {
			}

			if (conf.get("status") != null) {

				query += " AND status = '" + conf.get("status").get(0) + "'";

			} else {
			}

			if (conf.get("processoft") != null) {

				query += " AND \"processingSoftware\" = '" + conf.get("processoft").get(0) + "'";

			} else {
			}

			if (conf.get("processlevel") != null) {

				query += " AND \"processingLevel\" = '" + conf.get("processlevel").get(0) + "'";

			} else {
			}

			if (conf.get("compositetype") != null) {

				query += " AND \"compositeType\" = '" + conf.get("compositetype").get(0) + "'";

			} else {
			}

			// TODO set or range
			if (conf.get("cloud") != null) {

				query += " AND \"cloudCover\" = '" + conf.get("cloud").get(0) + "'";

			} else {
			}

			// TODO set or range
			if (conf.get("snow") != null) {

				query += " AND \"snowCover\" = '" + conf.get("snow").get(0) + "'";

			} else {
			}

			rs = st.executeQuery(query);
			System.out.println(query);

			if (connection != null)
				System.out.println("Connection succeded!");
			else
				System.out.println("Failed to make connection!");

			/*
			 * *********** XML RESPONSE CREATION ***********
			 */
			ObjectFactory objFactory = new ObjectFactory();
			Feed feed = (Feed) objFactory.createFeed();

			/*
			 * feed elements
			 */
			feed.setTitle("Catalogue Search Feed for ASAR Image Mode source packets Level 0 (ASA_IM__0P)");
			feed.setId("http://eo-virtual-archive4.esa.int/search/ASA_IM__0P/atom/");

			Generator generator = objFactory.createFeedGenerator();
			generator.setValue("Astrium Ltd");
			feed.setGenerator(generator);
			Feed.Link link = objFactory.createFeedLink();

			// TODO put the query url here
			link.setHref("query url");
			link.setType("application/atom+xml");
			Feed.Link link2 = objFactory.createFeedLink();

			// TODO put the description url here
			link2.setHref("description url");
			link2.setType("application/atom+xml");
			List<com.astrium.hmas.bean.catalogue.Feed.Link> links = feed.getLink();
			links.add(link2);
			links.add(link);

			// TODO put the number of results -> count on the sql query
			feed.setTotalResults((short) 2);
			feed.setStartIndex((short) 0);
			feed.setItemsPerPage((short) 20);

			// TODO if needed georss:Where and element -> feed query box

			/*
			 * entries -> part of feed
			 */
			while (rs.next()) {
				List<Entry> entries = feed.getEntry();
				Entry entry = objFactory.createFeedEntry();
				/*
				 * entry elements
				 */
				entry.setId("http://eo-virtual-archive4.esa.int/search/ASA_IM__0P/ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1/atom");
				entry.setTitle("ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");
				entry.setPublished("2012-03-06T00:10:33.000Z");
				entry.setUpdated("2013-05-16T22:25:57.124Z");
				entry.setDate("2010-01-22T01:44:41.316Z/2010-01-22T01:44:57.596Z");

				/*
				 * georss:Where and element -> entry
				 */
				Where where = objFactory.createWhere();
				Polygon polygon = objFactory.createPolygon();
				com.astrium.hmas.bean.catalogue.Polygon.Exterior exterior = objFactory.createPolygonExterior();
				LinearRing linearRing = objFactory.createPolygonExteriorLinearRing();
				PosList poslist = objFactory.createPolygonExteriorLinearRingPosList();
				poslist.setSrsDimension((short) 2);
				poslist.setValue(rs.getString("footprint"));
				linearRing.setPosList(poslist);
				exterior.setLinearRing(linearRing);
				polygon.setExterior(exterior);
				where.setPolygon(polygon);
				entry.setWhere(where);

				/*
				 * earthObservation and element
				 */
				EarthObservation earthObs = objFactory.createEarthObservation();
				PhenomenonTime phenomenonTime = objFactory.createPhenomenonTime();
				TimePeriod timePeriod = objFactory.createTimePeriod();
				timePeriod.setId("tp_ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");

				// TODO begin and end position
				timePeriod.setBeginPosition("2010-01-22T01:44:41.316Z");
				timePeriod.setEndPosition("2010-01-22T01:44:57.596Z");
				phenomenonTime.setTimePeriod(timePeriod);
				earthObs.setPhenomenonTime(phenomenonTime);

				ResultTime resultTime = objFactory.createResultTime();
				TimeInstant timeInstant = objFactory.createTimeInstant();
				timeInstant.setId("ad_ ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");
				timeInstant.setTimePosition("2010-01-22T02:35:06.000Z");
				// TODO timePosition
				resultTime.setTimeInstant(timeInstant);
				earthObs.setResultTime(resultTime);

				Procedure procedure = objFactory.createProcedure();

				EarthObservationEquipment eoEquipment = objFactory.createEarthObservationEquipment();
				eoEquipment.setId("eq_ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1");
				Platform platform = objFactory.createEarthObservationEquipmentPlatform();
				PlatformType platformType = objFactory.createEarthObservationEquipmentPlatformPlatform();
				platformType.setShortName(rs.getString("platform"));
				platformType.setOrbitType(rs.getString("orbitType"));
				platform.setPlatform(platformType);
				eoEquipment.setPlatform(platform);

				Instrument instrument = objFactory.createEarthObservationEquipmentInstrument();
				InstrumentType instrumentType = objFactory.createEarthObservationEquipmentInstrumentInstrument();
				instrumentType.setShortName(rs.getString("instrument"));
				instrument.setInstrument(instrumentType);
				eoEquipment.setInstrument(instrument);

				Sensor sensor = objFactory.createEarthObservationEquipmentSensor();
				SensorType sensorType = objFactory.createEarthObservationEquipmentSensorSensor();
				sensorType.setSensorType(rs.getString("sensorType"));
				sensorType.setOperationalMode(rs.getString("sensorMode"));
				sensorType.setResolution(rs.getString("resolution"));
				sensorType.setSwathIdentifier(rs.getString("swathId"));
				WavelengthInformation wavelengthInformation = objFactory.createEarthObservationEquipmentSensorSensorWavelengthInformation();
				WavelengthInformationType wavelengthInformationType = objFactory
						.createEarthObservationEquipmentSensorSensorWavelengthInformationWavelengthInformation();
				wavelengthInformationType.setDiscreteWavelengths(rs.getString("wavelength"));
				wavelengthInformationType.setSpectralRange(rs.getString("spectralRange"));
				wavelengthInformation.setWavelengthInformation(wavelengthInformationType);
				sensorType.setWavelengthInformation(wavelengthInformation);
				sensor.setSensor(sensorType);
				eoEquipment.setSensor(sensor);

				AcquisitionParameters acquisitionParameters = objFactory.createEarthObservationEquipmentAcquisitionParameters();
				Acquisition acquisition = objFactory.createEarthObservationEquipmentAcquisitionParametersAcquisition();
				acquisition.setOrbitDirection(rs.getString("orbitDirection"));
				acquisition.setOrbitNumber(rs.getString("orbitNumber"));
				acquisition.setWrsLatitudeGrid(rs.getString("frame"));
				acquisition.setWrsLongitudeGrid(rs.getString("track"));
				acquisitionParameters.setAcquisition(acquisition);
				eoEquipment.setAcquisitionParameters(acquisitionParameters);

				procedure.setEarthObservationEquipment(eoEquipment);

				earthObs.setProcedure(procedure);

				/*
				 * feature of interest and elements
				 */
				FeatureOfInterest featureOfInterest = objFactory.createFeatureOfInterest();
				Footprint footprint = objFactory.createFootprint();
				MultiExtentOf multiExtentOf = objFactory.createFootprintMultiExtentOf();
				MultiSurface multiSurface = objFactory.createMultiSurface();
				SurfaceMember surfaceMember = objFactory.createMultiSurfaceSurfaceMember();
				com.astrium.hmas.bean.catalogue.MultiSurface.SurfaceMember.Polygon polygon2 = objFactory.createMultiSurfaceSurfaceMemberPolygon();
				Exterior exterior2 = objFactory.createMultiSurfaceSurfaceMemberPolygonExterior();
				com.astrium.hmas.bean.catalogue.MultiSurface.SurfaceMember.Polygon.Exterior.LinearRing linearRing2 = objFactory
						.createMultiSurfaceSurfaceMemberPolygonExteriorLinearRing();
				linearRing2.setPosList(rs.getString("footprint"));
				exterior2.setLinearRing(linearRing2);
				polygon2.setExterior(exterior2);
				polygon2.setId("p_" + rs.getString("identifier"));
				surfaceMember.setPolygon(polygon2);
				multiSurface.setSurfaceMember(surfaceMember);
				multiSurface.setSrsName("EPSG:4326");
				multiSurface.setId("ms" + rs.getString("identifier"));
				multiExtentOf.setMultiSurface(multiSurface);
				footprint.setMultiExtentOf(multiExtentOf);
				footprint.setId("fp_" + rs.getString("identifier"));
				featureOfInterest.setFootprint(footprint);

				earthObs.setFeatureOfInterest(featureOfInterest);

				/*
				 * earth obs results and elements
				 */
				Result result = objFactory.createResult();
				EarthObservationResult eoResult = objFactory.createEarthObservationResult();
				eoResult.setCloudCoverPercentage(rs.getString("cloudCover"));
				eoResult.setSnowCoverPercentage(rs.getString("snowCover"));
				result.setEarthObservationResult(eoResult);

				earthObs.setResult(result);

				/*
				 * earth observation metadata properties and elements
				 */
				MetaDataProperty metaDataProperty = objFactory.createEarthObservationMetaDataProperty();
				EarthObservationMetaData eoMetaData = objFactory.createEarthObservationMetaDataPropertyEarthObservationMetaData();
				eoMetaData.setIdentifier(rs.getString("identifier"));
				eoMetaData.setAcquisitionType(rs.getString("acquisitionType"));
				eoMetaData.setProductType(rs.getString("type"));
				eoMetaData.setStatus(rs.getString("status"));
				ArchivedIn archivedIn = objFactory.createEarthObservationMetaDataPropertyEarthObservationMetaDataArchivedIn();
				ArchivingInformation archivingInformation = objFactory
						.createEarthObservationMetaDataPropertyEarthObservationMetaDataArchivedInArchivingInformation();
				archivingInformation.setArchivingCenter(rs.getString("archivingCenter"));
				archivingInformation.setArchivingDate(rs.getString("archivingDate"));
				archivedIn.setArchivingInformation(archivingInformation);
				eoMetaData.setArchivedIn(archivedIn);
				DownlinkedTo downlinkedTo = objFactory.createEarthObservationMetaDataPropertyEarthObservationMetaDataDownlinkedTo();
				DownlinkInformation downlinkInformation = objFactory
						.createEarthObservationMetaDataPropertyEarthObservationMetaDataDownlinkedToDownlinkInformation();
				downlinkInformation.setAcquisitionStation(rs.getString("acquisitionStation"));
				downlinkedTo.setDownlinkInformation(downlinkInformation);
				Processing processing = objFactory.createEarthObservationMetaDataPropertyEarthObservationMetaDataProcessing();
				ProcessingInformation processingInformation = objFactory
						.createEarthObservationMetaDataPropertyEarthObservationMetaDataProcessingProcessingInformation();
				processingInformation.setProcessingCenter(rs.getString("processingCenter"));
				processingInformation.setCompositeType(rs.getString("compositeType"));
				processingInformation.setProcessingDate(rs.getString("processingDate"));
				processingInformation.setProcessingLevel(rs.getString("processingLevel"));
				processingInformation.setProcessingSoftware(rs.getString("processingSoftware"));
				processing.getContent().add(processingInformation);
				downlinkedTo.setDownlinkInformation(downlinkInformation);
				eoMetaData.setProcessing(processing);
				eoMetaData.setDownlinkedTo(downlinkedTo);
				metaDataProperty.setEarthObservationMetaData(eoMetaData);

				earthObs.setMetaDataProperty(metaDataProperty);

				entry.setEarthObservation(earthObs);

				entries.add(entry);
			}

			/*
			 * XML construction thanks to the JAXB api
			 */
			JAXBContext jaxbContext = JAXBContext.newInstance("com.astrium.hmas.bean.catalogue");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));

			marshaller.marshal(feed, System.out);
			/*
			 * return the XML to the client
			 */
			return Response.ok(feed, "application/atom+xml").build();

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}

	}

	@POST
	@Consumes(MediaType.APPLICATION_ATOM_XML)
	// @GET
	public Response postRequestParser(FormDataMultiPart formDataMultiPart) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
}