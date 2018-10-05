/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;


import java.sql.*;

public class TASDatabase {
    
    private Connection conn = null;
    private Statement stmt = null;
    
    private ResultSet result = null;
    
    
    
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
        
        try{
            if(result != null)
                result.close();
            if(stmt != null)
                stmt.close();
            
            if(conn != null)
                conn.close();
        }
        catch(Exception e){System.err.println(e.getMessage());}
    }
    
    public Punch getPunch(int id){
        Punch punch = null;
        
        try{
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM punch WHERE id=?;");
            pst.setInt(1,id);
            
            result = pst.executeQuery();
            result.next();
            
            String badgeId = result.getString("badgeid");
            int terminalId = result.getInt("terminalid");
            int punchTypeId = result.getInt("punchtypeid");
            Badge badge = this.getBadge(badgeId);

            punch = new Punch(badge, terminalId, punchTypeId);
        }
        catch(Exception e){System.err.println(e.getMessage());}
        
        
        return punch;
    }
    public Badge getBadge(String id){
        Badge badge = null;
        
        try{
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM badge WHERE id=?;");
            pst.setString(1,id);
            
            result = pst.executeQuery();
            result.next();
            
            String badgeDesc = result.getString("description");
            
            badge = new Badge(id, badgeDesc);
        }
        catch(Exception e){System.err.println(e.getMessage());}
        
        return badge;
    }
    public Shift getShift(int id){
        Shift shift = null;
        try{
            
        }
        catch(Exception e){System.err.println(e.getMessage());}
        return shift;
    }
    public Shift getShift(Badge badge){
        Shift shift = null;
        
        return shift;
    }
    
}
