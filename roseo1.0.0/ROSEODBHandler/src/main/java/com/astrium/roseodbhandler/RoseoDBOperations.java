/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DBOperations.java
 *   File Type                                          :               Source Code
 *   Description                                        :                *
 * --------------------------------------------------------------------------------------------------------
 *
 * =================================================================
 *             (coffee) COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *             This software is supplied by EADS Astrium Limited on the express terms
 *             that it is to be treated as confidential and that it may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astrium.roseodbhandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import java.util.ResourceBundle;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author re-sulrich
 */
public class RoseoDBOperations {

//    protected final Connection connection;

//    protected DBOperations() {
//        
////        connection = createConnexionTomcat();
//    }
//    
//
//    public DBOperations(String databaseURL, String user, String pass) {
//        
////        connection = createConnexion(databaseURL, user, pass);
//    }
    
//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize(); //To change body of generated methods, choose Tools | Templates.
//        
//        if (connection != null) connection.close();
//    }
    
    protected Connection connection;
    //Map containing the connections to all the Databases
    protected static Map<String, Connection> connections;
    
    public RoseoDBOperations(String databaseName) {
        
        if (connections == null) {
            connections = new HashMap<>();
        }
        
        if (connections.containsKey(databaseName)) {
            
            System.out.println("Using connection for " + databaseName);
            connection = connections.get(databaseName);
        }
        else {
            System.out.println("");
            System.out.println("Create connexion for " + databaseName);
            System.out.println("");
            connection = createConnexionTomcat(databaseName);
            connections.put(databaseName, connection);
        }
    }
    
    /**
     * Constructor for local testing. 
     * Creates the connection for normal Java applications. 
     * The default (static) constructor uses Tomcat properties from the context.xml file to create the connection.
     * @param databaseURL
     * @param user
     * @param pass 
     */
    public RoseoDBOperations(String databaseURL, String user, String pass) {
        
        connection = createConnexion(databaseURL, user, pass);
    }

    private static Connection createConnexion(String databaseURL, String user, String pass) {
        Connection conn = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            
            System.out.println("Driver O.K.");
            
            conn = DriverManager.getConnection(databaseURL, user, pass);
            
            System.out.println("Connection started !");

        } catch (Exception e) {
            
            e.printStackTrace();
        }

