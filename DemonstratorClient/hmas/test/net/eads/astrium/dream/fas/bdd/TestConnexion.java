package net.eads.astrium.dream.fas.bdd;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestConnexion {
    public  static Connection conn = createConnexion();

    private static Connection createConnexion() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");
            
            String url = "jdbc:postgresql://10.2.200.210:5432/MMFASDatabase";
//            String url = "jdbc:postgresql://192.168.0.20:5432/MMFASDatabase";
            
            String user = "opensourcedbms";
            String passwd = "opensourcedbms";
            connection = DriverManager.getConnection(url, user,passwd);
            System.out.println("Connection started !");

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return connection;
    }
    
    @BeforeClass
    public static void init() throws SQLException
    {
        
    }
    
    
//    @Test
    public void testDates() throws SQLException
    {
        deleteTable("TimeOfInterest");
        deleteTable("Segment");
        deleteTable("Status");
        
        List<String[]> toi = new ArrayList<String[]>();
        
        toi.add(new String[]{"toiId", "serial primary key"});
        toi.add(new String[]{"beginTime", "timestamp"});
        toi.add(new String[]{"endTime", "timestamp"});
        
        TestConnexion.create("TimeOfInterest", toi);
        
        List<String[]> status = new ArrayList<String[]>();
        
        status.add(new String[]{"statusId", "serial primary key"});
        status.add(new String[]{"percentCompletion", "integer"});
        status.add(new String[]{"statusMessage", "varchar(100)"});
        status.add(new String[]{"updateTime", "timestamp"});
        status.add(new String[]{"estimatedTOC", "timestamp"});
        
        //Foreign keys
        status.add(new String[]{"previous", "integer references Status(statusId)"});
        
        TestConnexion.create("Status", status);
        
        
        
        
        
        List<String[]> segment = new ArrayList<String[]>();
        
        segment.add(new String[]{"segmentId", "serial primary key"});
        segment.add(new String[]{"coordinates", "varchar(50)"});
        segment.add(new String[]{"acquisitionStartTime", "timestamp"});
        segment.add(new String[]{"acquisitionStopTime", "timestamp"});
        segment.add(new String[]{"acquisitionMethod", "varchar(50)"});
        
        //Foreign keys
        segment.add(new String[]{"feasibility", "integer references FeasibilityAnalysis(faId)"});
        
        TestConnexion.create("Segment", segment);
    }
    
//    @Test
    public void dropEnum() throws SQLException {
//        dropEnum("bandtypes");
        dropEnum("sensortypes");
    }
    
//    @Test
    public void testCreateEnum() throws SQLException {
        
        createEnum("BandTypes", Arrays.asList(new String[]{"'L-BAND'", "'C-BAND'", "'S-BAND'", "'X-BAND'"}));
    }
    
//    @Test
    public void testDelete() throws SQLException {
        
        for (int i = 0; i < reqTables.length; i++) {
            delete(reqTables[i], "*", null);
        }
        for (int i = 0; i < baseTables.length; i++) {
            delete(baseTables[i], "*", null);
        }
    }
    
