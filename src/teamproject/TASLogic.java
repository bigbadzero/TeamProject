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
        int totalMinutes = 0;
        Long totalMillis = new Long(0);
        
        int lunchDeduct = shift.getLunchDeduct();
        int lunchLength = shift.getLunchLength();
        
        Punch punchIn = null;
        Punch punchOut = null;
        Punch lunchOut = null;
        Punch lunchIn = null;
        
        for(Punch p: punchList)
            p.adjust(shift);
        
        if(punchList.size() == 2){
            punchIn = punchList.get(0);
            punchOut = punchList.get(1);
        }
        else if(punchList.size() == 4){
            punchIn = punchList.get(0);
            lunchOut = punchList.get(1);
            lunchIn = punchList.get(2);
            punchOut = punchList.get(3);
        }
        
        
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
                if((totalMillis/Shift.MILLIS_TO_MIN) >= lunchDeduct){
                    totalMillis = totalMillis - (lunchLength * Shift.MILLIS_TO_MIN);
                }
            }
            
        }
    
        totalMinutes = (new Long(totalMillis/Shift.MILLIS_TO_MIN)).intValue();
        
        
        return totalMinutes;
    }
}
