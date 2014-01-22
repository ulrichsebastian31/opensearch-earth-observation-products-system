/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astrium.roseodbinitializer;

import com.astrium.roseodbhandler.RoseoManagementHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import org.apache.commons.io.IOUtils;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author re-cetienne
 */
public class PopulateDatabase {

    private static RoseoManagementHandler roseoManagementHandler;

    public static final String eoProduct1 = "f9565cf6dba885cf5e25150f936fd9c1";
    public static final String eoProduct2 = "e8fb3edf039b2b2253ea265b200b15e7";
    public static final String eoProduct3 = "e8fb3edf039b2b2253ea265b200b15e8";
    public static final String eoProduct4 = "e8fb3edf039b2b2253ea265b200b15e9";
    public static final String eoProduct5 = "e8fb3edf039b2b2253ea265b200b15z7";
    public static final String eoProduct6 = "f9565cf6dba885bf5e25150f936fd9c1";
    public static final String eoProduct7 = "f9565cf6dba885cf5e25150f936fd9c2";
    public static final String eoProduct8 = "f9565cf6dba885cf5e25150f936zd9c1";

    public static final String collection1 = "ESA.EECF.ENVISAT_ASA_IMx_xS";
    public static final String collection2 = "ESA.EECF.ENVISAT_ASA_IMx_xF";

    public static String order1 = "25d0587bf7ed8e0ea26f9e5359391cf1";
    public static String order2 = "ad6807775ea093bf1ea77c7c6adfbcea";

    public static final String item1 = "item1";
    public static final String item2 = "item2";
    public static final String item3 = "item3";
    public static final String item4 = "item4";
    public static final String item5 = "item5";
    public static final String item6 = "item6";
    public static final String item7 = "item7";
    public static final String item8 = "item8";

    JSONParser parser = new JSONParser();

    public void populate() throws Exception {
        roseoManagementHandler = new RoseoManagementHandler("jdbc:postgresql://" + ConnexionParameter.getUrl() + "/" + ConnexionParameter.getRoseoDatabase(),
                ConnexionParameter.getUser(),
                ConnexionParameter.getPass());

        testAddEOProducts();
        testAddCollection();
        testAddOrder();
        testAddOrderItem();
    }

    public void testAddEOProducts() throws SQLException, IOException, org.json.simple.parser.ParseException {
        
        InputStream optFile1 = new FileInputStream(System.getProperty("user.home") + File.separator + "hmas" + File.separator + "options1.json");
        String opt1 = IOUtils.toString(optFile1,"UTF-8");
        
        InputStream optFile2 = new FileInputStream(System.getProperty("user.home") + File.separator + "hmas" + File.separator + "options2.json");
        String opt2 = IOUtils.toString(optFile2,"UTF-8");
      
        roseoManagementHandler.addEOProduct(eoProduct1, null, opt1,"2013-08-13T00:00:00Z");
        roseoManagementHandler.addEOProduct(eoProduct2, null, opt1,"2013-12-11T00:00:00Z");
        roseoManagementHandler.addEOProduct(eoProduct3, null, opt2,"2013-09-11T00:00:00Z");
        roseoManagementHandler.addEOProduct(eoProduct4, collection1, null,"2013-08-12T00:00:00Z");
        roseoManagementHandler.addEOProduct(eoProduct5, collection1, null,"2013-08-12T00:00:00Z");
        roseoManagementHandler.addEOProduct(eoProduct6, collection2, null,"2013-10-23T00:00:00Z");
        roseoManagementHandler.addEOProduct(eoProduct7, collection2, null,"2013-10-26T00:00:00Z");
        roseoManagementHandler.addEOProduct(eoProduct8, collection2, null,"2013-10-13T00:00:00Z");

    }

    public void testAddCollection() throws Exception {
        
        InputStream optFile3 = new FileInputStream(System.getProperty("user.home") + File.separator + "hmas" + File.separator + "options3.json");
        String opt3 = IOUtils.toString(optFile3,"UTF-8");
        
        InputStream optFile4 = new FileInputStream(System.getProperty("user.home") + File.separator + "hmas" + File.separator + "options4.json");
        String opt4 = IOUtils.toString(optFile4,"UTF-8");
        
        roseoManagementHandler.addCollection(collection1, opt3);
        roseoManagementHandler.addCollection(collection2, opt4);
    }

    public void testAddOrder() throws Exception {
        roseoManagementHandler.addOrder(order1, "InProduction","2013-08-12T00:00:00Z","Order_1", "item1,item2,item3");
        roseoManagementHandler.addOrder(order2, "InProduction","2013-10-23T00:00:00Z","Order_2", "item6,item7,item8");
    }

    public void testAddOrderItem() throws Exception {
        
        InputStream optSetFile1 = new FileInputStream(System.getProperty("user.home") + File.separator + "hmas" + File.separator + "opt1Set.json");
        String optSet1 = IOUtils.toString(optSetFile1,"UTF-8");
        
        InputStream optSetFile2 = new FileInputStream(System.getProperty("user.home") + File.separator + "hmas" + File.separator + "opt2Set.json");
        String optSet2 = IOUtils.toString(optSetFile2,"UTF-8");
        
        InputStream optSetFile3 = new FileInputStream(System.getProperty("user.home") + File.separator + "hmas" + File.separator + "opt3Set.json");
        String optSet3 = IOUtils.toString(optSetFile3,"UTF-8");
        
        InputStream optSetFile4 = new FileInputStream(System.getProperty("user.home") + File.separator + "hmas" + File.separator + "opt4Set.json");
        String optSet4 = IOUtils.toString(optSetFile4,"UTF-8");
        
        roseoManagementHandler.addOrderItem(item1, eoProduct1, optSet1, "Completed");
        roseoManagementHandler.addOrderItem(item2, eoProduct2, optSet1, "Submitted");
        roseoManagementHandler.addOrderItem(item3, eoProduct3, optSet2, "Completed");
//        roseoManagementHandler.addOrderItem(item4, eoProduct4, optSet3, "InProduction");
//        roseoManagementHandler.addOrderItem(item5, eoProduct5, optSet3, "InProduction");
        roseoManagementHandler.addOrderItem(item6, eoProduct6, optSet4, "Submitted");
        roseoManagementHandler.addOrderItem(item7, eoProduct7, optSet4, "InProduction");
        roseoManagementHandler.addOrderItem(item8, eoProduct8, optSet4, "InProduction");
    }

}
