/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;


import java.sql.*;

public class TASDatabase {
    
    Connection conn = null;
    Statement stmt = null;
    ResultSet result = null;
    
    
    
    public TASDatabase(){
        
        
        
        
        try{
           
            
            String url = "jdbc:mysql://localhost/tas";
            String username = "tasuser";
            String password = "CS310";
            conn = DriverManager.getConnection(url,username,password);
            stmt = conn.createStatement();
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
