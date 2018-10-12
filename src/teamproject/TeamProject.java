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
        
        Badge b = db.getBadge("021890C0");
        
        
        Timestamp clock = new Timestamp(118,7,7,7,15,1,0);
        
        Punch p1 = new Punch(b, 101, 1,clock,Punch.CLOCKED_OUT);
        
        Shift s = db.getShift(4);
        
        p1.adjust(s);
        
        System.out.println(p1.printOriginalTimestamp());
        System.out.println(p1.printAdjustedTimestamp());
        
        
    }
    
}
