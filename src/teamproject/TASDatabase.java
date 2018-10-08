/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

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
            long ts = result.getLong("ts");
            Badge badge = this.getBadge(badgeId);
            
            ts = ts*1000;
            Timestamp ots = new Timestamp(ts);
            
            

            punch = new Punch(badge,id, terminalId,ots,ptid);
            
            
            pst.close();
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
            
            
            pst.close();
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
            
            
            pst.close();
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
        
        
        pst.close();
        }
        catch(Exception e){System.err.println(e.getMessage());}
        
        return shift;
    }
    public int insertPunch(Punch punch){
        String badgeId = punch.getBadge().getId();
        int terminalId = punch.getTerminalId();
        Timestamp ots = punch.getOriginalTimestamp();
        int ptid = punch.getPunchTypeId();
        
        try{
            int punchId = 0;
            int results = 0;
            ResultSet keys;
            String sql = "INSERT INTO punch (terminalid,badgeid,originaltimestamp,punchtypeid) VALUES (?,?,?,?);";
            PreparedStatement pst = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            
            pst.setInt(1,terminalId);
            pst.setString(2, badgeId);
            pst.setString(3, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(ots.getTime()));
            pst.setInt(4,ptid);
            
            results = pst.executeUpdate();
            
            if(results == 1){
                keys = pst.getGeneratedKeys();
                if(keys.next()){
                    punchId = keys.getInt(1);
                    punch.setId(punchId);
                }
            }
        }
        catch(Exception e){System.err.println(e.getMessage());}
        
        return punch.getId();
    }
    public ArrayList getDailyPunchList(Badge b, long ts){
        ArrayList<Punch> punchList = new ArrayList();
        
        String badgeId = b.getId();
        Timestamp timestamp = new Timestamp(ts*1000);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(timestamp);
        
        try{
            PreparedStatement pst = conn.prepareStatement("SELECT id FROM punch WHERE badgeid = ? AND originaltimestamp LIKE ?%;");
            pst.setString(1, badgeId);
            pst.setString(2, date); 
            
            result = pst.executeQuery();
            
            while(result.next()){
                int punchId = result.getInt("id");
                Punch punch = this.getPunch(punchId);
                punchList.add(punch);
            }
        }
        catch(Exception e){System.err.println(e.getMessage());}
        
        return punchList;
    }
    
}
