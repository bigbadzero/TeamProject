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
import java.sql.Timestamp;


public class TASLogic {
    
    
    
    public static int calculateTotalMinutes(ArrayList<Punch> punchList,Shift shift){
        int totalMinutes = 0;
        Long totalMillis = new Long(0);
        
        int lunchDeduct = shift.getLunchDeduct();
        int lunchLength = shift.getLunchLength();
        
        Punch punchIn = null;
        Punch punchOut = null;
        Punch lunchOut = null;
        Punch lunchIn = null;
        
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
        System.out.println(lunchDeduct);
        System.out.println(lunchLength);
        System.out.println(punchIn.printAdjustedTimestamp());
        System.out.println(punchOut.printAdjustedTimestamp());
        System.out.println(lunchIn);
        System.out.println(lunchOut);
        
        if(punchOut.getPunchtypeid() != Punch.TIMED_OUT){
            Long punchInMillis = punchIn.getAdjustedtimestamp().getTime();
            Long punchOutMillis = punchOut.getAdjustedtimestamp().getTime();
            
            totalMillis = punchOutMillis - punchInMillis;
            
            if((lunchOut != null) && (lunchIn != null)){ 
                totalMillis = totalMillis - (lunchLength * Shift.MILLIS_TO_MIN);
            }
            else{
                System.out.println(totalMillis/Shift.MILLIS_TO_MIN);
                if((totalMillis/Shift.MILLIS_TO_MIN) >= lunchDeduct){
                    totalMillis = totalMillis - (lunchLength * Shift.MILLIS_TO_MIN);
                }
            }
            
        }
    
        totalMinutes = (new Long(totalMillis/Shift.MILLIS_TO_MIN)).intValue();
        
        
        return totalMinutes;
    }
    
    public static Timestamp forceXafterY(Timestamp x, Timestamp y){
        if(x.before(y)){
            x = new Timestamp(x.getTime() + 24*Shift.MILLIS_TO_HOURS);
        }
        
        return x;
    }
}
