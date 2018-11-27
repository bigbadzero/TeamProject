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
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TeamProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {   
        
        TASDatabase db = new TASDatabase();
        
        GregorianCalendar gc = new GregorianCalendar();
        Badge b = db.getBadge("0FFA272B");
        
        /* PART ONE */
        
        /* Get Shift Object for Pay Period Starting 09-02-2018 (should include Labor Day override) */
        
        gc.set(Calendar.DAY_OF_MONTH, 2);
        gc.set(Calendar.YEAR, 2018);
        gc.set(Calendar.MONTH, 8);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        
        Timestamp ts = new Timestamp(gc.getTimeInMillis());
        
        System.out.println(ts.getDay());
        
        Shift s = db.getShift(b, gc.getTimeInMillis());
        
        /* Retrieve Punch List #1 */
        
        
        gc.set(Calendar.DAY_OF_MONTH, 9);
        gc.set(Calendar.MONTH, 8);
        
        s = db.getShift(b, gc.getTimeInMillis());
        
        
        /* Retrieve Punch List #2 */
        
        ArrayList<Punch> p2 = db.getPayPeriodPunchList(b, gc.getTimeInMillis());
        
        /* Adjust Punches */
        
        for (Punch p : p2) {
            p.adjust(s);
        }
        
        /* Calculate Pay Period 09-09-2018 Absenteeism */
        
        Double percentage = TASLogic.calculateAbsenteeism(p2, s);
        Absenteeism a2 = new Absenteeism(b.getId(), gc.getTimeInMillis(), percentage);
        

        
        System.out.println(a2.toString());
        
        
        
        
    }
}
