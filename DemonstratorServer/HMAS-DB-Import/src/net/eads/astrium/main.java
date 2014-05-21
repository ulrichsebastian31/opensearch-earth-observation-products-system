package net.eads.astrium;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class main {

	public main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String fichier = args[0];
		
        Connection con = null;
        PreparedStatement pst = null;

    
        String url = "jdbc:postgresql://"+args[1]+"/osresult";
        String user = args[2];
        String password = args[3];

        try {

            con = DriverManager.getConnection(url, user, password);
		
			//lecture du fichier texte	
			try{
				InputStream ips=new FileInputStream(fichier); 
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String ligne;
				int i = 0;
				while ((ligne=br.readLine())!=null){
					if (args.length==5) {
						System.out.println(ligne);
					}
					if (i>0) {
						String[] columns = ligne.split(";");
			            String stm = "INSERT INTO result (platform, \"orbitType\", instrument, \"sensorType\", \"sensorMode\", resolution, \"swathId\", wavelength,"
			            		+ " \"spectralRange\", \"orbitNumber\", \"orbitDirection\", track, frame, identifier, type, \"acquisitionType\", status, \"archivingCenter\", "
			            		+ " \"archivingDate\", \"acquisitionStation\", \"processingCenter\", \"processingSoftware\", \"processingDate\", \"processingLevel\", "
			            		+ " \"compositeType\", contents, \"cloudCover\", \"snowCover\", footprint, \"upperRight\", \"upperLeft\", \"lowerLeft\", \"lowerRight\") VALUES "
			            		+ "('"+columns[0]+"', '"+columns[1]+"', '"+columns[2]+"', '"+columns[3]+"', '"+columns[4]+"', '"+columns[5]+"', '"+columns[6]+"',"
			            		+ " '"+columns[7]+"', '"+columns[8]+"', '"+columns[9]+"', '"+columns[10]+"', '"+columns[11]+"', '"+columns[12]+"', "
			            		+ "'"+columns[13]+"', '"+columns[14]+"', '"+columns[15]+"', '"+columns[16]+"', '"+columns[17]+"', '"+columns[18]+"', "
			            		+ "'"+columns[19]+"', '"+columns[20]+"', '"+columns[21]+"', '"+columns[22]+"', '"+columns[23]+"', '"+columns[24]+"',"
			            		+ "'"+columns[25]+"', '"+columns[26]+"', '"+columns[27]+"', '"+columns[28]+"', "
			            		+ " ST_GeomFromText('POINT("+columns[29]+")', 4326), ST_GeomFromText('POINT("+columns[30]+")', 4326),"
			            		+ " ST_GeomFromText('POINT("+columns[31]+")', 4326), ST_GeomFromText('POINT("+columns[32]+")', 4326));";
						if (args.length==5) {
							System.out.println(columns.length + "   " +stm);
						}
						pst = con.prepareStatement(stm);                   
			            pst.executeUpdate();
					}
					i++;
				}
				System.out.println(i-1 + " records imported");
				br.close(); 
			}		
			catch (Exception e){
				System.out.println(e.toString());
			}
		
        } catch (SQLException ex) {
        	System.out.println(ex.getMessage());

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
            	System.out.println(ex.getMessage());
            }
        }
	}
	
}
