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
}