        return conn;
    }
    
    private static Connection createConnexionTomcat(String databaseName) {
        
        Connection conn = null;
        
        try {
            
            Class.forName("org.postgresql.Driver");
            
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)envContext.lookup("jdbc/" + databaseName);
            conn = ds.getConnection();
            
            System.out.println("Driver O.K.");
            
            
            System.out.println("Connection started !");

        } catch (Exception e) {
//            e.printStackTrace();
        }

        return conn;
    }

    public void deleteTable(String table)
            throws SQLException {
        
        Statement state = null;
        try {
            //Create the statement object
            state = connection.createStatement();
            
            String query = "DROP TABLE " + table + " CASCADE;";

//            System.out.println("" + query);

            state.executeQuery(query);
        } catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query")) {
//                System.out.println("No results returned");
            } else {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
    }

    public void createEnum(String name, List<String> values)
            throws SQLException {
        
        Statement state = null;
        try {
            //Create the statement object
            state = connection.createStatement();
            
            String query = "CREATE TYPE " + name + " AS ENUM (";
            for (Object value : values) {
                query += "" + value + ", ";
            }
            query = query.substring(0, query.lastIndexOf(","));
            query += ");";

    //        System.out.println("" + query);

            state.executeQuery(query);
            
        } catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query")) {
//                System.out.println("No results returned");
            } else {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
    }

    public void create(String tableName, List<String[]> fields)
            throws SQLException {
        
        Statement state = null;
        try {
            //Create the statement object
            state = connection.createStatement();
            

            String query = "CREATE TABLE " + tableName + " (";

            for (String[] field : fields) {

                query += "" + field[0] + " " + field[1] + ", ";
            }
            query = query.substring(0, query.lastIndexOf(","));
            query += ");";

//            System.out.println("" + query);
            state.executeQuery(query);

        } catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query")) {
//                System.out.println("No results returned");
            } else {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
    }

    public void addColumn(String table, String column, String columnType)
            throws SQLException {
        
        Statement state = null;
        try {
            //Create the statement object
            state = connection.createStatement();
            
            String query = "ALTER TABLE " + table + " ADD COLUMN " + column + " " + columnType + ";";

            state.executeQuery(query);

        } catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query")) {
//                System.out.println("No results returned");
            } else {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
        finally {
            if (state != null) state.close();
        }
    }

    public void addForeignKey(String table, String column, String refTable, String refColumn)
            throws SQLException {

        try {
            select(Arrays.asList(column), table, null);
        } catch (SQLException e) {
            System.out.println("" + e.getMessage());
            if (e.getMessage().contains("does not exist")) {
                addColumn(table, column, "integer");
            }
        }

        Statement state = null;
        try {
            //Create the statement object
            state = connection.createStatement();
            
            String query = "ALTER TABLE " + table
                    + " ADD  FOREIGN KEY(" + column + ") "
                    + "REFERENCES " + refTable + "(" + refColumn + ")";

            state.executeQuery(query);

        } catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query")) {
//                System.out.println("No results returned");
            } else {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
        finally {
            if (state != null) state.close();
        }
    }

    public int count(String table, List<String> conditions)
            throws SQLException {

        int nbResults = 0;

        Statement state = connection.createStatement();

        String query = "SELECT COUNT(*) FROM " + table;

        if (conditions != null && conditions.size() > 0) {
            query += " WHERE " + conditions.get(0);
            for (int i = 1; i < conditions.size(); i++) {

                query += " AND " + conditions.get(i);
            }
        }


        query += ";";
        
        System.out.println("" + query);

        ResultSet result = state.executeQuery(query);
        result.next();
        nbResults = result.getInt(1);

        return nbResults;
    }

    public List<List<String>> select(List<String> fields, String table, List<String> conditions)
            throws SQLException {

        int nbResults = count(table, conditions);
        if (nbResults == 0) {
            System.out.println("No results, count null.");
            return null;
        }

        List<List<String>> results = null;

        Statement state = null;
        try {
            
            //Create the statement object
            state = connection.createStatement();
            

            String query = "SELECT ";
            if (fields == null || fields.size() == 0) {
                query += "*, ";
            } else {

                for (String field : fields) {

                    query += "" + field + ", ";
                }
            }

            query = query.substring(0, query.lastIndexOf(","));
            query += " FROM " + table;

            if (conditions != null && conditions.size() > 0) {
                query += " WHERE " + conditions.get(0);
                for (int i = 1; i < conditions.size(); i++) {

                    query += " AND " + conditions.get(i);
                }
            }

            query += ";";

//            System.out.println("" + query);
            
            //ResultSet object contains the SQL request’s result
            ResultSet result = state.executeQuery(query);

            //Get metaData
            ResultSetMetaData resultMeta = result.getMetaData();
    //        System.out.println("\n**********************************");

            //Display columns
    //        for (int i = 1; i <= resultMeta.getColumnCount(); i++) {
    //            System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");
    //        }

    //        System.out.println("\n**********************************");

            results = new ArrayList<List<String>>();
            
            for (int nbRes = 0; nbRes < nbResults; nbRes++) {

                result.next();

                List<String> elements = new ArrayList<String>();
                for (int i = 1; i <= resultMeta.getColumnCount(); i++) {
                    Object e = result.getObject(i);
                    String element = "";
                    if (e != null) {
                        element = e.toString();
                    }

    //                System.out.print("\t" + element + "\t |");
                    elements.add(element);
                }
                results.add(elements);

    //            System.out.println("\n---------------------------------");
                
            }
            
            result.close();
            
        } catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query")) {
//                System.out.println("No results returned");
            } else {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
        finally {
            if (state != null) state.close();
        }

//        System.out.println("Results : " + results.size());
        
        return results;
    }

    public List<List<String>> selectOr(List<String> fields, String table, List<String> conditions)
            throws SQLException {

        int nbResults = count(table, conditions);
        if (nbResults == 0) {
            return null;
        }
//        System.out.println("");

        
        List<List<String>> results = null;

        Statement state = null;
        try {
            
            //Create the statement object
            state = connection.createStatement();
            

            String query = "SELECT ";
            if (fields == null) {
                query += "*, ";
            } else {
                for (String field : fields) {

                    query += "" + field + ", ";
                }
            }

            query = query.substring(0, query.lastIndexOf(","));
            query += " FROM " + table;

            if (conditions != null && conditions.size() > 0) {
                query += " WHERE " + conditions.get(0);
                for (int i = 1; i < conditions.size(); i++) {

                    query += " OR " + conditions.get(i);
                }
            }

            query += ";";

    //        System.out.println("" + query);

            //ResultSet object contains the SQL request’s result
            ResultSet result = state.executeQuery(query);

            //Get metaData
            ResultSetMetaData resultMeta = result.getMetaData();
    //        System.out.println("\n**********************************");

            //Display columns
    //        for (int i = 1; i <= resultMeta.getColumnCount(); i++) {
    //            System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");
    //        }

    //        System.out.println("\n**********************************");

            results = new ArrayList<List<String>>();

            for (int nbRes = 0; nbRes < nbResults; nbRes++) {

                result.next();

                List<String> elements = new ArrayList<String>();
                for (int i = 1; i <= resultMeta.getColumnCount(); i++) {
                    Object e = result.getObject(i);
                    String element = "";
                    if (e != null) {
                        element = e.toString();
                    }

    //                System.out.print("\t" + element + "\t |");
                    elements.add(element);
                }
                results.add(elements);

    //            System.out.println("\n---------------------------------");
            }
            
            
            result.close();
            
        } catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query")) {
//                System.out.println("No results returned");
            } else {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
        finally {
            if (state != null) state.close();
        }

        return results;
    }

    
    
    
    
    
    
    public void delete(String table, String idColumn, List<String> values) throws SQLException {

        Statement state = null;
        try {
            
            //Create the statement object
            state = connection.createStatement();
            
            String query = "DELETE FROM " + table;

            if (values != null && values.size() > 0) {
                query += " WHERE";
                for (String id : values) {

                    query += " " + idColumn + " = " + id + " OR";
                }
                query = query.substring(0, query.lastIndexOf("OR"));
            }

            query += ";";
            
            System.out.println("" + query);

            state.executeQuery(query);
 
        } catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query")) {
//                System.out.println("No results returned");
            } else {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
        finally {
            if (state != null) state.close();
        }

    }

    
    public void update(String table, List<String> fields, List<String> values, String condition) throws SQLException {
        update(table, fields, values, condition, null);
    }
    
    public String update(String table, List<String> fields, List<String> values, String condition, String idColumn) throws SQLException {
        
        String id = null;
        
        Statement state = null;
        try {
            
            //Create the statement object
            state = connection.createStatement();
            
            String query = "UPDATE " + table
                    + " SET (";
        
            for (String field : fields) {
                query += "" + field + ", ";
            }
            query = query.substring(0, query.lastIndexOf(","));
            
            query += ") = (";
            
            for (String value : values) {
                query += "" + value + ", ";
            }
            
            query = query.substring(0, query.lastIndexOf(","));
            
            query += ") WHERE " + condition + "";

            
            if (idColumn != null&& !idColumn.isEmpty()) {
                
                query += " RETURNING " + idColumn;
            }
            
            query += ";";
            
            System.out.println("" + query);
            
            ResultSet result = state.executeQuery(query);

            result.next();
            id = result.getString(1);
            
        } catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query")) {
//                System.out.println("No results returned");
            } else {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
        finally {
            if (state != null) state.close();
        }
        
        return id;
    }
    
    
    /**
     * Insert in the given table
     * for the given fields,
     * the given values
     * @param table
     * @param fields    The titles of the columns to be assigned
     * @param entries   A list of "entry", each "entry" must contain as much elements as the "fields" list
     * @throws SQLException 
     */
    public void insert(
            String table,
            List<String> fields,
            List<List<String>> entries) throws SQLException {
        
        Statement state = null;
        try {
            
            //Create the statement object
            state = connection.createStatement();
            
            String query = "INSERT INTO " + table
                    + " (";

            for (String field : fields) {

                query += "" + field + ",";
            }
            query = query.substring(0, query.lastIndexOf(","));
            query += ") VALUES ";

            for (List<String> entry : entries) {

                query += "(";
                for (String value : entry) {
                    query += "" + value + ",";
                }
                query = query.substring(0, query.lastIndexOf(","));
                query += "),";
            }
            query = query.substring(0, query.lastIndexOf(","));

            query += ";";

//            System.out.println("" + query);
            
            state.executeQuery(query);

        } catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query")) {
//                System.out.println("No results returned");
            } else {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
        finally {
            if (state != null) state.close();
        }
    }
    
    
    /**
     * Insert in the given table
     * for the given fields,
     * the given values
     * @param table
     * @param fields
     * @param entries
     * @throws SQLException 
     */
    public String insertReturningId(
            String table,
            List<String> fields,
            List<List<String>> entries,
            String idColumn) throws SQLException {
        
        String id = null;
        
        Statement state = null;
        try {
            
            //Create the statement object
            state = connection.createStatement();
            
            String query = "INSERT INTO " + table
                    + " (";

            for (String field : fields) {

                query += "" + field + ",";
            }
            query = query.substring(0, query.lastIndexOf(","));
            query += ") VALUES ";

            for (List<String> entry : entries) {

                query += "(";
                for (String value : entry) {
                    query += "" + value + ",";
                }
                query = query.substring(0, query.lastIndexOf(","));
                query += "),";
            }
            query = query.substring(0, query.lastIndexOf(","));

            //RETURNING
            query += "RETURNING "+idColumn+";";
            
            query += ";";

            System.out.println("" + query);
            
            ResultSet result = state.executeQuery(query);

            
            result.next();
            id = result.getString(1);
            
            System.out.println("Id : " + id);
            
            
        } catch (SQLException e) {
            if (e.getMessage().contains("No results were returned by the query")) {
//                System.out.println("No results returned");
            } else {
                System.out.println("Mess : " + e.getMessage());
                throw e;
            }
        }
        finally {
            if (state != null) state.close();
        }
        
        return id;
    }
    
    
    public String insertFileForNewId(String table, String idColumn, String fileColumn, InputStream fileContent, long fileSize)
            throws IOException, SQLException {

        String id = null;
        
        boolean insere = false;

        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(
                    "INSERT INTO "+table+" ("+fileColumn+") VALUES (?) RETURNING "+idColumn+";");
            
            
            System.out.println("INSERT INTO "+table+" ("+fileColumn+") VALUES (?) RETURNING "+idColumn+";\nSize : " + fileSize);
            try {
                
                byte[] b = new byte[(int) fileSize];
                fileContent.read(b);
                ps.setBytes(1, b);
                
                ResultSet res = ps.executeQuery();
                if (res != null) {
                    res.next();
                    id = res.getString(1);
                }

                insere = true;
            } finally {
            }
        } finally {
            if(ps != null) ps.close();
            fileContent.close();
        }

        return id;
    }
    
    
    public boolean insertFileInExistingId(String table, String idColumn, String fileColumn,
            String id, InputStream fileContent, long fileSize)
            throws IOException, SQLException {

        boolean insere = false;

        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(
                    "UPDATE "+table+" SET "+fileColumn+" = ? WHERE "+idColumn+" = ?;");
            
            try {
                
                byte[] b = new byte[(int) fileSize];
                fileContent.read(b);
                ps.setBytes(1, b);
                
                try {
                    ps.setInt(2, Integer.valueOf(id));
                    
//                    System.out.println("Parsed : " + Integer.valueOf(id));
                    
                } catch(NumberFormatException e) {
                    ps.setString(2, ""+id+"");
                }
                try {
                ps.executeQuery();
                } catch (SQLException e) {
                    if (!e.getMessage().contains("No results were returned by the query")) {
                        throw e;
                    }
                }
                insere = true;
            } finally {
            }
        } finally {
            if(ps != null) ps.close();
            fileContent.close();
        }

        return insere;
    }
    
    
    
    
    public Path loadFileOnDisk(
            String dbFileColumn,
            String dbTable, 
            String condition,
            String filePath
            ) throws SQLException, IOException {
        
        Statement state = null;
        Path path = null;
        try {
            state = connection.createStatement();
            String query = "SELECT "+dbFileColumn+" FROM "+dbTable+" WHERE "+condition+";";
            
            System.out.println("" + query);
            
            ResultSet rs = state.executeQuery(query);
            if (rs.next()) {
                InputStream is = rs.getBinaryStream(1);
                path = Paths.get(filePath);

                if (path == null) {
                    throw new IOException("why is path null ?");
                }
                if (is == null) {
                    System.out.println("IS null..");
                }
                
                Files.createDirectories(path.getParent());
                if (Files.exists(path)) {
                    Files.delete(path);
                }
                Files.createFile(path);
                Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);

            } else {
                return null;
            }
        } finally {
            if (state != null) state.close();
        }
        
        return path;
        
    }
    
}
