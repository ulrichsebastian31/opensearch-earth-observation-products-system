/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas.bdd;

import java.sql.SQLException;
import java.util.Arrays;

import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class CreateStruct {
    
    @Test
    public void main() throws SQLException {
        
//    	TestConnexion.createEnum("BandTypes", Arrays.asList(new String[]{"'L-BAND'", "'C-BAND'", "'S-BAND'", "'X-BAND'"}));
//        
//        TestConnexion.createEnum("SensorTypes", Arrays.asList(new String[]{"'SAR'", "'OPT'"}));
//        
//        
//        System.out.println("Begin");
//        CreateSensorStruct.testCreatePolarisationModeTable();
//        CreateSensorStruct.testCreateInstrumentModeTable();
//        CreateTaskMMFASDeploymentStruct.testCreateApplicationServerTable();
//        CreateTaskMMFASDeploymentStruct.testCreateMMFASTable();
//        
//        
//        CreateSensorStruct.testCreateSatelliteTable();
//        //Depends on MMFAS and SatellitePlatform
//        CreateTaskMMFASDeploymentStruct.testCreateLinkMMFASSatellitesTable();
//        
//        //Depends on Satellite
//        CreateSensorStruct.testCreateSensorTable();
//        //Depends on sensor
//        CreateSensorStruct.testCreateSARSensorChar();
//        CreateSensorStruct.testCreateOPTSensorChar();
//        //Depends on sensor and InstrumentMode
//        CreateSensorStruct.testCreateOPTIMChar();
//        CreateSensorStruct.testCreateSARIMChar();
        
        //Depends on SARIM and Pol Mode
        CreateSensorStruct.testCreateIMPMTable();
        
        
        
        //Depends on Satellite and ApplicationServer
        CreateTaskMMFASDeploymentStruct.testCreateFASTable();
        CreateTaskMMFASDeploymentStruct.testCreateMissionPlannerTable();
        
        CreateTaskMMFASDeploymentStruct.testCreateGroundStationTable();
        
        CreateTaskMMFASDeploymentStruct.testCreateUnavailibilityTable();
        
        
        CreateReqTaskStruct.testCreateStatusTable();
        //Depends on Status
        CreateReqTaskStruct.testCreateMMFASTaskTable();
        
        //Depends on Status and MMFASTask
        CreateReqTaskStruct.testCreateRequestTable();
        
        CreateReqTaskStruct.testCreateTaskingParametersTable();
        
        CreateReqTaskStruct.testCreateOPTParamsTable();
        CreateReqTaskStruct.testCreateSARParamsTable();
        
        CreateReqTaskStruct.testCreateLink_TP_InstModes();
        CreateReqTaskStruct.testCreateLink_TP_PolModes();
        
        
        CreateReqTaskStruct.testCreateSensorTaskTable();
        //Depends on SensorTask and Request
        CreateReqTaskStruct.testCreateLinkSensorTaskRequestTable();
        //Depends on Status and SensorTask and GroundStation
        CreateReqTaskStruct.testCreateSegmentTable();
        //Depends on Status and SensorTask
        CreateReqTaskStruct.testCreateResultTable();
        
    }
}
