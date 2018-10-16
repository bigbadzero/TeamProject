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
        
        /*
        *Calculates the total minutes an employee work in a day
        *Does the bulk of the calcualtion in terms of Milliseconds
        *Before converting the total of milliseconds to minutes
        */
        
        int totalMinutes = 0;
        Long totalMillis = new Long(0);
        Punch previous = null;
        boolean lunchOut = false;
        boolean lunchIn = false;
        
        //convert instance fields of shift into milliseconds
        long lunchDeduct = shift.getLunchDeduct() * Shift.MILLIS_TO_MIN;
        long lunchLength = shift.getLunchLength() * Shift.MILLIS_TO_MIN;
        
        for(Punch p: punchList){
            p.adjust(shift);
            
            if(p.getPunchtypeid() == Punch.CLOCKED_IN)
                previous = p;
            else if(p.getPunchtypeid() == Punch.CLOCKED_OUT)
                totalMillis += p.getAdjustedtimestamp().getTime() - previous.getAdjustedtimestamp().getTime();
            
            if(p.getEventdata().equals(Punch.EVENT_DATA_LUNCH_START))
                lunchOut = true;
            else if(p.getEventdata().equals(Punch.EVENT_DATA_LUNCH_STOP))
                lunchIn = true;
        }
        if(!lunchOut && !lunchIn){
            if(totalMillis >= lunchDeduct)
                totalMillis -= lunchLength;
        }
    
        totalMinutes = (new Long(totalMillis/Shift.MILLIS_TO_MIN)).intValue();
        
        return totalMinutes;
    }
}
