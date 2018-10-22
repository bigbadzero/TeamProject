/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;

/**
 *
 * @author Brendan
 */
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.*;
import java.lang.Number;
import java.sql.Timestamp;


public class TASLogic {
    
    public static final int MILLIS_TO_MIN = 60000;
    public static final long MILLIS_TO_HOURS = 3600000;
    public static final int MILLIS_TO_SECS = 1000;
    
    public static int calculateTotalMinutes(ArrayList<Punch> punchList,Shift shift){
        
        /*
        *Calculates the total minutes an employee work in a day
        *Does the bulk of the calcualtion in terms of Milliseconds
        *Before converting the total of milliseconds to minutes
        */
        
        
        int totalMinutes = 0;
        Long totalMillis = new Long(0);
        Punch previous = null;
        boolean inPair = false;
        boolean lunchOut = false;
        boolean lunchIn = false;
        
        //convert instance fields of shift into milliseconds
        long lunchDeduct = shift.getLunchDeduct() * TASLogic.MILLIS_TO_MIN;
        long lunchLength = shift.getLunchLength() * TASLogic.MILLIS_TO_MIN;
        
        for(Punch p: punchList){
            p.adjust(shift);
            
            if(p.getPunchtypeid() == Punch.CLOCKED_IN && !inPair){
                previous = p;
                inPair = true;
            }
            else if(inPair){
                if(p.getPunchtypeid() == Punch.CLOCKED_OUT){
                    totalMillis += p.getAdjustedtimestamp().getTime() - previous.getAdjustedtimestamp().getTime();
                    inPair = false;
                }
                else if(p.getPunchtypeid() == Punch.TIMED_OUT)
                    inPair = false;
            }
            
            if(p.getEventdata().equals(Punch.EVENT_DATA_LUNCH_START))
                lunchOut = true;
            else if(p.getEventdata().equals(Punch.EVENT_DATA_LUNCH_STOP))
                lunchIn = true;
        }
        if(!lunchOut && !lunchIn){
            if(totalMillis > lunchDeduct)
                totalMillis -= lunchLength;
        }
    
        totalMinutes = (new Long(totalMillis/TASLogic.MILLIS_TO_MIN)).intValue();
        
        return totalMinutes;
    }
    
    public static Timestamp forceXafterY(Timestamp x, Timestamp y){
        if(x.before(y)){
            x = new Timestamp(x.getTime() + 24*TASLogic.MILLIS_TO_HOURS);
        }
        
        return x;
    }
    
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist){
        
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        HashMap<String, String>  punchData;
        
        for(int i = 0; i < dailypunchlist.size(); i++){
            Punch punch = dailypunchlist.get(i);
            punchData = new HashMap<>();
            
            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", punch.getBadgeid());
            punchData.put("terminalid", String.valueOf(punch.getTerminalid()));
            punchData.put("punchtypeid",String.valueOf(punch.getPunchtypeid()));
            punchData.put("punchdata", punch.getEventdata());
            punchData.put("originaltimestamp", String.valueOf(punch.getOriginaltimestamp().getTime()));
            punchData.put("adjustedtimestamp", String.valueOf(punch.getAdjustedtimestamp().getTime()));
            
            jsonData.add(punchData);
        }
        
        String json = JSONValue.toJSONString(jsonData);
        return json;
    }
}
