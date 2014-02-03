/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas.bdd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author re-sulrich
 */
public class CreateSensorStruct {
    
    public static void testCreateOPTIMChar() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"instrumentModeId", "serial primary key"});
        
        fields.add(new String[]{"acrossTrackResolution", "double precision"});
        fields.add(new String[]{"alongTrackResolution", "double precision"});
        
        fields.add(new String[]{"maxPowerConsumption", "double precision"});
        fields.add(new String[]{"numberOfSamples", "double precision"});
        fields.add(new String[]{"bandType", "varchar(20)"});
        fields.add(new String[]{"minSpectralRange", "double precision"});
        fields.add(new String[]{"maxSpectralRange", "double precision"});
        fields.add(new String[]{"snrRatio", "double precision"});
        fields.add(new String[]{"noiseEquivalentRadiance", "double precision"});
        
        //Foreign key
        fields.add(new String[]{"instMode", "varchar(5) references InstrumentMode(imId)"});
        fields.add(new String[]{"sensor", "varchar(10) references Sensor(sensorId)"});
        
        TestConnexion.create("OptIMCharacteristics", fields);
    }
    
    public static void testCreateSARIMChar() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"instrumentModeId", "serial primary key"});
        
        fields.add(new String[]{"acrossTrackResolution", "double precision"});
        fields.add(new String[]{"alongTrackResolution", "double precision"});
        
        fields.add(new String[]{"maxPowerConsumption", "double precision"});
        fields.add(new String[]{"minAcrossTrackAngle", "double precision"});
        fields.add(new String[]{"maxAcrossTrackAngle", "double precision"});
        fields.add(new String[]{"minAlongTrackAngle", "double precision"});
        fields.add(new String[]{"maxAlongTrackAngle", "double precision"});
        fields.add(new String[]{"swathWidth", "double precision"});
        fields.add(new String[]{"radiometricResolution", "double precision"});
        fields.add(new String[]{"minRadioStab", "double precision"});
        fields.add(new String[]{"maxRadioStab", "double precision"});
        fields.add(new String[]{"minAlongTrackAmbRatio", "double precision"});
        fields.add(new String[]{"maxAlongTrackAmbRatio", "double precision"});
        fields.add(new String[]{"minAcrossTrackAmbRatio", "double precision"});
        fields.add(new String[]{"maxAcrossTrackAmbRatio", "double precision"});
        fields.add(new String[]{"minNoiseEquivalentSO", "double precision"});
        fields.add(new String[]{"maxNoiseEquivalentSO", "double precision"});
        
        //Foreign key
        fields.add(new String[]{"instMode", "varchar(5) references InstrumentMode(imId)"});
        fields.add(new String[]{"sensor", "varchar(10) references Sensor(sensorId)"});
        
        TestConnexion.create("SarIMCharacteristics", fields);
    }
    
    public static void testCreatePolarisationModeTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"pmId", "varchar(5) primary key"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"description", "varchar(1024)"});
        
        TestConnexion.create("PolarisationMode", fields);
    }
    
    public static void testCreateInstrumentModeTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"imId", "varchar(5) primary key"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"description", "varchar(1024)"});
        
        TestConnexion.create("InstrumentMode", fields);
    }
    
    public static void testCreateIMPMTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        //Foreign keys
        fields.add(new String[]{"instMode", "integer references SarIMCharacteristics(instrumentModeId)"});
        fields.add(new String[]{"polMode", "varchar(5) references PolarisationMode(pmId)"});
        
        TestConnexion.create("LNK_IM_PM", fields);
    }
    
    
    public static void testCreateSatelliteTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"satelliteId", "varchar(10) primary key"});
        fields.add(new String[]{"osfFile", "bytea"});
        fields.add(new String[]{"tleFile", "bytea"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"description", "varchar(1024)"});
        fields.add(new String[]{"orbitType", "varchar(10)"});
        fields.add(new String[]{"href", "varchar(1024)"});
        
        TestConnexion.create("SatellitePlatform", fields);
    }
    
    public static void testCreateSensorTable() throws SQLException {
        
        
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"sensorId", "varchar(10) primary key"});
        fields.add(new String[]{"sensorName", "varchar(100)"});
        fields.add(new String[]{"sensorDescription", "varchar(1024)"});
        fields.add(new String[]{"type", "SensorTypes"});
        fields.add(new String[]{"bandType", "BandTypes"});
        fields.add(new String[]{"minLatitude", "double precision"});
        fields.add(new String[]{"maxLatitude", "double precision"});
        fields.add(new String[]{"minLongitude", "double precision"});
        fields.add(new String[]{"maxLongitude", "double precision"});
        fields.add(new String[]{"sdfFile", "bytea"});
        //Characteristics
        fields.add(new String[]{"acqMethod", "varchar(20)"});
        fields.add(new String[]{"applications", "varchar(100)"});
        fields.add(new String[]{"mass", "double precision"});
        fields.add(new String[]{"maxPowerConsumption", "double precision"});
        
        //Foreign keys
        //Satellite
        fields.add(new String[]{"platform", "varchar(10) references SatellitePlatform(satelliteId)"});
        
        TestConnexion.create("Sensor", fields);
    }
    
    /**
     * Creates the properties of the sensor
     */
    public static void testCreateOPTSensorChar() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"sensorCharId", "serial primary key"});
        
        fields.add(new String[]{"acrossTrackFOV", "double precision"});
        fields.add(new String[]{"minAcrossTrackAngle", "double precision"});
        fields.add(new String[]{"maxAcrossTrackAngle", "double precision"});
        fields.add(new String[]{"minAlongTrackAngle", "double precision"});
        fields.add(new String[]{"maxAlongTrackAngle", "double precision"});
        fields.add(new String[]{"swathWidth", "double precision"});
        fields.add(new String[]{"groundLocationAccuracy", "double precision"});
        fields.add(new String[]{"revisitTimeInDays", "double precision"});
        fields.add(new String[]{"numberOfBands", "integer"});
        
        //Foreign keys
        fields.add(new String[]{"sensor", "varchar(10) references Sensor(sensorId)"});
        
        TestConnexion.create("OptSensorCharacteristics", fields);
    }
    
    public static void testCreateSARSensorChar() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"sensorCharId", "serial primary key"});
        
        fields.add(new String[]{"antennaLength", "double precision"});
        fields.add(new String[]{"antennaWidth", "double precision"});
        fields.add(new String[]{"groundLocationAccuracy", "double precision"});
        fields.add(new String[]{"revisitTimeInDays", "double precision"});
        fields.add(new String[]{"transmitFrequencyBand", "varchar(20)"});
        fields.add(new String[]{"transmitCenterFrequency", "double precision"});
        
        //Foreign keys
        fields.add(new String[]{"sensor", "varchar(10) references Sensor(sensorId)"});
        
        TestConnexion.create("SarSensorCharacteristics", fields);
    }
    
}
