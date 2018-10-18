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
    
    public TASDatabase(){
        
        try{
           
            
            String url = "jdbc:mysql://localhost/tas";
            String username = "tasuser";
            String password = "CS310";
            conn = DriverManager.getConnection(url,username,password);
        }
        catch(Exception e){System.err.println(e.getMessage());}
    }
    

    
    public void close(){
        
        try{
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
            
            ResultSet result = pst.executeQuery();
            result.next();
            
            String badgeId = result.getString("badgeid");
            
            int terminalId = result.getInt("terminalid");
            int ptid = result.getInt("punchtypeid");
            long ts = result.getLong("ts");
            Badge badge = this.getBadge(badgeId);
            
            ts = ts*1000;
            Timestamp ots = new Timestamp(ts);
            
            

            punch = new Punch(badge,id, terminalId,ots,ptid);
            
            result.close();
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
            
            ResultSet result = pst.executeQuery();
            result.next();
                       
            String badgeDesc = result.getString("description");
            
            badge = new Badge(id, badgeDesc);
            
            result.close();
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
            
            ResultSet result = pst.executeQuery();
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
            
            result.close();
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
        
        ResultSet result = pst.executeQuery();
        result.next();
        
        int shiftId = result.getInt("shiftid");
        
        shift = this.getShift(shiftId);
        
        result.close();
        pst.close();
        }
        catch(Exception e){System.err.println(e.getMessage());}
        
        return shift;
    }
    public int insertPunch(Punch punch){
        String badgeId = punch.getBadge().getId();
        int terminalId = punch.getTerminalid();
        Timestamp ots = punch.getOriginaltimestamp();
        int ptid = punch.getPunchtypeid();
        
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
            
            pst.close();
        }
        catch(Exception e){System.err.println(e.getMessage());}
        
        return punch.getId();
    }
    public ArrayList getDailyPunchList(Badge b, long ts){
        ArrayList<Punch> punchList = new ArrayList();
        
        String badgeId = b.getId();
        Timestamp timestamp = new Timestamp(ts);
        Timestamp followingTimestamp = new Timestamp(timestamp.getTime() + 24*Shift.MILLIS_TO_HOURS);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(timestamp);
        String followingDate = new SimpleDateFormat("yyyy-MM-dd").format(followingTimestamp);
        
  
        try{
            PreparedStatement pst1 = conn.prepareStatement("SELECT id FROM punch WHERE badgeid = ? AND originaltimestamp LIKE ?;"); 
            pst1.setString(1, badgeId);
            pst1.setString(2, date + "%"); 
            
            ResultSet result1 = pst1.executeQuery();
            while(result1.next()){
                int punchId = result1.getInt("id");
                Punch punch = this.getPunch(punchId);
                punchList.add(punch);
            }
            
            PreparedStatement pst2 = conn.prepareStatement("SELECT * FROM punch WHERE badgeid = ? AND originaltimestamp LIKE ? LIMIT 1;");
            pst2.setString(1, badgeId);
            pst2.setString(2, followingDate + "%");
            
            ResultSet result2 = pst2.executeQuery();
            if(result2.next()){
                if(result2.getInt("punchtypeid") == Punch.CLOCKED_OUT){
                    int punchId = result2.getInt("id");
                    Punch punch = this.getPunch(punchId);
                    punchList.add(punch);
                }
            }
                
            
            result1.close();
            pst1.close();
            result2.close();
            pst2.close();
        }
        catch(Exception e){System.err.println(e.getMessage());}
        
        return punchList;
    }
    
}
