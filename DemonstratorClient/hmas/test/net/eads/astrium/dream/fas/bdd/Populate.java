/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas.bdd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static net.eads.astrium.dream.fas.bdd.TestConnexion.insert;
import net.eads.astrium.dream.fas.bdd.objects.GroundStation;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author re-sulrich
 */
public class Populate {
    
    
    
    //The data about the Sentinel 1 mission has been collected on :
    // https://directory.eoportal.org/web/eoportal/satellite-missions/c-missions/copernicus-sentinel-1
    
    
    @Test
    public void populate() {
        try {
//            addDeployments();
//            addMMFAS();
//            
//            addSatellites();
//            addFAS();
//            addMMFASSatellites();
//            
//            addSentinel1Sensor();
//            addSentinel1SARSensorChar();
//            addSentinel1InstrumentModes();
//            addSentinel1InstrumentModesSarChar();
//            addSentinel1PolarizationModes();
//            addInstrumentModesPolarisationModesJointures();
//            
//            addSentinel2Sensor();
//            
//            addSentinel2OPTSensorChar();
//            addSentinel2InstrumentModes();
//            addSentinel2InstrumentModesOptChar();
            
            addStationsFromXMLFile();
            addFucinoUnavailibilities();
            addSentinel1Unavailibilities();
            
        } catch (SQLException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addDeployments() throws SQLException {
        
        String table = "ApplicationServer";
        
        List<String> fields = new ArrayList<String>();
        
        fields.add("name");
        fields.add("description");
        fields.add("serverBaseAddress");
        
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'Deployment1'");
        depl1.add("''");
        depl1.add("'127.0.0.1:8080'");
        
        List<String> depl2 = new ArrayList<String>();
        depl2.add("'Deployment1'");
        depl2.add("''");
        depl2.add("'192.168.0.20:8080'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
        values.add(depl2);
        
        insert(
                table, 
                fields, 
                values);
    }
    
    public void addMMFAS() throws SQLException {
        String table = "MMFAS";
        
        List<String> fields = new ArrayList<String>();
        fields.add("mmfasId");
        fields.add("name");
        fields.add("description");
        fields.add("href");
        
        fields.add("server");
        
        List<String> gmesmmfas = new ArrayList<String>();
        gmesmmfas.add("'gmes-mmfas'");
        gmesmmfas.add("'GMES MMFAS'");
        gmesmmfas.add("'This MMFAS permits to task the satellites of the GMES program.'");
        gmesmmfas.add("'http://www.esa.int/Our_Activities/Observing_the_Earth/GMES/'");
        
        gmesmmfas.add("'1'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(gmesmmfas);
        
        insert(
                table, 
                fields, 
                values);
        
    }
    
    public void addMMFASSatellites() throws SQLException {
        
        String table = "LNK_MMFAS_Satellite";
        
        List<String> fields = new ArrayList<String>();
      
        fields.add("mmfas");
        fields.add("satellite");
        
        List<String> gmess1 = new ArrayList<String>();
        gmess1.add("'gmes-mmfas'");
        gmess1.add("'Sentinel1'");
        
        List<String> gmess2 = new ArrayList<String>();
        gmess2.add("'gmes-mmfas'");
        gmess2.add("'Sentinel2'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(gmess1);
        values.add(gmess2);
        
        insert(
                table, 
                fields, 
                values);
        
    }
    
    public void addFAS() throws SQLException {
        
        String table = "FAS";
        
        List<String> fields = new ArrayList<String>();
      
        fields.add("fasId");
        fields.add("name");
        fields.add("description");
        
        fields.add("externalFAS");
        
        fields.add("platform");
        fields.add("server");
        
        List<String> gmess1 = new ArrayList<String>();
        gmess1.add("'s1-fas'");
        gmess1.add("'FAS Sentinel 1'");
        gmess1.add("'This server computes Feasibility Analysis for the sensors of the Sentinel 1 platform.'");
        
        //Internal FAS
        gmess1.add("FALSE");
        
        gmess1.add("'Sentinel1'");
        gmess1.add("1");
        
        List<String> gmess2 = new ArrayList<String>();
        gmess2.add("'s2-fas'");
        gmess2.add("'FAS Sentinel 2'");
        gmess2.add("'This server computes Feasibility Analysis for the sensors of the Sentinel 2 platform.'");
        //Internal FAS
        gmess2.add("FALSE");
        
        gmess2.add("'Sentinel2'");
        gmess2.add("2");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(gmess1);
        values.add(gmess2);
        
        insert(
                table, 
                fields, 
                values);
    }
//    @Test
    public void addSatellites() throws SQLException {
        String table = "SatellitePlatform";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("osffile");
//        fields.add("tlefile");
        fields.add("satelliteId");
        fields.add("name");
        fields.add("description");
        fields.add("orbittype");
        fields.add("href");


        List<String> sentinel1 = new ArrayList<String>();
        sentinel1.add("'Sentinel1'");
        sentinel1.add("'Sentinel1'");
        sentinel1.add("'Sentinel 1 is a SAR satellite part of the Sentinel project.'");
        sentinel1.add("'SSO'");
        sentinel1.add("'http://www.esa.int/Our_Activities/Observing_the_Earth/GMES/Sentinel-1'");
        
        List<String> sentinel2 = new ArrayList<String>();
        sentinel2.add("'Sentinel2'");
        sentinel2.add("'Sentinel2'");
        sentinel2.add("'Sentinel 2 is a optical satellite part of the Sentinel project.'");
        sentinel2.add("'SSO'");
        sentinel2.add("'http://www.esa.int/Our_Activities/Observing_the_Earth/GMES/Sentinel-2'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(sentinel1);
        values.add(sentinel2);
        
        insert(
                table, 
                fields, 
                values);
    }
    
//    @Test
    public void addSentinel1Sensor() throws SQLException {
        
        String table = "Sensor";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("sdffile");
        fields.add("sensorId");
        fields.add("sensorName");
        fields.add("sensorDescription");
        fields.add("type");
        fields.add("bandType");
        fields.add("minLatitude");
        fields.add("maxLatitude");
        fields.add("minLongitude");
        fields.add("maxLongitude");
        
        fields.add("mass");
        fields.add("maxPowerConsumption");
        fields.add("acqMethod");
        fields.add("applications");
        
        fields.add("platform");


        List<String> sentinel1 = new ArrayList<String>();
        sentinel1.add("'S1SAR'");
        sentinel1.add("'Sentinel 1 SAR'");
        sentinel1.add("'The Sentinel-1 SAR mission is part of the GMES system, which is designed to\n" +
                "provide an independent and operational information capacity to the European Union\n" +
                "to warrant environment and security policies and to support sustainable economic\n" +
                "growth. In particular, the mission will provide timely and high quality remote\n" +
                "sensing data to support monitoring the open ocean and the changes to marine and\n" +
                "coastal environmental conditions.'");
        sentinel1.add("'SAR'");
        sentinel1.add("'C-BAND'");
        sentinel1.add("-90");
        sentinel1.add("90");
        sentinel1.add("-180");
        sentinel1.add("180");
        
        sentinel1.add("880");
        sentinel1.add("4368");
        sentinel1.add("'C-band SAR'");
        sentinel1.add("'C-band SAR all-weather imaging capability'");
        
        sentinel1.add("'Sentinel1'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(sentinel1);
        
        insert(
                table, 
                fields, 
                values);
    }
    
//    @Test
    public void addSentinel1SARSensorChar() throws SQLException {
        
        
        String table = "SarSensorCharacteristics";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("sdffile");
        fields.add("antennaLength");
        fields.add("antennaWidth");
        fields.add("groundLocationAccuracy");
        fields.add("revisitTimeInDays");
        fields.add("transmitFrequencyBand");
        fields.add("transmitCenterFrequency");
        
        fields.add("sensor");
        
        List<String> s1Char = new ArrayList<String>();
        s1Char.add("12.3");
        s1Char.add("0.82");
        s1Char.add("0.0");
        s1Char.add("3");
        s1Char.add("'C'");
        s1Char.add("5.405");
        //Sensor 1 = sentinel1
        s1Char.add("'S1SAR'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(s1Char);
        
        insert(
                table, 
                fields, 
                values);
    }
    
//    @Test
    public void addSentinel2Sensor() throws SQLException {
        
        String table = "Sensor";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("sdffile");
        fields.add("sensorId");
        fields.add("sensorName");
        fields.add("sensorDescription");
        fields.add("type");
        fields.add("bandType");
        fields.add("minLatitude");
        fields.add("maxLatitude");
        fields.add("minLongitude");
        fields.add("maxLongitude");
        
        fields.add("mass");
        fields.add("maxPowerConsumption");
        fields.add("acqMethod");
        fields.add("applications");
        
        fields.add("platform");

        
        List<String> sentinel2 = new ArrayList<String>();
        sentinel2.add("'S2OPT'");
        sentinel2.add("'Sentinel 2 OPT'");
        sentinel2.add("'Sentinel-2 is a multispectral operational imaging mission within the GMES \n" +
                "(Global Monitoring for Environment and Security) program, jointly implemented by the EC \n" +
                "(European Commission) and ESA (European Space Agency) for global land observation \n" +
                "(data on vegetation, soil and water cover for land, inland waterways and coastal areas, \n" +
                "and also provide atmospheric absorption and distortion data corrections) at high resolution \n" +
                "with high revisit capability to provide enhanced continuity of data \n" +
                "so far provided by SPOT-5 and Landsat-7.'");
        sentinel2.add("'OPT'");
        sentinel2.add("NULL");
        sentinel2.add("-56");
        sentinel2.add("83");
        sentinel2.add("-180");
        sentinel2.add("180");
        
        sentinel2.add("290");
        sentinel2.add("266");
        sentinel2.add("'Pushbroom'");
        sentinel2.add("'Multispectral Imager'");
        
        
        sentinel2.add("'Sentinel2'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(sentinel2);
        
        
        insert(
                table, 
                fields, 
                values);
    }
    
    
    
    //    @Test
    public void addSentinel2OPTSensorChar() throws SQLException {
        
        
        String table = "OptSensorCharacteristics";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("sdffile");
        fields.add("acrossTrackFOV");
        fields.add("minAcrossTrackAngle");
        fields.add("maxAcrossTrackAngle");
        fields.add("minAlongTrackAngle");
        fields.add("maxAlongTrackAngle");
        fields.add("swathWidth");
        fields.add("groundLocationAccuracy");
        fields.add("revisitTimeInDays");
        fields.add("numberOfBands");
        
        
        fields.add("sensor");
        
        List<String> s2Char = new ArrayList<String>();
        s2Char.add("20.6");
        s2Char.add("-27.0");
        s2Char.add("27.0");
        s2Char.add("0.0");
        s2Char.add("0.0");
        
        s2Char.add("290000");
        
        s2Char.add("50.0");
        s2Char.add("5.0");
        
        s2Char.add("13");
        
        //Sensor 2 = sentinel2
        s2Char.add("'S2OPT'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(s2Char);
        
        insert(
                table, 
                fields, 
                values);
    }
    
//    @Test
    public void addSentinel2InstrumentModes() throws SQLException {
        
        String table = "InstrumentMode";
        
        List<String> fields = new ArrayList<String>();
        fields.add("imId");
        fields.add("name");
        fields.add("description");

        List<String> sm = new ArrayList<String>();
        sm.add("'MSI'");
        sm.add("'MSI'");
        sm.add("'The instrument is based on the pushbroom observation concept. The telescope features a TMA (Three Mirror Anastigmat) design with a pupil diameter of 150 mm, providing a very good imaging quality all across its wide FOV (Field of View). The equivalent swath width is 290 km. The telescope structure and the mirrors are made of silicon carbide (SiC) which allows to minimize thermoelastic deformations.'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(sm);
        
        insert(
                table, 
                fields, 
                values);
    }
    
    
//    @Test
    public void addSentinel2InstrumentModesOptChar () throws SQLException {
        
        
        String table = "OptImCharacteristics";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("sdffile");
        
        fields.add("acrossTrackResolution");
        fields.add("alongTrackResolution");
        
        fields.add("maxPowerConsumption");
        fields.add("numberOfSamples");
        fields.add("bandType");
        fields.add("minSpectralRange");
        fields.add("maxSpectralRange");
        fields.add("snrRatio");
        fields.add("noiseEquivalentRadiance");
        
        //Foreign key
        fields.add("instMode");
        fields.add("sensor");
        
        // MSI (Multi spectral instrument)
        List<String> s1Char = new ArrayList<String>();
        s1Char.add("5");
        s1Char.add("5");
        
        s1Char.add("266.0");
        s1Char.add("12000");
        s1Char.add("'VIS'");
        s1Char.add("490.0");
        s1Char.add("690.0");
        s1Char.add("170.0");
        s1Char.add("0.1");
        
        s1Char.add("'MSI'");
        s1Char.add("'S2OPT'");
        
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(s1Char);
        
        insert(
                table, 
                fields, 
                values);
    }
    
    
    
    
    
//    @Test
    public void addSentinel1InstrumentModes() throws SQLException {
        
        String table = "InstrumentMode";
        
        List<String> fields = new ArrayList<String>();
        fields.add("imId");
        fields.add("name");
        fields.add("description");

        List<String> sm = new ArrayList<String>();
        sm.add("'SM'");
        sm.add("'SM'");
        sm.add("'The conventional SAR strip mapping mode assumes a fixed pointing direction \n" +
                "of the radar antenna broadside to the platform track. \n" +
                "A strip map is an image formed in width by the swath of the SAR and \n" +
                "follows the length contour of the flight line of the platform itself. \n" +
                "A detailed description of this mode you will find on the topic SLAR.'");
        
        List<String> iw = new ArrayList<String>();
        iw.add("'IW'");
        iw.add("'IW'");
        iw.add("'The IW mode acquires data of wide swaths (composed of 3 sub-swaths),\n" +
                "at the expense of resolution, using the TOPSAR imaging technique. The TOPSAR\n" +
                "imaging is a form of ScanSAR imaging (the antenna beam is switched cyclically\n" +
                "among the three sub-swaths) where, for each burst, the beam\n" +
                "is electronically steered from backward to forward in the azimuth direction.\n" +
                "This leads to uniform NESZ and ambiguity levels within the\n" +
                "scan bursts, resulting in a higher quality image.'");
        
        List<String> ew = new ArrayList<String>();
        ew.add("'EW'");
        ew.add("'EW'");
        ew.add("'The EW mode uses the TOPSAR imaging technique.\n" +
                "The EW mode provides a very large swath coverage (obtained\n" +
                "from imaging 5 sub-swaths) at the expense of a further reduction in resolution.\n" +
                "The EW mode is a TOPSAR single sweep mode. The TOPSAR\n" +
                "imaging is a form of ScanSAR imaging (the antenna beam is switched cyclically\n" +
                "among the three sub-swaths) where, for each burst, the beam\n" +
                "is electronically steered from backward to forward in the azimuth direction.\n" +
                "This leads to uniform NESZ and ambiguity levels within the\n" +
                "scan bursts, resulting in a higher quality image.'");
        
        List<String> wv = new ArrayList<String>();
        wv.add("'WV'");
        wv.add("'WV'");
        wv.add("'The Synthetic Aperture Radar can be operated in wave mode. \n"
                + "The primary purpose is to measure directional wave spectra - wave energy \n"
                + "as a function of the directions and lengths of waves at the ocean surface - \n"
                + "from the backscattered radiation from sample areas. \n"
                + "For this function the SAR collects data at spatial intervals of either 200 km \n"
                + "(nominally) or 300 km anywhere within the swath available to the SAR mode \n"
                + "(100 km wide) in steps of approximately 2 km.'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(sm);
        values.add(iw);
        values.add(ew);
        values.add(wv);
        insert(
                table, 
                fields, 
                values);
    }
    
//    @Test
    public void addSentinel1InstrumentModesSarChar () throws SQLException {
        
        
        String table = "SarImCharacteristics";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("sdffile");
        
        fields.add("acrossTrackResolution");
        fields.add("alongTrackResolution");
        
        fields.add("maxPowerConsumption");
        fields.add("minAcrossTrackAngle");
        fields.add("maxAcrossTrackAngle");
        fields.add("minAlongTrackAngle");
        fields.add("maxAlongTrackAngle");
        fields.add("swathWidth");
        fields.add("radiometricResolution");
        fields.add("minRadioStab");
        fields.add("maxRadioStab");
        fields.add("minAlongTrackAmbRatio");
        fields.add("maxAlongTrackAmbRatio");
        fields.add("minAcrossTrackAmbRatio");
        fields.add("maxAcrossTrackAmbRatio");
        fields.add("minNoiseEquivalentSO");
        fields.add("maxNoiseEquivalentSO");
        
        //Foreign key
        fields.add("instMode");
        fields.add("sensor");
        
        // Stripmap (SM)
        List<String> s1Char = new ArrayList<String>();
        
        s1Char.add("5");
        s1Char.add("5");
        
        s1Char.add("4368");
        s1Char.add("20");
        s1Char.add("45");
        s1Char.add("20");
        s1Char.add("45");
        s1Char.add("80");
        s1Char.add("1");
        s1Char.add("1");
        s1Char.add("1");
        s1Char.add("-22");
        s1Char.add("-25");
        s1Char.add("-22");
        s1Char.add("-25");
        s1Char.add("-22");
        s1Char.add("-22");
        
        s1Char.add("'SM'");
        s1Char.add("'S1SAR'");
        
        
        
        // Interferometric Wide Swath (IWS)
        List<String> s2Char = new ArrayList<String>();
        
        s2Char.add("5");
        s2Char.add("20");
        
        s2Char.add("4075");
        s2Char.add("25");
        s2Char.add("90");
        s2Char.add("25");
        s2Char.add("90");
        s2Char.add("250");
        s2Char.add("1");
        s2Char.add("1");
        s2Char.add("1");
        s2Char.add("-22");
        s2Char.add("-25");
        s2Char.add("-22");
        s2Char.add("-25");
        s2Char.add("-22");
        s2Char.add("-22");
        
        s2Char.add("'IW'");
        s2Char.add("'S1SAR'");
        
        
        
        
        // Extra Wide Swath (EWS)
        List<String> s3Char = new ArrayList<String>();
        
        s3Char.add("25");
        s3Char.add("40");
        
        s3Char.add("4368");
        s3Char.add("20");
        s3Char.add("90");
        s3Char.add("20");
        s3Char.add("90");
        s3Char.add("400");
        s3Char.add("1");
        s3Char.add("1");
        s3Char.add("1");
        s3Char.add("-22");
        s3Char.add("-25");
        s3Char.add("-22");
        s3Char.add("-25");
        s3Char.add("-22");
        s3Char.add("-22");
        
        s3Char.add("'EW'");
        s3Char.add("'S1SAR'");
        
        
        
        
        // Wave mode (WM)
        List<String> s4Char = new ArrayList<String>();
        
        s4Char.add("5");
        s4Char.add("5");
        
        s4Char.add("4368");
        s4Char.add("23");
        s4Char.add("36.5");
        s4Char.add("20");
        s4Char.add("45");
        s4Char.add("20");
        s4Char.add("1");
        s4Char.add("1");
        s4Char.add("1");
        s4Char.add("-22");
        s4Char.add("-25");
        s4Char.add("-22");
        s4Char.add("-25");
        s4Char.add("-22");
        s4Char.add("-22");
        
        s4Char.add("'WV'");
        s4Char.add("'S1SAR'");
        
        
        
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(s1Char);
        values.add(s2Char);
        values.add(s3Char);
        values.add(s4Char);
        
        insert(
                table, 
                fields, 
                values);
    }
    
    
    
    
//    @Test
    public void addSentinel1PolarizationModes() throws SQLException {
        
        String table = "PolarisationMode";
        
        List<String> fields = new ArrayList<String>();
        fields.add("pmId");
        fields.add("name");
        fields.add("description");

        List<String> hh = new ArrayList<String>();
        hh.add("'HH'");
        hh.add("'HH'");
        hh.add("'Single horizontal mode'");
        List<String> vv = new ArrayList<String>();
        vv.add("'VV'");
        vv.add("'VV'");
        vv.add("'Single vertical mode'");
        List<String> hv = new ArrayList<String>();
        hv.add("'HV'");
        hv.add("'HV'");
        hv.add("'Dual horizontal vertical mode'");
        List<String> vh = new ArrayList<String>();
        vh.add("'VH'");
        vh.add("'VH'");
        vh.add("'Dual vertical horizontal mode'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(hh);
        values.add(vv);
        values.add(hv);
        values.add(vh);
        
        insert(
                table, 
                fields, 
                values);
    }
    
//    @Test
    public void addInstrumentModesPolarisationModesJointures() throws SQLException {

        
        String table = "lnk_im_pm";
        
//        String[] ims = {"'SM'","'IW'","'EW'","'WV'"};
        String[] ims = {"1","2","3","4"};
        String[] pms = {"'HH'","'VV'","'HV'","'VH'"};
        
        List<String> fields = new ArrayList<String>();
        fields.add("instMode");
        fields.add("polMode");

        List<List<String>> values = new ArrayList<List<String>>();
        
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 4; j++) {
                
                List<String> hh = new ArrayList<String>();
                hh.add("" + ims[i]);
                hh.add("" + pms[j]);
                values.add(hh);
            }
        }
        
        List<String> hh = new ArrayList<String>();
        hh.add("" + ims[3]);
        hh.add("" + pms[0]);
        values.add(hh);
        List<String> vv = new ArrayList<String>();
        vv.add("" + ims[3]);
        vv.add("" + pms[1]);
        values.add(vv);
        
        insert(
                table, 
                fields, 
                values);
    }
    
//    @Test
    public void addStationsFromXMLFile() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
        
        List<GroundStation> stations = new ArrayList<GroundStation>();
        
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        
        String filePath = "C:\\Users\\re-cetienne\\.dream\\fas\\stations.xml";
        
        File inputDataFile = new File(filePath);
        
        if (!inputDataFile.exists())
        {
        	throw new FileNotFoundException("File " + filePath + " could not be found.");
        }
        
        Document doc = documentBuilder.parse(inputDataFile);
        
        System.out.println( "" + doc.getFirstChild().getNodeName() );
        
        System.out.println("");
        
        for (int i = 0; i < doc.getFirstChild().getChildNodes().getLength(); i++) {
            Node dataBlock = doc.getFirstChild().getChildNodes().item(i);
            String name = dataBlock.getNodeName();
            System.out.println(""+ name);
            
            if (name.contains("Data_Block"))
            {
                for (int j = 0; j < dataBlock.getChildNodes().getLength(); j++) {
                    Node stationsList = dataBlock.getChildNodes().item(j);
                    
                    System.out.println(" - " + stationsList.getNodeName());
                    
                    if (stationsList.getNodeName().contains("List_of_Ground_Stations"))
                    {
                        for (int k = 0; k < stationsList.getChildNodes().getLength(); k++) {
                            Node stat = stationsList.getChildNodes().item(k);
                            
                            if (stat.getNodeName().contains("Ground_Station"))
                            {
                                GroundStation station = new GroundStation();
                                
                                
                                for (int l = 0; l < stat.getChildNodes().getLength(); l++) {
                                    Node property = stat.getChildNodes().item(l);
                                    
                                    String propName = property.getNodeName();
                                    
                                    if (propName.contains("Station_id"))
                                    {
                                        String value = property.getTextContent();
                                        System.out.println("" + value);
                                        station.setIntId(value);
                                    }
                                    if (propName.contains("Descriptor"))
                                    {
                                        String value = property.getTextContent();
                                        System.out.println("" + value);
                                        station.setName(value);
                                    }
                                    if (propName.contains("Antenna"))
                                    {
                                        String value = property.getTextContent();
                                        System.out.println("" + value);
                                        station.setAntennaType(value);
                                    }
                                    if (propName.contains("Location"))
                                    {
                                        System.out.println("" +  property.getChildNodes().getLength());
                                        for (int m = 0; m < property.getChildNodes().getLength(); m++) {
                                            Node coord = property.getChildNodes().item(m);
                                            String coordName = coord.getNodeName();
                                            
                                            if (coordName.contains("Long"))
                                            {
                                                String value = coord.getTextContent();
                                                
                                                System.out.println("" + Double.valueOf(value));
                                                station.setLon(Double.valueOf(value));
                                            }
                                            if (coordName.contains("Lat"))
                                            {
                                                String value = coord.getTextContent();
                                                
                                                System.out.println("" + Double.valueOf(value));
                                                station.setLat(Double.valueOf(value));
                                            }
                                            if (coordName.contains("Alt"))
                                            {
                                                String value = coord.getTextContent();
                                                
                                                System.out.println("" + Double.valueOf(value));
                                                station.setAlt(Double.valueOf(value));
                                            }
                                        }
                                    } // End if (propName.contains("Location"))
                                } // End for (int l = 0; l < stat.getChildNodes().getLength(); l++)
                                
                                stations.add(station);
                            }
                        } // End for(int k = 0; k < stationsList.getChildNodes().getLength(); k++)
                    }
                }
            }
        } // End for (int i = 0; i < doc.getFirstChild().getChildNodes().getLength(); i++)
        
        List<String> fields = new ArrayList<String>();
//        fields.add("groundStationId");
        fields.add("name");
        fields.add("longitude");
        fields.add("latitude");
        fields.add("altitude");
        fields.add("antennaType");
        fields.add("internationalIdentifier");
        for (GroundStation groundStation : stations) {
            
            List<String> value = new ArrayList<String>();
            value.add("'" + groundStation.getName() + "'");
            value.add("" + groundStation.getLon());
            value.add("" + groundStation.getLat());
            value.add("" + groundStation.getAlt());
            value.add("'" + groundStation.getAntennaType() + "'");
            value.add("'" + groundStation.getIntId() + "'");
            
            List<List<String>> values = new ArrayList<List<String>>();
            values.add(value);
            try {
                TestConnexion.insert("GroundStation", fields, values);
            } catch (SQLException ex) {
//                Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
//    @Test
    public void addFucinoUnavailibilities() throws SQLException {
        
        String gsId = "1";
        
        List<String> fields = new ArrayList<String>();
        fields.add("beginU");
        fields.add("endU");
        fields.add("cause");
        fields.add("station");
        
        List<String> uStat1 = new ArrayList<String>();
        uStat1.add("'2013-08-12T00:00:00Z'");
        uStat1.add("'2013-08-13T00:00:00Z'");
        uStat1.add("'Maintenance'");
        uStat1.add(gsId);
        
        List<String> uStat2 = new ArrayList<String>();
        uStat2.add("'2013-08-01T06:00:00Z'");
        uStat2.add("'2013-08-01T18:00:00Z'");
        uStat2.add("'Occupied'");
        uStat2.add(gsId);
        
        List<String> uStat3 = new ArrayList<String>();
        uStat3.add("'2013-08-23T00:00:00Z'");
        uStat3.add("'2013-08-24T12:00:00Z'");
        uStat3.add("'Maintenance'");
        uStat3.add(gsId);
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(uStat1);
        values.add(uStat2);
        values.add(uStat3);
        
        insert(
                "Unavailibility", 
                fields, 
                values);
    }
    
//    @Test
    public void addSentinel1Unavailibilities() throws SQLException {
        
        String sensorId = "'S1SAR'";
        
        List<String> fields = new ArrayList<String>();
        fields.add("beginU");
        fields.add("endU");
        fields.add("cause");
        fields.add("sensor");
        
        List<String> uStat1 = new ArrayList<String>();
        uStat1.add("'2013-08-15T00:00:00Z'");
        uStat1.add("'2013-08-16T00:00:00Z'");
        uStat1.add("'Maintenance'");
        uStat1.add(sensorId);
        
        List<String> uStat2 = new ArrayList<String>();
        uStat2.add("'2013-08-19T06:00:00Z'");
        uStat2.add("'2013-08-19T18:00:00Z'");
        uStat2.add("'Occupied'");
        uStat2.add(sensorId);
        
        List<String> uStat3 = new ArrayList<String>();
        uStat3.add("'2013-08-21T00:00:00Z'");
        uStat3.add("'2013-08-21T12:00:00Z'");
        uStat3.add("'Occupied'");
        uStat3.add(sensorId);
        
        
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(uStat1);
        values.add(uStat2);
        values.add(uStat3);
        
        insert(
                "Unavailibility", 
                fields, 
                values);
    }
    
}
