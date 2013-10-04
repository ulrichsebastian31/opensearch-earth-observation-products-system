package com.astrium.hmas.server.FeasibilityService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FeasibilityServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Feasibility Service interface 
 *   																	implementation
 *   																	Parses the XML response from the 
 *   																	Feasibility Search server and return a map
 *   																	of FeasibilityResult objects
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.astrium.hmas.bean.Point;
import com.astrium.hmas.bean.FeasibilityBean.FeasibilityResult;
import com.astrium.hmas.bean.FeasibilityBean.FeasibilitySearch;
import com.astrium.hmas.client.FeasibilityService.FeasibilityService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@SuppressWarnings("serial")
public class FeasibilityServiceImpl extends RemoteServiceServlet implements FeasibilityService {

	@Override
	public Map<String, FeasibilityResult> getResults(FeasibilitySearch feasibilitySearch) throws IllegalArgumentException {
		// TODO Auto-generated method stub

		/*
		 * Jersey Client creation
		 */
		Client client = new Client();
		/*
		 * Call to the server
		 */
		WebResource webResource = client.resource("http://127.0.0.1:8080/HMAS-FAS-1.0-SNAPSHOT/hmas/fas/os/search");
		/*
		 * parameters map
		 */
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();

		/*
		 * Add all the parameters to the request to the server : we get the
		 * param key from the FeasibilitySearch object's attribute "parameters"
		 * if it's not null
		 */
		if (feasibilitySearch.parameters.get("time:start") != null) {

			queryParams.add(feasibilitySearch.parameters.get("time:start"), feasibilitySearch.getStart());

		}
		if (feasibilitySearch.parameters.get("time:end") != null) {

			queryParams.add(feasibilitySearch.parameters.get("time:end"), feasibilitySearch.getEnd());

		}
		if (feasibilitySearch.parameters.get("eo:platform") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eo:platform"), feasibilitySearch.getPlatform());

		}
		if (feasibilitySearch.parameters.get("eo:sensorType") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eo:sensorType"), feasibilitySearch.getSensorType());

		}
		if (feasibilitySearch.parameters.get("eo:instrument") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eo:instrument"), feasibilitySearch.getInstrument());

		}
		if (feasibilitySearch.parameters.get("eo:resolution") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eo:resolution"), feasibilitySearch.getResolution());

		}
		if (feasibilitySearch.parameters.get("geo:box") != null) {

			queryParams.add(feasibilitySearch.parameters.get("geo:box"), feasibilitySearch.getNwlon() + "," + feasibilitySearch.getSelat() + ","
					+ feasibilitySearch.getSelon() + "," + feasibilitySearch.getNwlat());

		}
		if (feasibilitySearch.parameters.get("eosp:acquisitionAngleIncidenceAzimuth") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionAngleIncidenceAzimuth"), feasibilitySearch.getAzimuth());

		}
		if (feasibilitySearch.parameters.get("eosp:acquisitionAngleIncidenceElevation") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionAngleIncidenceElevation"), feasibilitySearch.getElevation());

		}
		if (feasibilitySearch.parameters.get("eosp:acquisitionAnglePointingRangeAlongTrack") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionAnglePointingRangeAlongTrack"), feasibilitySearch.getTrackAlong());

		}
		if (feasibilitySearch.parameters.get("eosp:acquisitionAnglePointingRangeAcrossTrack") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionAnglePointingRangeAcrossTrack"), feasibilitySearch.getTrackAcross());

		}
		if (feasibilitySearch.parameters.get("eo:sensorMode") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eo:sensorMode"), feasibilitySearch.getSensorMode());

		}
		if (feasibilitySearch.parameters.get("eo:compositeType") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eo:compositeType"), feasibilitySearch.getCompositeType());

		}
		if (feasibilitySearch.parameters.get("eosp:acquisitionParametersOPTMinimumLuminosity") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionParametersOPTMinimumLuminosity"), feasibilitySearch.getMinLuminosity());

		}
		if (feasibilitySearch.parameters.get("eosp:acquisitionParametersSARPolarizationMode") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionParametersSARPolarizationMode"), feasibilitySearch.getPolMode());

		}
		if (feasibilitySearch.parameters.get("eo:cloudCover") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eo:cloudCover"), feasibilitySearch.getCloudCover());

		}
		if (feasibilitySearch.parameters.get("eo:snowCover") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eo:snowCover"), feasibilitySearch.getSnowCover());

		}
		if (feasibilitySearch.parameters.get("eosp:validationParametersOPTmaxSunGlint") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eosp:validationParametersOPTmaxSunGlint"), feasibilitySearch.getMaxSunGlint());

		}
		if (feasibilitySearch.parameters.get("eosp:validationParametersOPTHazeAccepted") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eosp:validationParametersOPTHazeAccepted"), feasibilitySearch.getHaze());

		}
		if (feasibilitySearch.parameters.get("eosp:validationParametersOPTSandWindAccepted") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eosp:validationParametersOPTSandWindAccepted"), feasibilitySearch.getSandWind());

		}
		if (feasibilitySearch.parameters.get("eosp:validationParametersSARMaximumNoiseLevel") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eosp:validationParametersSARMaximumNoiseLevel"), feasibilitySearch.getMaxNoiseLevel());

		}
		if (feasibilitySearch.parameters.get("eosp:validationParametersSARMaxAmbiguityLevel") != null) {

			queryParams.add(feasibilitySearch.parameters.get("eosp:validationParametersSARMaxAmbiguityLevel"), feasibilitySearch.getMaxAmbiguityLevel());

		}

		/*
		 * Get the response : the XML file
		 */
		String xml = webResource.queryParams(queryParams).accept("application/atom+xml").get(String.class);

		/*
		 * URL with all the parameters
		 */
		String url = webResource.queryParams(queryParams).toString();
		System.out.println(url);

		/*
		 * The object where we will put the results from the XML response
		 */
		Map<String, FeasibilityResult> results = new HashMap<String, FeasibilityResult>();

		try {
			/*
			 * ********************** XML RESPONSE PARSING ********************
			 */
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

			doc.getDocumentElement().normalize();

			/*
			 * Get the "entry" element -> correspond to a FeasibilityResult
			 * object
			 */
			NodeList nlist = doc.getElementsByTagName("atom:entry");

			if (nlist != null && nlist.getLength() > 0) {

				for (int i = 0; i < nlist.getLength(); i++) {

					FeasibilityResult entry = new FeasibilityResult();

					Element param = (Element) nlist.item(i);

					entry.setXml(url);

					Element id = (Element) param.getElementsByTagName("atom:id").item(0);
					String idtxt = id.getChildNodes().item(0).getNodeValue();
					entry.setIdentifier(idtxt);

					Element title = (Element) param.getElementsByTagName("atom:title").item(0);
					String titletxt = title.getChildNodes().item(0).getNodeValue();
					entry.setTitle(titletxt);

					/*
					 * GetFeasibilityResponseDocument document =
					 * GetFeasibilityResponseDocument.Factory.parse(param);
					 * FeasibilityStudyType extension =
					 * document.getGetFeasibilityResponse
					 * ().getExtensionArray()[0]; SegmentPropertyType
					 * segmentProp = extension.getSegmentArray()[0];
					 */

					Element getResponse = (Element) param.getElementsByTagName("ns:GetFeasibilityResponse").item(0);
					if (getResponse == null) {
						System.out.println("no feasibility response");
					} else {
						Element extension = (Element) getResponse.getElementsByTagName("ns1:extension").item(0);

						if (extension == null) {
							System.out.println("no extension");
						} else {
							NodeList segment = extension.getElementsByTagName("ns:segment");
							/*
							 * There can be several segment for one entry
							 */
							if (segment != null && segment.getLength() > 0) {

								List<Point> lowerLeft = new ArrayList<Point>();
								List<Point> lowerRight = new ArrayList<Point>();
								List<Point> upperLeft = new ArrayList<Point>();
								List<Point> upperRight = new ArrayList<Point>();

								for (int j = 0; j < segment.getLength(); j++) {
									Element seg = (Element) segment.item(j);
									Element segmentElmt = (Element) seg.getElementsByTagName("ns:Segment").item(0);

									Element footprint = (Element) segmentElmt.getElementsByTagName("ns:footprint").item(0);

									if (footprint == null) {

										System.out.println("no footprint");

									} else {
										Element polygon = (Element) footprint.getElementsByTagName("ns2:Polygon").item(0);

										Element exterior = (Element) polygon.getElementsByTagName("ns2:exterior").item(0);

										Element abstractRing = (Element) exterior.getElementsByTagName("ns:AbstractRing").item(0);

										Element coordinates = (Element) abstractRing.getElementsByTagName("ns:coordinates").item(0);
										String coordinatestxt = coordinates.getChildNodes().item(0).getNodeValue();
										String[] coords = coordinatestxt.split(" ");

										String[] coordPointLL = coords[0].split(",");
										Point lowerLeftPoint = new Point();
										lowerLeftPoint.setLatitude(Double.parseDouble(coordPointLL[0]));
										lowerLeftPoint.setLongitude(Double.parseDouble(coordPointLL[1]));
										lowerLeft.add(lowerLeftPoint);

										String[] coordPointLR = coords[1].split(",");
										Point lowerRightPoint = new Point();
										lowerRightPoint.setLatitude(Double.parseDouble(coordPointLR[0]));
										lowerRightPoint.setLongitude(Double.parseDouble(coordPointLR[1]));
										lowerRight.add(lowerRightPoint);

										String[] coordPointUR = coords[2].split(",");
										Point upperRightPoint = new Point();
										upperRightPoint.setLatitude(Double.parseDouble(coordPointUR[0]));
										upperRightPoint.setLongitude(Double.parseDouble(coordPointUR[1]));
										upperRight.add(upperRightPoint);

										String[] coordPointUL = coords[3].split(",");
										Point upperLeftPoint = new Point();
										upperLeftPoint.setLatitude(Double.parseDouble(coordPointUL[0]));
										upperLeftPoint.setLongitude(Double.parseDouble(coordPointUL[1]));
										upperLeft.add(upperLeftPoint);

									}

									Element startTime = (Element) segmentElmt.getElementsByTagName("ns:acquisitionStartTime").item(0);
									if (startTime == null) {
										System.out.println("no start time");
									} else {
										String startTimetxt = startTime.getChildNodes().item(0).getNodeValue();
										String acquisitionTime = startTimetxt;
										Element endTime = (Element) segmentElmt.getElementsByTagName("ns:acquisitionStopTime").item(0);
										if (endTime == null) {
											System.out.println("no end time");
										} else {
											String endTimetxt = endTime.getChildNodes().item(0).getNodeValue();
											acquisitionTime += "-" + endTimetxt;
										}
										entry.setAcquisitionDate(acquisitionTime);
									}

									Element acquisitionMethod = (Element) segmentElmt.getElementsByTagName("ns:acquisitionMethod").item(0);
									if (acquisitionMethod == null) {
										System.out.println("no acquisition method");
									} else {
										Element eoEquipment = (Element) acquisitionMethod.getElementsByTagName("eop:EarthObservationEquipment").item(0);

										if (eoEquipment == null) {
											System.out.println("no equipment");
										} else {
											Element platform = (Element) eoEquipment.getElementsByTagName("eop:platform").item(0);

											if (platform == null) {
												System.out.println("no platform");
											} else {
												Element platformElmt = (Element) platform.getElementsByTagName("eop:Platform").item(0);

												
												
												
//												Element platformName = (Element) platformElmt.getElementsByTagName("eop:shortName").item(0);
//												if (platformName == null) {
//													System.out.println("no platform name");
//												} else {
//													String platformNametxt = platformName.getChildNodes().item(0).getNodeValue();
//													entry.setPlatform(platformNametxt);
//												}
												

												Element orbitType = (Element) platformElmt.getElementsByTagName("eop:orbitType").item(0);
												if (orbitType == null) {
													System.out.println("no orbit type");
												} else {
													if (orbitType.getChildNodes().item(0) != null) {
														String orbitTypeetxt = orbitType.getChildNodes().item(0).getNodeValue();
														entry.setOrbitType(orbitTypeetxt);
													}

												}

											}

											Element instrument = (Element) eoEquipment.getElementsByTagName("eop:instrument").item(0);

											if (instrument == null) {
												System.out.println("no instrument");
											} else {

												Element instrumentElmt = (Element) instrument.getElementsByTagName("eop:Instrument").item(0);

												Element instrumentName = (Element) instrumentElmt.getElementsByTagName("eop:shortName").item(0);
												String instrumentNametxt = instrumentName.getChildNodes().item(0).getNodeValue();
												entry.setInstrument(instrumentNametxt);
											}

											Element sensor = (Element) eoEquipment.getElementsByTagName("eop:sensor").item(0);

											if (sensor == null) {
												System.out.println("no sensor");
											} else {

												Element sensorElmt = (Element) sensor.getElementsByTagName("eop:Sensor").item(0);

												Element sensorType = (Element) sensorElmt.getElementsByTagName("eop:sensorType").item(0);

												if (sensorType == null) {
													System.out.println("no sensor type");
												} else {
													if (sensorType.getChildNodes().item(0) != null) {
														String sensorTypetxt = sensorType.getChildNodes().item(0).getNodeValue();
														entry.setSensor(sensorTypetxt);
													} else {
														System.out.println("no sensor type text");
														String sensorTypeAtt = sensorType.getAttribute("codeSpace");
														entry.setSensor(sensorTypeAtt);
													}

												}

												Element resolution = (Element) sensorElmt.getElementsByTagName("eop:resolution").item(0);

												if (resolution == null) {
													System.out.println("no resolution");
												} else {
													String resolutiontxt = resolution.getChildNodes().item(0).getNodeValue();
													entry.setResolution(resolutiontxt);
												}

											}
										}
									}
								}

								entry.setUpperLeft(upperLeft);
								entry.setUpperRight(upperRight);
								entry.setLowerLeft(lowerLeft);
								entry.setLowerRight(lowerRight);

								Element result = (Element) getResponse.getElementsByTagName("ns:result").item(0);
								if (result == null) {
									System.out.println("no result");
								} else {
									Element statusReport = (Element) result.getElementsByTagName("ns:StatusReport").item(0);

									Element statusMsg = (Element) statusReport.getElementsByTagName("ns1:statusMessage").item(0);
									if (statusMsg == null) {
										System.out.println("no status msg");
									} else {
										String statusMsgtxt = statusMsg.getChildNodes().item(0).getNodeValue();
										entry.setStatusMsg(statusMsgtxt);
									}

								}
							}

							results.put(String.valueOf(i), entry);

						}
					}
				}
			} else {
				/*
				 * There is no result
				 */
				System.out.println("no result");
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;
	}

}
