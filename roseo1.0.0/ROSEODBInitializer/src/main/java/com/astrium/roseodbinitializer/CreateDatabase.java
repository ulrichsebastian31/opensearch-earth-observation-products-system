package com.astrium.roseodbinitializer;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class CreateDatabase 
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Database Creation Beginning........................." );
        ConnexionParameter.setUrl(args[0]);
        ConnexionParameter.setUser(args[1]);
        ConnexionParameter.setPass(args[2]);
        ConnexionParameter.setRoseoDatabase("ROSEODatabase");
        
        Connexion.testDrop();
        CreateROSEOStruct.createROSEOStruct();
        new PopulateDatabase().populate();
    }
}
