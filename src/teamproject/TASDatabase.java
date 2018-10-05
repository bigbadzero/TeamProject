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
            PreparedStatement pst = conn.prepareStatement("SELECT *, UNIX_TIMESTAMP(originaltimestamp) AS ts FROM punch WHERE id = ?;");
            pst.setInt(1,id);
            
            result = pst.executeQuery();
            result.next();
            
            String badgeId = result.getString("badgeid");
            
            int terminalId = result.getInt("terminalid");
            int ptid = result.getInt("punchtypeid");
            Badge badge = this.getBadge(badgeId);
            long ts = result.getLong("ts");
            ts = ts*1000;
            Timestamp ots = new Timestamp(ts);
            
            

            punch = new Punch(badge,id, terminalId,ots,ptid);
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
            PreparedStatement pst = conn.prepareStatement("SELECT description, `interval`, graceperiod, dock, lunchdeduct, "
                    + "UNIX_TIMESTAMP(`start`) AS `start`, UNIX_TIMESTAMP(`stop`) AS `stop`, UNIX_TIMESTAMP(lunchstart) AS lunchstart, "
                    + "UNIX_TIMESTAMP(lunchstop) AS lunchstop FROM shift WHERE id=?;");
            pst.setInt(1, id);
            
            result = pst.executeQuery();
            result.next();
            
            String desc = result.getString("description");
            
            
            Timestamp start = new Timestamp(result.getLong("start") *1000);
            Timestamp stop = new Timestamp(result.getLong("stop") *1000);
            int interval = result.getInt("interval");
            int gracePeriod = result.getInt("graceperiod");
            int dock = result.getInt("dock");
            Timestamp lunchStart = new Timestamp(result.getLong("lunchstart") *1000);
            Timestamp lunchStop = new Timestamp(result.getLong("lunchstop") *1000);
            //lunch length
            int lunchDeduct = result.getInt("lunchdeduct");
            
            shift = new Shift(id,desc, start,stop,interval,gracePeriod,dock,lunchStart,lunchStop,lunchDeduct );
            
            
        }
        catch(Exception e){System.err.println(e.getMessage());}
        return shift;
    }
    public Shift getShift(Badge badge){
        Shift shift = null;
        try{
        String badgeID = badge.getId();
        
        PreparedStatement pst = conn.prepareStatement("SELECT shiftid FROM employee WHERE badgeid =?;");
        pst.setString(1, badgeID);
        
        result = pst.executeQuery();
        result.next();
        
        int shiftId = result.getInt("shiftid");
        
        shift = this.getShift(shiftId);
        }
        catch(Exception e){System.err.println(e.getMessage());}
        
        return shift;
    }
    
}