//    @Test
    public void testAddColumn() throws SQLException  {
        
        try {
            Statement state = conn.createStatement();

            String query = "" +
"ALTER TABLE FAS ADD COLUMN server integer references ApplicationServer(asId);";
        
            System.out.println("" + query);

            state.executeQuery(query);
        }catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query"))
            {
                System.out.println("No results returned");
            }
            else
            {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
    }
    
//    @Test
    public void testInsert() throws SQLException {
        String table = "SatellitePlatform";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("osffile");
//        fields.add("tlefile");
        fields.add("name");
        fields.add("description");
        fields.add("orbittype");
        fields.add("href");

        List<String> sentinel1 = new ArrayList<String>();
        sentinel1.add("'Sentinel1'");
        sentinel1.add("'Sentinel 1 is a SAR satellite part of the Sentinel project.'");
        sentinel1.add("'SSO'");
        sentinel1.add("'http://www.esa.int/Our_Activities/Observing_the_Earth/GMES/Sentinel-1'");
        
        List<String> sentinel2 = new ArrayList<String>();
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
    public void testSelect() throws SQLException {
        
        List<String> fields = new ArrayList<String>();
//            fields.add("SensorId");
//            fields.add("SensorName");
        fields.add("*");

        String table = "spatial_ref_sys";

        List<String> conditions = new ArrayList<String>();
//            conditions.add("satelliteId='sent1'");
//            conditions.add("satelliteId='sent2'");

        select(fields, table, conditions);
    }
    
    @Test
    public void testDrop() throws SQLException {
        System.out.println("length : " + baseTables.length);
        
        for (int i = 0; i < reqTables.length; i++) {
            deleteTable(reqTables[i]);
        }
        for (int i = 0; i < baseTables.length; i++) {
            deleteTable(baseTables[i]);
        }
    }
    
    public static String[] baseTables = {
        "LNK_Task_Sensor",
        "LNK_Task_Station",
        "LNK_Sensor_IM",
        "Unavailibility",
        "SatellitePlatform",
        "FeasibilityAnalysis",
        "Task",
        "Status",
        "Sensor",
        "Segment",
        "GroundStation",
    };
    
    public static String[] reqTables = {
        "LNK_IM_PM",
        "PolarisationMode",
        "OptSensorCharacteristics",
        "SarSensorCharacteristics",
        "OptIMCharacteristics",
        "SarIMCharacteristics",
        "InstrumentMode",
        "Request",
    };
    
    public static void deleteTable(String table) throws SQLException {
        
        Statement state = null;
        try {
            state = conn.createStatement();

            String query = "DROP TABLE " + table + " CASCADE;";

            System.out.println("" + query);

            state.executeQuery(query);
        }catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query"))
            {
                System.out.println("No results returned");
            }
            else
            {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
        state.close();
    }
    
    public static void dropEnum(String name) throws SQLException {
        
        Statement state = conn.createStatement();

        String query = "DROP TYPE " + name + " CASCADE;";

        System.out.println("" + query);
        
        state.executeQuery(query);
    }
    
    public static void createEnum(String name, List<String> values) throws SQLException {
        try {
            Statement state = conn.createStatement();

            String query = "CREATE TYPE " + name + " AS ENUM (";
            for (Object value : values) {
                query += "" + value + ", ";
            }
            query = query.substring(0, query.lastIndexOf(","));
            query += ");";

            System.out.println("" + query);

            state.executeQuery(query);
         
        }catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query"))
            {
                System.out.println("No results returned");
            }
            else
            {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
    }
    
    public static void create(String tableName, List<String[]> fields) throws SQLException {
        try {
            //Create the statement object
            Statement state = conn.createStatement();


            String query = "CREATE TABLE " + tableName + " (";

            for (String[] field : fields) {

                query += "" + field[0] + " " + field[1] + ", ";
            }
            query = query.substring(0, query.lastIndexOf(","));
            query += ");";

            System.out.println("" + query);
            state.executeQuery(query);
            
        }catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query"))
            {
                System.out.println("No results returned");
            }
            else
            {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
    }
    
    public static void addColumn(String table, String column, String columnType) throws SQLException {
        try {
            //Create the statement object
            Statement state = conn.createStatement();

            String query = "ALTER TABLE " + table + " ADD COLUMN " + column + " " + columnType + ";";

            state.executeQuery(query);
        
        }catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query"))
            {
                System.out.println("No results returned");
            }
            else
            {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
    }
    
    public static void addForeignKey(String table, String column, String refTable, String refColumn) throws SQLException {
        
        try {
            select(Arrays.asList(column), table, null);
        }catch (SQLException e) {
            System.out.println("" + e.getMessage());
            if (e.getMessage().contains("does not exist"))
            {
                addColumn(table, column, "integer");
            }
        }
        
        
        try {
            //Create the statement object
            Statement state = conn.createStatement();

            String query = "ALTER TABLE " + table +
                    " ADD  FOREIGN KEY(" + column + ") " +
                    "REFERENCES "+refTable+"("+refColumn+")";

            state.executeQuery(query);
        
        }catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query"))
            {
                System.out.println("No results returned");
            }
            else
            {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
    }
    
    public static Map<String, List<String>> select(List<String> fields, String table, List<String> conditions) throws SQLException {
        //Create the statement object
        Statement state = conn.createStatement();

        
        String query = "SELECT ";
        if (fields == null)
        {
            query += "*, ";
        }
        else
        {
            for (String field : fields) {

                query += "" + field + ", ";
            }
        }
        

        query = query.substring(0, query.lastIndexOf(","));
        query += " FROM " + table;
        
        if (conditions != null && conditions.size() > 0)
        {
            query += " WHERE " + conditions.get(0);
            for (int i = 1; i < conditions.size(); i++) {
                
                query += " AND " + conditions.get(i);
            }
        }
        
        query += ";";

        
        
        //ResultSet object contains the SQL request’s result
        ResultSet result = state.executeQuery(query);

        //Get metaData
        ResultSetMetaData resultMeta = result.getMetaData();
        System.out.println("\n**********************************");

        //Display columns
        for(int i = 1; i <= resultMeta.getColumnCount(); i++)
            System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");

        System.out.println("\n**********************************");

        Map<String, List<String>> results = new HashMap<String, List<String>>();
        
        while(result.next()){
            
                List<String> elements = new ArrayList<String>();
                for(int i = 1; i <= resultMeta.getColumnCount(); i++)
                {
                    System.out.print("\t" + result.getObject(i).toString() + "\t |");
                    elements.add(result.getObject(i).toString());
                }
                results.put(elements.get(0), elements);
/*			
                System.out.print("\t" + result.getString("name") + "\t |");
                System.out.print("\t" + result.getString("dept") + "\t |");
                System.out.print("\t" + result.getString("jobtitle") + "\t |");*/

            System.out.println("\n---------------------------------");
        }
        result.close();
        state.close();
        
        return results;
    }
    
    public static void delete(String table, String column, List<String> values) throws SQLException {
        
        Statement state = null;
        try {
            //Create the statement object
            state = conn.createStatement();

            String query = "DELETE FROM " + table;

            if (values != null && values.size() > 0)
            {
                query += " WHERE";
                for (String id : values) {

                    query += " " + column + " = " + id + " OR";
                }
                query = query.substring(0, query.lastIndexOf("OR"));
            }

            query += ";";
            System.out.println("" + query);

            state.executeQuery(query);

        
        }catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query"))
            {
                System.out.println("No results returned");
            }
            else
            {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
        state.close();
    }
    
    public static void insert(
            String table, 
            List<String> fields, 
            List<List<String>> entries
        ) throws SQLException {
        try {
            //Create the statement object
            Statement state = conn.createStatement();

            String query = "INSERT INTO " + table 
                    + " (";

            for (String field : fields) {
                
                query += "" + field + ",";
            }
            query = query.substring(0, query.lastIndexOf(","));
            query += ") VALUES ";
            
            for (List<String> entry : entries) {
                
                query += "(";
                for (String value : entry)
                {
                    query += "" + value + ",";
                }
                query = query.substring(0, query.lastIndexOf(","));
                query += "),";
            }
                query = query.substring(0, query.lastIndexOf(","));
            
            query += ";";
            
            System.out.println("" + query);
            state.executeQuery(query);
        
        }catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query"))
            {
                System.out.println("No results returned");
            }
            else
            {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
    }
    
    public static boolean insertFile
			(String table, String idColumn, String fileColumn, 
                        String id, InputStream fileContent, long fileSize) 
			throws IOException, SQLException {
        
        boolean insere = false;
        
        try 
        {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE " + table + " SET " + fileColumn + " = '?' WHERE " + idColumn + " = '?';");

            try 
            {
                ps.setBinaryStream(1, fileContent, (int)fileSize);
                ps.setString(2, id);
                ps.executeUpdate();
		
                insere = true;
            }
            finally 
            {
              ps.close();
            }
        } 
        finally 
        {
                fileContent.close();
        }

        return insere;
    }
    
    
    
    
}
