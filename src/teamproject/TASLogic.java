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

public class TASLogic {
    
    
    
    public static int calculateTotalMinutes(ArrayList<Punch> punchList){
        int minutes = 0;
        
        Punch punchIn = null;
        Punch punchOut = null;
        Punch lunchOut = null;
        Punch lunchIn = null;
        
        for(Punch p: punchList){
            if(p.getPunchtypeid() == Punch.CLOCKED_IN){
                if(p.getEventdata().equals(Punch.EVENT_DATA_LUNCH_STOP))
                   lunchIn = p; 
                else{
                    punchIn = p;
                }   
            }
            if(p.getPunchtypeid() == Punch.CLOCKED_OUT){
                if(p.getEventdata().equals(Punch.EVENT_DATA_LUNCH_START))
                    lunchOut = p;
                else{
                    punchOut = p;
                }
            }
        }
        
        return minutes;
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
            punchData.put("eventtypeid",String.valueOf(punch.getPunchtypeid()));
            punchData.put("eventdata", punch.getEventdata());
            punchData.put("originaltimestamp", String.valueOf(punch.getOriginaltimestamp().getTime()));
            
            jsonData.add(punchData);
        }
        
        String json = JSONValue.toJSONString(jsonData);
        return json;
    }
}
