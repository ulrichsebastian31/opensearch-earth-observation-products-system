package com.astrium.hmas.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.astrium.hmas.bean.CatalogueResult;
import com.astrium.hmas.bean.CatalogueSearch;
import com.astrium.hmas.bean.Point;
import com.astrium.hmas.client.CatalogueService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@SuppressWarnings("serial")
public class CatalogueServiceImpl extends RemoteServiceServlet implements
		CatalogueService {

	public Map<String, CatalogueResult> getResults(
			CatalogueSearch catalogueSearch) throws IllegalArgumentException {
		URLConnectionClientHandler ch = new URLConnectionClientHandler(
				new ConnectionFactory());
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT,
				30000);
		clientConfig.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT,
				30000);

		Client client = new Client();
		WebResource webResource = client
				.resource("http://localhost:8080/hmas_server-1.0-SNAPSHOT/hmas/os");
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();

		queryParams.add("startIndex", "0");
		if(catalogueSearch.parameters.get("time:start") != null){
			queryParams.add(catalogueSearch.parameters.get("time:start"),
					catalogueSearch.getStart());
		}	
		if(catalogueSearch.parameters.get("time:end") != null){
			queryParams.add(catalogueSearch.parameters.get("time:end"),
					catalogueSearch.getStop());
		}
		//bbox -> must be LBRU
		queryParams.add("bbox", catalogueSearch.getSelat() + ","
				+ catalogueSearch.getSelon() + "," + catalogueSearch.getNwlat()
				+ "," + catalogueSearch.getNwlon());
		if (catalogueSearch.parameters.get("eo:platform")  != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:platform"),
					catalogueSearch.getPlatform());
		}
		if (catalogueSearch.parameters.get("eo:orbitType")  != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:orbitType"),
					catalogueSearch.getOrbitType());
		}
		if (catalogueSearch.parameters.get("eo:instrument")  != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:instrument"),
					catalogueSearch.getInstrument());
		}
		if (catalogueSearch.parameters.get("eo:sensorType")  != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:sensorType"),
					catalogueSearch.getSensorType());
		}
		if (catalogueSearch.parameters.get("eo:sensorMode") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:sensorMode"),
					catalogueSearch.getSensorMode());
		}
		if (catalogueSearch.parameters.get("eo:resolution") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:resolution"),
					String.valueOf(catalogueSearch.getResolution_max()));
		}
		if (catalogueSearch.parameters.get("eo:swathId") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:swathId"),
					String.valueOf(catalogueSearch.getSwathId()));
		}
		if (catalogueSearch.parameters.get("eo:wavelength") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:wavelength"),
					String.valueOf(catalogueSearch.getWaveLenght_max()));
		}
		if (catalogueSearch.parameters.get("eo:spectralRange")  != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:spectralRange"),
					String.valueOf(catalogueSearch.getSpectralRange()));
		}
		if (catalogueSearch.parameters.get("eo:orbitNumber") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:orbitNumber"),
					String.valueOf(catalogueSearch.getOrbitNumber_max()));
		}
		if (catalogueSearch.parameters.get("eo:orbitDirection") != null) {
			queryParams.add(
					catalogueSearch.parameters.get("eo:orbitDirection"),
					String.valueOf(catalogueSearch.getOrbitDirection()));
		}
		if (catalogueSearch.parameters.get("eo:track") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:track"),
					String.valueOf(catalogueSearch.getTrack_across()));
		}
		if (catalogueSearch.parameters.get("eo:frame") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:frame"),
					String.valueOf(catalogueSearch.getFrame_max()));
		}
		if (catalogueSearch.parameters.get("eo:identifier") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:identifier"),
					String.valueOf(catalogueSearch.getIdentifier()));
		}
		if (catalogueSearch.parameters.get("eo:type") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:type"),
					String.valueOf(catalogueSearch.getEntryType()));
		}
		if (catalogueSearch.parameters.get("eo:acquisitionType") != null) {
			queryParams.add(
					catalogueSearch.parameters.get("eo:acquisitionType"),
					String.valueOf(catalogueSearch.getAcquisitionType()));
		}
		if (catalogueSearch.parameters.get("eo:status")  != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:status"),
					String.valueOf(catalogueSearch.getStatus()));
		}
		if (catalogueSearch.parameters.get("eo:archivingCenter") != null) {
			queryParams.add(
					catalogueSearch.parameters.get("eo:archivingCenter"),
					String.valueOf(catalogueSearch.getArchivingCenter()));
		}
		if (catalogueSearch.parameters.get("eo:acquisitionStation") != null) {
			queryParams.add(
					catalogueSearch.parameters.get("eo:acquisitionStation"),
					String.valueOf(catalogueSearch.getAcquisitionStation()));
		}
		if (catalogueSearch.parameters.get("eo:ProcessingCenter")  != null) {
			queryParams.add(
					catalogueSearch.parameters.get("eo:ProcessingCenter"),
					String.valueOf(catalogueSearch.getProcessingCenter()));
		}
		if (catalogueSearch.parameters.get("eo:processingSoftware") != null) {
			queryParams.add(
					catalogueSearch.parameters.get("eo:processingSoftware"),
					String.valueOf(catalogueSearch.getProcessingSoftware()));
		}
		if (catalogueSearch.parameters.get("eo:processingDate") != null) {
			queryParams.add(
					catalogueSearch.parameters.get("eo:processingDate"),
					String.valueOf(catalogueSearch.getProcessingDate_start()));
		}
		if (catalogueSearch.parameters.get("eo:processingLevel") != null) {
			queryParams.add(
					catalogueSearch.parameters.get("eo:processingLevel"),
					String.valueOf(catalogueSearch.getProcessingLevel()));
		}
		if (catalogueSearch.parameters.get("eo:compositeType") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:compositeType"),
					String.valueOf(catalogueSearch.getCompositeType()));
		}
		if (catalogueSearch.parameters.get("eo:contents") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:contents"),
					String.valueOf(catalogueSearch.getContents()));
		}
		if (catalogueSearch.parameters.get("eo:cloudCover") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:cloudCover"),
					String.valueOf(catalogueSearch.getCloudCover()));
		}
		if (catalogueSearch.parameters.get("eo:snowCover") != null) {
			queryParams.add(catalogueSearch.parameters.get("eo:snowCover"),
					String.valueOf(catalogueSearch.getSnowCover()));
		}

		String s = webResource.queryParams(queryParams)
				.accept("application/atom+xml").get(String.class);
		
		String url = webResource.queryParams(queryParams).toString();

		Map<String, CatalogueResult> results = new HashMap<String, CatalogueResult>();

		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;

			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(new InputSource(
					new ByteArrayInputStream(s.getBytes("utf-8"))));

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList nlist = doc.getElementsByTagName("entry");

			if (nlist != null && nlist.getLength() > 0) {
				for (int i = 0; i < nlist.getLength(); i++) {
					CatalogueResult entry = new CatalogueResult();
					entry.setXml(url);
					Element param = (Element) nlist.item(i);

					Element id = (Element) param.getElementsByTagName("id")
							.item(0);
					String idtxt = id.getChildNodes().item(0).getNodeValue();
					entry.setId(idtxt);

					Element title = (Element) param.getElementsByTagName(
							"title").item(0);
					String titletxt = title.getChildNodes().item(0)
							.getNodeValue();
					entry.setTitle(titletxt);

					Element earthObs = (Element) param.getElementsByTagName(
							"eop:EarthObservation").item(0);

					if (earthObs == null) {
						System.out.println("no Earth Observation -> GML");

						Element where = (Element) param.getElementsByTagName(
								"georss:where").item(0);

						Element polygon = (Element) where.getElementsByTagName(
								"gml:Polygon").item(0);

						Element exterior = (Element) polygon
								.getElementsByTagName("gml:exterior").item(0);

						Element linearRing = (Element) exterior
								.getElementsByTagName("gml:LinearRing").item(0);

						Element position = (Element) linearRing
								.getElementsByTagName("gml:posList").item(0);
						// TODO spliter le string pour en faire des coordonnées
						String positiontxt = position.getChildNodes().item(0)
								.getNodeValue();
						String[] coords = positiontxt.split(" ");
						Point upperRight = new Point(
								Double.parseDouble(coords[0]),
								Double.parseDouble(coords[1]));
						Point upperLeft = new Point(
								Double.parseDouble(coords[2]),
								Double.parseDouble(coords[3]));
						Point lowerLeft = new Point(
								Double.parseDouble(coords[4]),
								Double.parseDouble(coords[5]));
						Point lowerRight = new Point(
								Double.parseDouble(coords[6]),
								Double.parseDouble(coords[7]));

						entry.setUpperRight(upperRight);
						entry.setUpperLeft(upperLeft);
						entry.setLowerRight(lowerRight);
						entry.setLowerLeft(lowerLeft);

						Element validTime = (Element) param
								.getElementsByTagName("gml:validTime").item(0);
						if (validTime == null) {
							System.out.println("no valid time");
						} else {
							Element timePeriod = (Element) validTime
									.getElementsByTagName("gml:TimePeriod")
									.item(0);

							if (timePeriod == null) {
								System.out.println("no time period");
							} else {
								Element start = (Element) timePeriod
										.getElementsByTagName(
												"gml:beginPosition").item(0);
								String starttxt = start.getChildNodes().item(0)
										.getNodeValue();
								entry.setStart(starttxt);

								Element end = (Element) timePeriod
										.getElementsByTagName("gml:endPosition")
										.item(0);
								String endtxt = end.getChildNodes().item(0)
										.getNodeValue();
								entry.setEnd(endtxt);
							}

						}

						Element content = (Element) param.getElementsByTagName(
								"content").item(0);
						String contenttxt = content.getChildNodes().item(0)
								.getNodeValue();
						String res = contenttxt.replaceAll("&lt;", "<");
						res = res.replaceAll("&gt;", ">");
						/*
						 * Document doc_content = dBuilder.parse(new
						 * InputSource(new
						 * ByteArrayInputStream(res.getBytes("utf-8"))));
						 * 
						 * //optional, but recommended //read this -
						 * http://stackoverflow
						 * .com/questions/13786607/normalization
						 * -in-dom-parsing-with-java-how-does-it-work
						 * doc_content.getDocumentElement().normalize();
						 * 
						 * Element ul =
						 * (Element)doc_content.getElementsByTagName
						 * ("ul").item(0); Element orbitNumber =
						 * (Element)ul.getElementsByTagName("li").item(1);
						 * String orbitNumbertxt =
						 * orbitNumber.getChildNodes().item(0).getNodeValue();
						 * String[] on = orbitNumbertxt.split(":"); String
						 * orbitNumberValue = on[1];
						 * entry.setOrbitNumber(orbitNumberValue);
						 * 
						 * Element processingCenter =
						 * (Element)ul.getElementsByTagName("li").item(2);
						 * String processingCentertxt =
						 * processingCenter.getChildNodes
						 * ().item(0).getNodeValue(); String[] pc =
						 * processingCentertxt.split(":"); String
						 * processingCenterValue = pc[1];
						 * entry.setProcessingCenter(processingCenterValue);
						 * 
						 * Element processingDate =
						 * (Element)ul.getElementsByTagName("li").item(3);
						 * String processingDatetxt =
						 * processingDate.getChildNodes
						 * ().item(0).getNodeValue(); String[] pd =
						 * processingDatetxt.split(":"); String
						 * processingDateValue = pd[1];
						 * entry.setProcessingDate(processingDateValue);
						 */

					} else {

						Element time = (Element) earthObs.getElementsByTagName(
								"om:phenomenonTime").item(0);

						if (time == null) {
							// TODO regarder si c'est mandatory
							// do nothing
							System.out.println("no time");
						} else {

							Element timePeriod = (Element) time
									.getElementsByTagName("gml:TimePeriod")
									.item(0);

							if (timePeriod == null) {
								System.out.println("no time period");
							} else {
								Element start = (Element) timePeriod
										.getElementsByTagName(
												"gml:beginPosition").item(0);
								String starttxt = start.getChildNodes().item(0)
										.getNodeValue();
								entry.setStart(starttxt);

								Element end = (Element) timePeriod
										.getElementsByTagName("gml:endPosition")
										.item(0);
								String endtxt = end.getChildNodes().item(0)
										.getNodeValue();
								entry.setEnd(endtxt);
							}

						}

						Element resultTime = (Element) earthObs
								.getElementsByTagName("om:resultTime").item(0);

						if (resultTime == null) {
							System.out.println("no result time");
						} else {
							Element instantTime = (Element) resultTime
									.getElementsByTagName("gml:TimeInstant")
									.item(0);

							if (instantTime == null) {
								System.out.println("no instant time");
							} else {
								Element positionTime = (Element) instantTime
										.getElementsByTagName(
												"gml:timePosition").item(0);
								String positionTimetxt = positionTime
										.getChildNodes().item(0).getNodeValue();
								entry.setTime(positionTimetxt);
							}

						}

						Element procedure = (Element) earthObs
								.getElementsByTagName("om:procedure").item(0);

						if (procedure == null) {
							System.out.println("no procedure");
						} else {

							Element eoEquipment = (Element) procedure
									.getElementsByTagName(
											"eop:EarthObservationEquipment")
									.item(0);

							if (eoEquipment == null) {
								System.out.println("no equipment");
							} else {

								Element platform = (Element) eoEquipment
										.getElementsByTagName("eop:platform")
										.item(0);

								if (platform == null) {
									System.out.println("no platform");
								} else {
									Element platformElmt = (Element) platform
											.getElementsByTagName(
													"eop:Platform").item(0);

									Element platformName = (Element) platformElmt
											.getElementsByTagName(
													"eop:shortName").item(0);
									if (platformName == null) {
										System.out.println("no platform name");
									} else {
										String platformNametxt = platformName
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setPlatform(platformNametxt);
									}

									Element orbitType = (Element) platformElmt
											.getElementsByTagName(
													"eop:orbitType").item(0);
									if (orbitType == null) {
										System.out.println("no orbit type");
									} else {
										String orbitTypeetxt = orbitType
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setOrbitType(orbitTypeetxt);
									}

								}

								Element instrument = (Element) eoEquipment
										.getElementsByTagName("eop:instrument")
										.item(0);

								if (instrument == null) {
									System.out.println("no instrument");
								} else {

									Element instrumentElmt = (Element) instrument
											.getElementsByTagName(
													"eop:Instrument").item(0);

									Element instrumentName = (Element) instrumentElmt
											.getElementsByTagName(
													"eop:shortName").item(0);
									String instrumentNametxt = instrumentName
											.getChildNodes().item(0)
											.getNodeValue();
									entry.setInstrument(instrumentNametxt);
								}

								Element sensor = (Element) eoEquipment
										.getElementsByTagName("eop:sensor")
										.item(0);

								if (sensor == null) {
									System.out.println("no sensor");
								} else {

									Element sensorElmt = (Element) sensor
											.getElementsByTagName("eop:Sensor")
											.item(0);

									Element sensorType = (Element) sensorElmt
											.getElementsByTagName(
													"eop:sensorType").item(0);

									if (sensorType == null) {
										System.out.println("no sensor type");
									} else {
										String sensorTypetxt = sensorType
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setSensorType(sensorTypetxt);
									}

									Element sensorMode = (Element) sensorElmt
											.getElementsByTagName(
													"eop:operationalMode")
											.item(0);

									if (sensorMode == null) {
										System.out.println("no sensor mode");
									} else {
										String sensorModetxt = sensorMode
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setSensorMode(sensorModetxt);
									}

									Element resolution = (Element) sensorElmt
											.getElementsByTagName(
													"eop:resolution").item(0);

									if (resolution == null) {
										System.out.println("no resolution");
									} else {
										String resolutiontxt = resolution
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setResolution(resolutiontxt);
									}

									Element swathId = (Element) sensorElmt
											.getElementsByTagName(
													"eop:swathIdentifier")
											.item(0);

									if (swathId == null) {
										System.out.println("no swath id");
									} else {
										String swathIdtxt = swathId
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setSwathId(swathIdtxt);
									}

									Element waveLengthInfo = (Element) sensorElmt
											.getElementsByTagName(
													"eop:wavelengthInformation")
											.item(0);

									if (waveLengthInfo == null) {
										System.out
												.println("no wave lenght info");
									} else {
										Element waveLengthInfoElmt = (Element) waveLengthInfo
												.getElementsByTagName(
														"eop:WavelengthInformation")
												.item(0);

										Element discreteWaveLengthInfo = (Element) waveLengthInfoElmt
												.getElementsByTagName(
														"eop:discreteWavelengths")
												.item(0);

										if (discreteWaveLengthInfo == null) {
											System.out
													.println("no discrete wave lenght info");
										} else {
											String discreteWaveLengthInfotxt = discreteWaveLengthInfo
													.getChildNodes().item(0)
													.getNodeValue();
											entry.setWaveLenght(discreteWaveLengthInfotxt);
										}

										Element spectralRange = (Element) waveLengthInfoElmt
												.getElementsByTagName(
														"eop:spectralRange")
												.item(0);

										if (spectralRange == null) {
											System.out
													.println("no spectral range");
										} else {
											String spectralRangetxt = discreteWaveLengthInfo
													.getChildNodes().item(0)
													.getNodeValue();
											entry.setSpectralRange(spectralRangetxt);
										}

									}

								}

								Element acquisitionParameters = (Element) eoEquipment
										.getElementsByTagName(
												"eop:acquisitionParameters")
										.item(0);

								if (acquisitionParameters == null) {
									System.out
											.println("no acquisition parameters");
								} else {
									Element acquisition = (Element) acquisitionParameters
											.getElementsByTagName(
													"eop:Acquisition").item(0);

									Element orbitNumber = (Element) acquisition
											.getElementsByTagName(
													"eop:orbitNumber").item(0);

									if (orbitNumber == null) {
										System.out.println("no orbit number");
									} else {
										String orbitNumbertxt = orbitNumber
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setOrbitNumber(orbitNumbertxt);
									}

									Element orbitDirection = (Element) acquisition
											.getElementsByTagName(
													"eop:orbitDirection").item(
													0);

									if (orbitDirection == null) {
										System.out
												.println("no orbit direction");
									} else {
										String orbitDirectiontxt = orbitDirection
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setOrbitDirection(orbitDirectiontxt);
									}

									Element track = (Element) acquisition
											.getElementsByTagName(
													"eop:wrsLongitudeGrid")
											.item(0);

									if (track == null) {
										System.out.println("no track");
									} else {
										String tracktxt = track.getChildNodes()
												.item(0).getNodeValue();
										entry.setTrack(tracktxt);
									}

									Element frame = (Element) acquisition
											.getElementsByTagName(
													"eop:wrsLatitudeGrid")
											.item(0);

									if (frame == null) {
										System.out.println("no frame");
									} else {
										String frametxt = frame.getChildNodes()
												.item(0).getNodeValue();
										entry.setFrame(frametxt);
									}
								}

							}

						}

						// TODO Toujours non null??
						Element featureOfInterest = (Element) earthObs
								.getElementsByTagName("om:featureOfInterest")
								.item(0);
						if (featureOfInterest == null) {
							System.out.println("no feature of interest");

						} else {
							Element footprint = (Element) featureOfInterest
									.getElementsByTagName("eop:Footprint")
									.item(0);

							Element multiExtentof = (Element) footprint
									.getElementsByTagName("eop:multiExtentOf")
									.item(0);

							Element multiSurface = (Element) multiExtentof
									.getElementsByTagName("gml:MultiSurface")
									.item(0);

							Element polygon = (Element) multiSurface
									.getElementsByTagName("gml:Polygon")
									.item(0);

							Element exterior = (Element) polygon
									.getElementsByTagName("gml:exterior").item(
											0);

							Element linearRing = (Element) exterior
									.getElementsByTagName("gml:LinearRing")
									.item(0);

							Element position = (Element) linearRing
									.getElementsByTagName("gml:posList")
									.item(0);
							// TODO spliter le string pour en faire des
							// coordonnées
							String positiontxt = position.getChildNodes()
									.item(0).getNodeValue();
							String[] coords = positiontxt.split(" ");
							Point upperRight = new Point(
									Double.parseDouble(coords[0]),
									Double.parseDouble(coords[1]));
							Point upperLeft = new Point(
									Double.parseDouble(coords[2]),
									Double.parseDouble(coords[3]));
							Point lowerLeft = new Point(
									Double.parseDouble(coords[4]),
									Double.parseDouble(coords[5]));
							Point lowerRight = new Point(
									Double.parseDouble(coords[6]),
									Double.parseDouble(coords[7]));

							entry.setUpperRight(upperRight);
							entry.setUpperLeft(upperLeft);
							entry.setLowerRight(lowerRight);
							entry.setLowerLeft(lowerLeft);
						}

						Element metadataProperty = (Element) earthObs
								.getElementsByTagName("eop:metaDataProperty")
								.item(0);

						if (metadataProperty == null) {
							System.out.println("no metadata property");
						} else {

							Element eoMetadata = (Element) metadataProperty
									.getElementsByTagName(
											"eop:EarthObservationMetaData")
									.item(0);

							Element identifier = (Element) eoMetadata
									.getElementsByTagName("eop:identifier")
									.item(0);
							if (identifier == null) {
								System.out.println("no identifier");
							} else {
								String identifiertxt = identifier
										.getChildNodes().item(0).getNodeValue();
								entry.setIdentifier(identifiertxt);
							}

							Element acquisitionType = (Element) eoMetadata
									.getElementsByTagName("eop:acquisitionType")
									.item(0);
							if (acquisitionType == null) {
								System.out.println("no acquisition type");
							} else {
								String acquisitionTypetxt = acquisitionType
										.getChildNodes().item(0).getNodeValue();
								entry.setAcquisitionType(acquisitionTypetxt);
							}

							Element productType = (Element) eoMetadata
									.getElementsByTagName("eop:productType")
									.item(0);
							if (productType == null) {
								System.out.println("no type");
							} else {
								String productTypetxt = productType
										.getChildNodes().item(0).getNodeValue();
								entry.setProductType(productTypetxt);
							}

							Element status = (Element) eoMetadata
									.getElementsByTagName("eop:status").item(0);
							if (status == null) {
								System.out.println("no status");
							} else {
								String statustxt = status.getChildNodes()
										.item(0).getNodeValue();
								entry.setStatus(statustxt);
							}

							Element archivedIn = (Element) eoMetadata
									.getElementsByTagName("eop:archivedIn")
									.item(0);
							if (archivedIn == null) {
								System.out.println("no achiving information");
							} else {
								Element archivingInfo = (Element) archivedIn
										.getElementsByTagName(
												"eop:ArchivingInformation")
										.item(0);

								Element archivingCenter = (Element) archivingInfo
										.getElementsByTagName(
												"eop:archivingCenter").item(0);
								if (archivingCenter == null) {
									System.out.println("no archiving center");
								} else {
									String archivingCentertxt = archivingCenter
											.getChildNodes().item(0)
											.getNodeValue();
									entry.setArchivingCenter(archivingCentertxt);
								}

								Element archivingDate = (Element) archivingInfo
										.getElementsByTagName(
												"eop:archivingDate").item(0);
								if (archivingDate == null) {
									System.out.println("no archiving date");
								} else {
									String archivingDatetxt = archivingDate
											.getChildNodes().item(0)
											.getNodeValue();
									entry.setArchivingDate(archivingDatetxt);
								}
							}

							Element link = (Element) eoMetadata
									.getElementsByTagName("eop:downlinkedTo")
									.item(0);

							if (link == null) {
								System.out.println("no link");
							} else {
								Element linkInfo = (Element) link
										.getElementsByTagName(
												"eop:DownlinkInformation")
										.item(0);

								Element acquisitionStation = (Element) linkInfo
										.getElementsByTagName(
												"eop:acquisitionStation").item(
												0);
								String acquisitionStationtxt = acquisitionStation
										.getChildNodes().item(0).getNodeValue();
								entry.setAcquisitionStation(acquisitionStationtxt);
							}

							Element processing = (Element) eoMetadata
									.getElementsByTagName("eop:processing")
									.item(0);
							if (processing == null) {
								System.out.println("no processing info");
							} else {
								Element processingInfoEOP = (Element) processing
										.getElementsByTagName(
												"eop:ProcessingInformation")
										.item(0);

								if (processingInfoEOP == null) {
									System.out
											.println("no processing info eop");
								} else {

									Element processingCenter = (Element) processingInfoEOP
											.getElementsByTagName(
													"eop:processingCenter")
											.item(0);
									if (processingCenter == null) {
										System.out
												.println("no processing center");
									} else {
										String processingCentertxt = processingCenter
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setProcessingCenter(processingCentertxt);
									}

									Element processingSoftware = (Element) processingInfoEOP
											.getElementsByTagName(
													"eop:processingSoftware")
											.item(0);
									if (processingSoftware == null) {
										System.out
												.println("no processing software");
									} else {
										String processingSoftwaretxt = processingSoftware
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setProcessingSoftware(processingSoftwaretxt);
									}

									Element processingDate = (Element) processingInfoEOP
											.getElementsByTagName(
													"eop:processingDate").item(
													0);
									if (processingDate == null) {
										System.out
												.println("no processing date");
									} else {
										String processingDatetxt = processingDate
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setProcessingDate(processingDatetxt);
									}

									Element processingLevel = (Element) processingInfoEOP
											.getElementsByTagName(
													"eop:processingLevel")
											.item(0);
									if (processingLevel == null) {
										System.out
												.println("no processing level");
									} else {
										String processingLeveltxt = processingLevel
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setProcessingLevel(processingLeveltxt);
									}

									Element compositeType = (Element) processingInfoEOP
											.getElementsByTagName(
													"eop:compositeType")
											.item(0);
									if (compositeType == null) {
										System.out.println("no composite type");
									} else {
										String compositeTypetxt = compositeType
												.getChildNodes().item(0)
												.getNodeValue();
										entry.setCompositeType(compositeTypetxt);
									}

								}

								Element processingInfoALT = (Element) processing
										.getElementsByTagName(
												"alt:ProcessingInformation")
										.item(0);
								if (processingInfoALT == null) {
									System.out
											.println("no processing info alt");
								} else {
									Element contents = (Element) processingInfoALT
											.getElementsByTagName(
													"alt:productContentsType")
											.item(0);
									String contentstxt = contents
											.getChildNodes().item(0)
											.getNodeValue();
									entry.setContents(contentstxt);
								}

							}

						}

						Element result = (Element) earthObs
								.getElementsByTagName("om:result").item(0);
						if (result == null) {
							System.out.println("no result");
						} else {
							Element eoResult = (Element) result
									.getElementsByTagName(
											"opt:EarthObservationResult").item(
											0);
							if (eoResult == null) {
								System.out.println("no eo result");
							} else {
								Element cloudCover = (Element) eoResult
										.getElementsByTagName(
												"opt:cloudCoverPercentage")
										.item(0);
								if (cloudCover == null) {
									System.out.println("no cloud cover");
								} else {
									String cloudCovertxt = cloudCover
											.getChildNodes().item(0)
											.getNodeValue();
									entry.setCloudCover(cloudCovertxt);
								}

								Element snowCover = (Element) eoResult
										.getElementsByTagName(
												"opt:snowCoverPercentage")
										.item(0);
								if (snowCover == null) {
									System.out.println("no cloud cover");
								} else {
									String snowCovertxt = snowCover
											.getChildNodes().item(0)
											.getNodeValue();
									entry.setSnowCover(snowCovertxt);
								}
							}
						}
						
						
					}

					
				results.put(String.valueOf(i), entry);
				}
				
			} else {
				System.out.println("no result");
				
			}

		} catch (ParserConfigurationException e) {// TODO Auto-generated catch
													// block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

}
