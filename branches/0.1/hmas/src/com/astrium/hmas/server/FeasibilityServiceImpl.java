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

import net.opengis.eosps.x20.GetFeasibilityResponseDocument;
import net.opengis.eosps.x20.SegmentPropertyType;
import net.opengis.eosps.x20.SegmentType;
import net.opengis.eosps.x20.FeasibilityStudyType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.astrium.hmas.bean.FeasibilityResult;
import com.astrium.hmas.bean.FeasibilitySearch;
import com.astrium.hmas.bean.Point;
import com.astrium.hmas.client.FeasibilityService;
import com.google.gwt.thirdparty.javascript.jscomp.mozilla.rhino.xml.XMLObject;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@SuppressWarnings("serial")
public class FeasibilityServiceImpl extends RemoteServiceServlet implements
FeasibilityService{

	@Override
	public Map<String, FeasibilityResult> getResults(
			FeasibilitySearch feasibilitySearch)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		
		Client client = new Client();
		WebResource webResource = client
				.resource("http://localhost:8080/hmas_server-1.0-SNAPSHOT/hmas/feas");
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		
		if(feasibilitySearch.parameters.get("time:start") != null){
			queryParams.add(feasibilitySearch.parameters.get("time:start"),
					feasibilitySearch.getStart());
		}
		if(feasibilitySearch.parameters.get("time:end") != null){
			queryParams.add(feasibilitySearch.parameters.get("time:end"),
					feasibilitySearch.getEnd());
		}
		if(feasibilitySearch.parameters.get("eo:platform") != null){
			queryParams.add(feasibilitySearch.parameters.get("eo:platform"),
					feasibilitySearch.getPlatform());
		}
		if(feasibilitySearch.parameters.get("eo:sensorType") != null){
			queryParams.add(feasibilitySearch.parameters.get("eo:sensorType"),
					feasibilitySearch.getSensorType());
		}
		if(feasibilitySearch.parameters.get("eo:instrument") != null){
			queryParams.add(feasibilitySearch.parameters.get("eo:instrument"),
					feasibilitySearch.getInstrument());
		}
		if(feasibilitySearch.parameters.get("eo:resolution") != null){
			queryParams.add(feasibilitySearch.parameters.get("eo:resolution"),
					feasibilitySearch.getResolution());
		}
		if(feasibilitySearch.parameters.get("geo:box") != null){
			queryParams.add(feasibilitySearch.parameters.get("geo:box"),
					feasibilitySearch.getNwlon() + "," + feasibilitySearch.getSelat() + "," + feasibilitySearch.getSelon() + "," + feasibilitySearch.getNwlat());
		}
		if(feasibilitySearch.parameters.get("eosp:acquisitionAngleIncidenceAzimuth") != null){
			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionAngleIncidenceAzimuth"),
					feasibilitySearch.getAzimuth());
		}
		if(feasibilitySearch.parameters.get("eosp:acquisitionAngleIncidenceElevation") != null){
			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionAngleIncidenceElevation"),
					feasibilitySearch.getElevation());
		}
		if(feasibilitySearch.parameters.get("eosp:acquisitionAnglePointingRangeAlongTrack") != null){
			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionAnglePointingRangeAlongTrack"),
					feasibilitySearch.getTrackAlong());
		}
		if(feasibilitySearch.parameters.get("eosp:acquisitionAnglePointingRangeAcrossTrack") != null){
			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionAnglePointingRangeAcrossTrack"),
					feasibilitySearch.getTrackAcross());
		}
		if(feasibilitySearch.parameters.get("eo:sensorMode") != null){
			queryParams.add(feasibilitySearch.parameters.get("eo:sensorMode"),
					feasibilitySearch.getSensorMode());
		}
		if(feasibilitySearch.parameters.get("eo:compositeType") != null){
			queryParams.add(feasibilitySearch.parameters.get("eo:compositeType"),
					feasibilitySearch.getCompositeType());
		}
		if(feasibilitySearch.parameters.get("eosp:acquisitionParametersOPTMinimumLuminosity") != null){
			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionParametersOPTMinimumLuminosity"),
					feasibilitySearch.getMinLuminosity());
		}
		if(feasibilitySearch.parameters.get("eosp:acquisitionParametersSARPolarizationMode") != null){
			queryParams.add(feasibilitySearch.parameters.get("eosp:acquisitionParametersSARPolarizationMode"),
					feasibilitySearch.getPolMode());
		}
		if(feasibilitySearch.parameters.get("eo:cloudCover") != null){
			queryParams.add(feasibilitySearch.parameters.get("eo:cloudCover"),
					feasibilitySearch.getCloudCover());
		}
		if(feasibilitySearch.parameters.get("eo:snowCover") != null){
			queryParams.add(feasibilitySearch.parameters.get("eo:snowCover"),
					feasibilitySearch.getSnowCover());
		}
		if(feasibilitySearch.parameters.get("eosp:validationParametersOPTmaxSunGlint") != null){
			queryParams.add(feasibilitySearch.parameters.get("eosp:validationParametersOPTmaxSunGlint"),
					feasibilitySearch.getMaxSunGlint());
		}
		if(feasibilitySearch.parameters.get("eosp:validationParametersOPTHazeAccepted") != null){
			queryParams.add(feasibilitySearch.parameters.get("eosp:validationParametersOPTHazeAccepted"),
					feasibilitySearch.getHaze());
		}
		if(feasibilitySearch.parameters.get("eosp:validationParametersOPTSandWindAccepted") != null){
			queryParams.add(feasibilitySearch.parameters.get("eosp:validationParametersOPTSandWindAccepted"),
					feasibilitySearch.getSandWind());
		}
		if(feasibilitySearch.parameters.get("eosp:validationParametersSARMaximumNoiseLevel") != null){
			queryParams.add(feasibilitySearch.parameters.get("eosp:validationParametersSARMaximumNoiseLevel"),
					feasibilitySearch.getMaxNoiseLevel());
		}
		if(feasibilitySearch.parameters.get("eosp:validationParametersSARMaxAmbiguityLevel") != null){
			queryParams.add(feasibilitySearch.parameters.get("eosp:validationParametersSARMaxAmbiguityLevel"),
					feasibilitySearch.getMaxAmbiguityLevel());
		}
		
		String xml = webResource.queryParams(queryParams)
				.accept("application/atom+xml").get(String.class);
		
		String url = webResource.queryParams(queryParams).toString();
		System.out.println(url);
		
		Map<String, FeasibilityResult> results = new HashMap<String, FeasibilityResult>();

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
			
			doc.getDocumentElement().normalize();
			
			NodeList nlist = doc.getElementsByTagName("entry");
			
			if (nlist != null && nlist.getLength() > 0) {
				for (int i = 0; i < nlist.getLength(); i++) {
					
					FeasibilityResult entry = new FeasibilityResult();
					
					Element param = (Element) nlist.item(i);
					
					entry.setXml(url);
					
					Element id = (Element) param.getElementsByTagName("id").item(0);
					String idtxt = id.getChildNodes().item(0).getNodeValue();
					entry.setIdentifier(idtxt);
					
					Element title = (Element) param.getElementsByTagName("title").item(0);
					String titletxt = title.getChildNodes().item(0).getNodeValue();
					entry.setTitle(titletxt);
					
					/*GetFeasibilityResponseDocument document = GetFeasibilityResponseDocument.Factory.parse(param);
					FeasibilityStudyType extension = document.getGetFeasibilityResponse().getExtensionArray()[0];
					SegmentPropertyType segmentProp = extension.getSegmentArray()[0];*/
					
					Element getResponse = (Element) param.getElementsByTagName("ns:GetFeasibilityResponse").item(0);
					if(getResponse == null){
						System.out.println("no feasibility response");
					}else {
						Element extension = (Element) getResponse.getElementsByTagName("ns1:extension").item(0);
						if(extension == null){
							System.out.println("no extension");
						} else {
							Element segment = (Element) extension.getElementsByTagName("ns:segment").item(0);
							if(segment == null){
								System.out.println("no segment");
							} else {
								Element segmentElmt = (Element) segment.getElementsByTagName("ns:Segment").item(0);
								
								Element footprint = (Element) segmentElmt.getElementsByTagName("ns:footprint").item(0);
								if(footprint == null){
									System.out.println("no footprint");
								} else {
									Element polygon = (Element) footprint.getElementsByTagName("ns2:Polygon").item(0);
									
									Element exterior = (Element) polygon.getElementsByTagName("ns2:exterior").item(0);
									
									Element abstractRing = (Element) exterior.getElementsByTagName("ns:AbstractRing").item(0);
									
									Element coordinates = (Element) abstractRing.getElementsByTagName("ns:coordinates").item(0);
									String coordinatestxt = coordinates.getChildNodes().item(0).getNodeValue();
									String[] coords = coordinatestxt.split(" ");
									
									String[] coordPointLL = coords[0].split(",");
									Point lowerLeft = new Point();
									lowerLeft.setLatitude(Double.parseDouble(coordPointLL[0]));
									lowerLeft.setLongitude(Double.parseDouble(coordPointLL[1]));
									entry.setLowerLeft(lowerLeft);
									
									String[] coordPointLR = coords[1].split(",");
									Point lowerRight = new Point();
									lowerRight.setLatitude(Double.parseDouble(coordPointLR[0]));
									lowerRight.setLongitude(Double.parseDouble(coordPointLR[1]));
									entry.setLowerRight(lowerRight);
									
									String[] coordPointUR = coords[2].split(",");
									Point upperRight = new Point();
									upperRight.setLatitude(Double.parseDouble(coordPointUR[0]));
									upperRight.setLongitude(Double.parseDouble(coordPointUR[1]));
									entry.setUpperRight(upperRight);
									
									String[] coordPointUL = coords[3].split(",");
									Point upperLeft = new Point();
									upperLeft.setLatitude(Double.parseDouble(coordPointUL[0]));
									upperLeft.setLongitude(Double.parseDouble(coordPointUL[1]));
									entry.setUpperLeft(upperLeft);
									
									
								}
								
								Element startTime = (Element) segmentElmt.getElementsByTagName("ns:acquisitionStartTime").item(0);
								if (startTime == null){
									System.out.println("no start time");
								}else {
									String startTimetxt = startTime.getChildNodes().item(0).getNodeValue();
									String acquisitionTime = startTimetxt;
									Element endTime = (Element) segmentElmt.getElementsByTagName("ns:acquisitionStopTime").item(0);
									if(endTime == null){
										System.out.println("no end time");
									}else{
										String endTimetxt = endTime.getChildNodes().item(0).getNodeValue();
										acquisitionTime += "-" + endTimetxt;
									}
									entry.setAcquisitionDate(acquisitionTime);
								}
								
								Element acquisitionMethod = (Element) segmentElmt.getElementsByTagName("ns:acquisitionMethod").item(0);
								if (acquisitionMethod == null){
									System.out.println("no acquisition method");
								}else{
									Element eoEquipment = (Element) acquisitionMethod.getElementsByTagName("eop:EarthObservationEquipment").item(0);

									if (eoEquipment == null) {
										System.out.println("no equipment");
									} else {
										Element platform = (Element) eoEquipment.getElementsByTagName("eop:platform").item(0);

										if (platform == null) {
											System.out.println("no platform");
										} else {
											Element platformElmt = (Element) platform.getElementsByTagName("eop:Platform").item(0);

											Element platformName = (Element) platformElmt.getElementsByTagName("eop:shortName").item(0);
											if (platformName == null) {
												System.out.println("no platform name");
											} else {
												String platformNametxt = platformName.getChildNodes().item(0).getNodeValue();
												entry.setPlatform(platformNametxt);
											}
											
											Element orbitType = (Element) platformElmt.getElementsByTagName("eop:orbitType").item(0);
											if (orbitType == null) {
												System.out.println("no orbit type");
											} else {
												String orbitTypeetxt = orbitType.getChildNodes().item(0).getNodeValue();
												entry.setOrbitType(orbitTypeetxt);
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
												if(sensorType.getChildNodes().item(0) != null){
													String sensorTypetxt = sensorType.getChildNodes().item(0).getNodeValue();
													entry.setSensor(sensorTypetxt);
												}else{
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
						}
						
						Element result = (Element) getResponse.getElementsByTagName("ns:result").item(0);
						if (result == null){
							System.out.println("no result");
						}else{
							Element statusReport = (Element) result.getElementsByTagName("ns:StatusReport").item(0);
							
							Element statusMsg = (Element) statusReport.getElementsByTagName("ns1:statusMessage").item(0);
							if(statusMsg == null){
								System.out.println("no status msg");
							}else{
								String statusMsgtxt = statusMsg.getChildNodes().item(0).getNodeValue();
								entry.setStatusMsg(statusMsgtxt);
							}
							
							Element eoTaskingParam = (Element) statusReport.getElementsByTagName("ns:eoTaskingParameters").item(0);
							if(eoTaskingParam == null){
								System.out.println("no eo tasking parameters");
							}else{
								Element taskingParam = (Element) eoTaskingParam.getElementsByTagName("ns:TaskingParameters").item(0);
								
								Element coverageRequest = (Element) taskingParam.getElementsByTagName("ns:CoverageProgrammingRequest").item(0);
								if(coverageRequest == null) {
									System.out.println("no coverage programming request");
								}else{
									//TODO time of interest, region of interest --> Query??
									Element timeofInterest = (Element) coverageRequest.getElementsByTagName("ns:CoverageProgrammingRequest").item(0);
									if (timeofInterest == null){
										System.out.println("no time of interest");
									}else{}

									Element acquisitionType = (Element) coverageRequest.getElementsByTagName("ns:AcquisitionType").item(0);
									if(acquisitionType == null){
										System.out.println("no acquisition type");
									}else{
										
										Element monoAcquisition = (Element) acquisitionType.getElementsByTagName("ns:MonoscopicAcquisition").item(0);
										if(monoAcquisition == null){
											System.out.println("no monoscopic acquisition");
										}else{
											Element acqAngle = (Element) coverageRequest.getElementsByTagName("ns:AcquisitionAngle").item(0);
											
											if(acqAngle == null){
												System.out.println("no acquisition angle");
											} else {
												Element incidenceRange = (Element) acqAngle.getElementsByTagName("ns:IncidenceRange").item(0);
												if (incidenceRange == null){
													System.out.println("no incidence angle");
												}else{
													Element azimuth = (Element) incidenceRange.getElementsByTagName("ns:AzimuthAngle").item(0);
													if (azimuth == null) {
														System.out.println("no azimuth");
													} else {
														String azimuthtxt = azimuth.getChildNodes().item(0).getNodeValue();
														entry.setAzimuth(azimuthtxt);
													}
													
													Element elevation = (Element) incidenceRange.getElementsByTagName("ns:ElevationAngle").item(0);
													if (elevation == null) {
														System.out.println("no elevation");
													} else {
														String elevationtxt = elevation.getChildNodes().item(0).getNodeValue();
														entry.setElevation(elevationtxt);
													}
												}
												
												Element pointingRange = (Element) acqAngle.getElementsByTagName("ns:PointingRange").item(0);
												if(pointingRange == null){
													System.out.println("no pointing range");
												}else{
													Element alongTrack = (Element) pointingRange.getElementsByTagName("ns:AlongTrackAngle").item(0);
													if(alongTrack == null){
														System.out.println("no along track");
													}else{
														String alongTracktxt = alongTrack.getChildNodes().item(0).getNodeValue();
														entry.setAlongTrack(alongTracktxt);
													}
													
													Element acrossTrack = (Element) pointingRange.getElementsByTagName("ns:AcrossTrackAngle").item(0);
													if(acrossTrack == null){
														System.out.println("no across track");
													}else{
														String acrossTracktxt = acrossTrack.getChildNodes().item(0).getNodeValue();
														entry.setAcrossTrack(acrossTracktxt);
													}
												}
											}
											
											Element acquisitionParam = (Element) monoAcquisition.getElementsByTagName("ns:AcquisitionParameters").item(0);
											if(acquisitionParam == null){
												System.out.println("no acquisition parameters");
											}else{
												Element acquisitionParamSAR = (Element) acquisitionParam.getElementsByTagName("ns:AcquisitionParametersSAR").item(0);
												if(acquisitionParamSAR == null){
													System.out.println("no SAR acquisition parameters");
												}else{
													Element instrumentMode = (Element) acquisitionParamSAR.getElementsByTagName("ns:InstrumentMode").item(0);
													if(instrumentMode == null){
														System.out.println("no instrument mode");
													}else{
														Element valueInstrumentMode = (Element) instrumentMode.getElementsByTagName("ns1:value").item(0);
														if(valueInstrumentMode == null){
															System.out.println("no value for instrument node");
														}else{
															String valueInstrumentModetxt = valueInstrumentMode.getChildNodes().item(0).getNodeValue();
															entry.setInstrumentMode(valueInstrumentModetxt);
														}
													}
													Element polarizationMode = (Element) acquisitionParamSAR.getElementsByTagName("ns:PolarizationMode").item(0);
													if(polarizationMode == null){
														System.out.println("no polarization mode");
													}else{
														Element valuepolarizationMode = (Element) polarizationMode.getElementsByTagName("ns1:value").item(0);
														if(valuepolarizationMode == null){
															System.out.println("no value for polarization node");
														}else{
															String valuepolarizationModetxt = valuepolarizationMode.getChildNodes().item(0).getNodeValue();
															entry.setPolarizationMode(valuepolarizationModetxt);
														}
													}
													Element compositeType = (Element) acquisitionParamSAR.getElementsByTagName("ns:FusionAccepted").item(0);
													if(compositeType == null){
														System.out.println("no composite type");
													}else{
														Element compositeTypeMode = (Element) compositeType.getElementsByTagName("ns1:value").item(0);
														if(compositeTypeMode == null){
															System.out.println("no value for composite type");
														}else{
															String compositeTypeModetxt = compositeTypeMode.getChildNodes().item(0).getNodeValue();
															entry.setCompositeType(compositeTypeModetxt);
														}
													}
												
												}
												
												Element acquisitionParamOPT = (Element) acquisitionParam.getElementsByTagName("ns:AcquisitionParametersOPT").item(0);
												if (acquisitionParamOPT == null){
													System.out.println("no OPT acquisition parameters");
												}else{
													Element resolution = (Element) acquisitionParamOPT.getElementsByTagName("ns:GroundResolution").item(0);
													if(resolution == null){
														System.out.println("no resolution");
													}else{
														Element valueResolution = (Element) resolution.getElementsByTagName("ns1:value").item(0);
														if(valueResolution == null){
															System.out.println("no value for resolution");
														}else{
															String valueResolutiontxt = valueResolution.getChildNodes().item(0).getNodeValue();
															entry.setResolution(valueResolutiontxt);													}
													}
													
													Element minLuminosity = (Element) acquisitionParamOPT.getElementsByTagName("ns:MinLuminosity").item(0);
													if(minLuminosity == null){
														System.out.println("no min luminosity");
													}else{
														Element valueMinLuminosity = (Element) minLuminosity.getElementsByTagName("ns1:value").item(0);
														if(valueMinLuminosity == null){
															System.out.println("no value for min luminosity");
														}else{
															String valueMinLuminositytxt = valueMinLuminosity.getChildNodes().item(0).getNodeValue();
															entry.setMinLuminosity(valueMinLuminositytxt);													}
													}
													
													Element instrumentMode = (Element) acquisitionParamOPT.getElementsByTagName("ns:InstrumentMode").item(0);
													if(instrumentMode == null){
														System.out.println("no instrument mode");
													}else{
														Element valueInstrumentMode = (Element) instrumentMode.getElementsByTagName("ns1:value").item(0);
														if(valueInstrumentMode == null){
															System.out.println("no value for instrument node");
														}else{
															String valueInstrumentModetxt = valueInstrumentMode.getChildNodes().item(0).getNodeValue();
															entry.setInstrumentMode(valueInstrumentModetxt);
														}
													}
													
													Element compositeType = (Element) acquisitionParamOPT.getElementsByTagName("ns:FusionAccepted").item(0);
													if(compositeType == null){
														System.out.println("no composite type");
													}else{
														Element compositeTypeMode = (Element) compositeType.getElementsByTagName("ns1:value").item(0);
														if(compositeTypeMode == null){
															System.out.println("no value for composite type");
														}else{
															String compositeTypeModetxt = compositeTypeMode.getChildNodes().item(0).getNodeValue();
															entry.setCompositeType(compositeTypeModetxt);
														}
													}
												}
											}
											
										}
										
									}
									
									Element validationParam = (Element) coverageRequest.getElementsByTagName("ns:ValidationParameters").item(0);
									if(validationParam == null){
										System.out.println("no validation param");
									}else{
										Element validationParamOPT = (Element) validationParam.getElementsByTagName("ns:ValidationParametersOPT").item(0);
										if(validationParamOPT == null){
											System.out.println("no OPT validation param");
										}else{
											Element cloud = (Element) validationParamOPT.getElementsByTagName("ns:MaxCloudCover").item(0);
											if(cloud == null){
												System.out.println("no max cloud cover");
											}else{
												Element valueCloud = (Element) cloud.getElementsByTagName("ns1:value").item(0);
												if(valueCloud == null){
													System.out.println("no value for max cloud cover");
												}else{
													String valueCloudtxt = valueCloud.getChildNodes().item(0).getNodeValue();
													entry.setCloudCover(valueCloudtxt);
												}
											}
											
											Element snow = (Element) validationParamOPT.getElementsByTagName("ns:MaxSnowCover").item(0);
											if(snow == null){
												System.out.println("no max snow cover");
											}else{
												Element valueSnow = (Element) snow.getElementsByTagName("ns1:value").item(0);
												if(valueSnow == null){
													System.out.println("no value for max snow cover");
												}else{
													String valueSnowtxt = valueSnow.getChildNodes().item(0).getNodeValue();
													entry.setSnowCover(valueSnowtxt);
												}
											}
											
											Element sunGlint = (Element) validationParamOPT.getElementsByTagName("ns:MaxSunGlint").item(0);
											if(sunGlint == null){
												System.out.println("no max sun glint");
											}else{
												Element valueSunGlint = (Element) sunGlint.getElementsByTagName("ns1:value").item(0);
												if(valueSunGlint == null){
													System.out.println("no value for max sun glint");
												}else{
													String valueSunGlinttxt = valueSunGlint.getChildNodes().item(0).getNodeValue();
													entry.setMaxSunGlint(valueSunGlinttxt);
												}
											}
											
											Element haze = (Element) validationParamOPT.getElementsByTagName("ns:HazeAccepted").item(0);
											if(haze == null){
												System.out.println("no haze");
											}else{
												Element valueHaze = (Element) haze.getElementsByTagName("ns1:value").item(0);
												if(valueHaze == null){
													System.out.println("no value for haze");
												}else{
													String valueHazetxt = valueHaze.getChildNodes().item(0).getNodeValue();
													entry.setHaze(valueHazetxt);
												}
											}
											
											Element sandWind = (Element) validationParamOPT.getElementsByTagName("ns:SandWindAccepted").item(0);
											if(sandWind == null){
												System.out.println("no sand wind");
											}else{
												Element valueSandWind = (Element) sandWind.getElementsByTagName("ns1:value").item(0);
												if(valueSandWind == null){
													System.out.println("no value for sand wind");
												}else{
													String valueSandWindtxt = valueSandWind.getChildNodes().item(0).getNodeValue();
													entry.setSandWind(valueSandWindtxt);
												}
											}
										}
										
										Element validationParamSAR = (Element) validationParam.getElementsByTagName("ns:ValidationParametersSAR").item(0);
										if(validationParamSAR == null){
											System.out.println("no SAR validation param");
										}else{
											Element noiseLevel = (Element) validationParamSAR.getElementsByTagName("ns:MaxNoiseLevel").item(0);
											if(noiseLevel == null){
												System.out.println("no max noise level");
											}else{
												Element valueNoiseLevel = (Element) noiseLevel.getElementsByTagName("ns1:value").item(0);
												if(valueNoiseLevel == null){
													System.out.println("no value for max noise level");
												}else{
													String valueNoiseLeveltxt = valueNoiseLevel.getChildNodes().item(0).getNodeValue();
													entry.setMaxNoiseLevel(valueNoiseLeveltxt);
												}
											}
										}
									}
									
									
								}
							}
						
						}
					}
					
					results.put(String.valueOf(i), entry);
					
					
				}
			}else{
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
	
