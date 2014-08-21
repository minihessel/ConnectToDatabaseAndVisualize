/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dataselector;

import static dataselector.FXMLDocumentController.dataen;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import snaq.db.DBPoolDataSource;

/**
 *
 * @author Eskil
 */
public class SQL_manager {

    public SQL_manager () {
    
        
    }
    
    public void bindSQLdata(String title, ResultSet rs) throws SQLException {
        /* Etter at SQL er oppkoblet henter vi ut dataen fra databasen og binder den til en streng og en double
        Deretter legger vi til strengen og doubleverdien til i dataListen
         */
        System.out.println("HER KOMMER DEN");
        String test = rs.getString("asd");
        // System.out.print(test);
        Integer val = rs.getInt("val");
        dataen.add(new Record(test, val));
        System.out.println(dataen);
    }

    protected void connectToSql(String mySqlAdress, Integer myPort,String sqlInstance) throws SQLException{
       
      
    dataen.clear();
        System.out.print("Kobler til");
      
        try {
            //String myDriver = "org.gjt.mm.mysql.Driver";
            
            String myUrl = "jdbc:mysql://"+mySqlAdress+":"+myPort + "/" + sqlInstance;
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println(myUrl
              + "user=root&password=1234");
            DriverManager.setLoginTimeout(2);
            //Connection conn = DriverManager.getConnection(myUrl, "root", "1234");
            Connection conn = DriverManager.getConnection(myUrl+"?"
              + "user=root&password=1234");
            DriverManager.setLoginTimeout(2);
             String SQL = "SELECT asd,val FROM test";
            ResultSet rs = conn.createStatement().executeQuery(SQL);
               while (rs.next()) {
                /* Kjører metoden for å binde data til dataList(datasettet) */
                System.out.println("faen");
                bindSQLdata("asd", rs);

            }
        } 
      catch (Exception e)
{
  System.err.println("Got an exception! ");
  System.err.println(e.getMessage());
}
    }
    
}
