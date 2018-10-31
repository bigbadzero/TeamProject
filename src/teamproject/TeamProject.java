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
        
         /* Get Punch */
        
        Punch p = db.getPunch(3634);
        Badge b = db.getBadge(p.getBadgeid());
        Shift s = db.getShift(b);
        
        /* Get Pay Period Punch List */
        
        System.out.println(p);
        System.out.println(b);
        System.out.println(s);
        
        long ts = p.getOriginaltimestamp().getTime();
        System.out.println(ts);
        ArrayList<Punch> punchlist = db.getPayPeriodPunchList(b, ts);

        /* Adjust Punches */
        
        for (Punch punch : punchlist) {
            punch.adjust(s);
        }
        
        /* Compute Pay Period Total Absenteeism */
        
        double percentage = TASLogic.calculateAbsenteeism(punchlist, s);
        
        System.out.println(percentage);
        
        /* Insert Absenteeism Into Database */
        
        Absenteeism a1 = new Absenteeism(b.getId(), ts, percentage);
        
        System.out.println(a1.getPayPeriod().getTimeInMillis());
        
        //db.insertAbsenteeism(a1);
    }
}
