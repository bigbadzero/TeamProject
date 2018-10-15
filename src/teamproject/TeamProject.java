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
        
        TASDatabase db = new TASDatabase();
        
        Badge b = db.getBadge("4E6E296E");
        
        Shift s = db.getShift(1);
        
        Timestamp clockIn = new Timestamp(118,9,15,7,3,33,0);
        Punch punchIn = new Punch(b,1,407,clockIn,Punch.CLOCKED_IN);
        
        Timestamp clockOut = new Timestamp(118,9,15,17,31,33,0);
        Punch punchOut = new Punch(b,1,407,clockOut,Punch.CLOCKED_OUT);
        
        ArrayList<Punch> punchList = new ArrayList();
        
        punchList.add(punchIn);
        punchList.add(punchOut);
        
        int minutes = TASLogic.calculateTotalMinutes(punchList, s);
        
        System.out.println(minutes/Shift.MILLIS_TO_MIN);
        
        
    }
}
