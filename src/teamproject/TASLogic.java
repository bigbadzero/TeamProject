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
import java.lang.Number;

public class TASLogic {
    
    
    
    public static int calculateTotalMinutes(ArrayList<Punch> punchList,Shift shift){
        Long totalMinutes = new Long(0);
        
        Punch punchIn = null;
        Punch punchOut = null;
        Punch lunchOut = null;
        Punch lunchIn = null;
        
        int lunchDeduct = shift.getLunchDeduct();
        int lunchLength = shift.getLunchLength();
        
        for(Punch p: punchList){
            p.adjust(shift);
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
        
        if(punchOut.getPunchtypeid() != Punch.TIMED_OUT){                
            long punchInLong = punchIn.getAdjustedtimestamp().getTime();
            long punchOutLong = punchOut.getAdjustedtimestamp().getTime();
            
            totalMinutes = punchOutLong - punchInLong; 
            
            if((lunchOut != null) && (lunchIn != null))
                totalMinutes = totalMinutes - lunchLength;
            
            else{
                if(totalMinutes >= lunchDeduct)
                    totalMinutes = totalMinutes - lunchLength;   
            }
        }
        
        return totalMinutes.intValue();
    }
}
