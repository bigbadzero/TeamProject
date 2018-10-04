/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

public class TASDatabase {
    
    Connection connection = null;
    
    
    
    public TASDatabase(){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TAS_FA18?user=tasuser&password=CS310&useSSL=false");
        }
        catch(Exception e){System.err.println(e.getMessage());}
    }
    
    public void close(){
        
    }
    public Badge createBadge(){
        Badge badge = null;
        
        return badge;
    }
    public Shift createShift(){
        Shift shift = null;
        
        return shift;
    }
    public Punch createPunch(){
        Punch punch = null;
        
        return punch;
    }
    public Punch getPunch(int id){
        Punch punch = null;
        
        return punch;
    }
    public Badge getBadge(String id){
        Badge badge = null;
        
        return badge;
    }
    public Shift getShift(int id){
        Shift shift = null;
        
        return shift;
    }
    public Shift getShift(Badge badge){
        Shift shift = null;
        
        return shift;
    }
    
}
