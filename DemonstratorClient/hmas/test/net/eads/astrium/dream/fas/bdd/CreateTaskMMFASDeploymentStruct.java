/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas.bdd;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static net.eads.astrium.dream.fas.bdd.TestConnexion.createEnum;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class CreateTaskMMFASDeploymentStruct {
    
    
    
    
    public static void testCreateGroundStationTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"groundStationId", "serial primary key"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"longitude", "double precision"});
        fields.add(new String[]{"latitude", "double precision"});
        fields.add(new String[]{"altitude", "double precision"});
        fields.add(new String[]{"antennaType", "varchar(10)"});
        fields.add(new String[]{"internationalIdentifier", "char(8)"});
        
        TestConnexion.create("GroundStation", fields);
        
    }
    
    public static void testCreateMMFASTable() throws SQLException {
        
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"mmfasId", "varchar(10) primary key"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"description", "varchar(1024)"});
        fields.add(new String[]{"href", "varchar(1024)"});
        
        fields.add(new String[]{"server", "integer references ApplicationServer(asId)"});
        
        TestConnexion.create("MMFAS", fields);
    }
    
    public static void testCreateFASTable() throws SQLException {
        
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"fasId", "varchar(10) primary key"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"description", "varchar(1024)"});
        fields.add(new String[]{"externalFAS", "boolean"});
        fields.add(new String[]{"externalHref", "varchar(1024)"});
        
        fields.add(new String[]{"platform", "varchar(10) references SatellitePlatform(satelliteId)"});
        fields.add(new String[]{"server", "integer references ApplicationServer(asId)"});
        
        TestConnexion.create("FAS", fields);
    }
    
    public static void testCreateMissionPlannerTable() throws SQLException {
        
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"mpId", "varchar(10) primary key"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"description", "varchar(1024)"});
        fields.add(new String[]{"externalFAS", "boolean"});
        fields.add(new String[]{"externalHref", "varchar(1024)"});
        
        fields.add(new String[]{"platform", "varchar(10) references SatellitePlatform(satelliteId)"});
        fields.add(new String[]{"server", "integer references ApplicationServer(asId)"});
        
        TestConnexion.create("MissionPlanner", fields);
    }
    
    public static void testCreateApplicationServerTable() throws SQLException {
        
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"asId", "serial primary key"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"description", "varchar(1024)"});
        fields.add(new String[]{"serverBaseAddress", "varchar(1024)"});
        
        TestConnexion.create("ApplicationServer", fields);
    }
    
    public static void testCreateLinkMMFASSatellitesTable() throws SQLException {
        
        List<String[]> fields = new ArrayList<String[]>();

        fields.add(new String[]{"mmfas", "varchar(10) references MMFAS(mmfasId)"});
        fields.add(new String[]{"satellite", "varchar(10) references SatellitePlatform(satelliteId)"});
        
        TestConnexion.create("LNK_MMFAS_Satellite", fields);
    }
    
    public static void testCreateUnavailibilityTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"unavailibilityId", "serial primary key"});
        fields.add(new String[]{"beginU", "timestamp"});
        fields.add(new String[]{"endU", "timestamp"});
        fields.add(new String[]{"cause", "varchar(1024)"});
        
        //Foreign keys
        fields.add(new String[]{"station", "integer references GroundStation(groundStationId)"});
        fields.add(new String[]{"sensor", "varchar(10) references Sensor(sensorId)"});
        
        TestConnexion.create("Unavailibility", fields);
    }
    
    
}
