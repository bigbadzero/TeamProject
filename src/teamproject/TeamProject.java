/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;

/**
 *
 * @author bwats
 */
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

public class TeamProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        
        TASDatabase db = new TASDatabase();
        
        Shift s = db.getShift(1);
        
        
        Punch p = db.getPunch(150);
        
        p.adjust(s);
        
        System.out.println(p.getAdjustedTimestamp());
        
        
    }
    
}
