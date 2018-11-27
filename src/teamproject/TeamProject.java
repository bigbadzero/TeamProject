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
        Badge b = db.getBadge("3282F212");
        
        /* PART ONE */
        
        /* Get Shift Object for Pay Period Starting 09-09-2018 (regular Shift 1 schedule) */
        
        gc.set(Calendar.DAY_OF_MONTH, 9);
        gc.set(Calendar.YEAR, 2018);
        gc.set(Calendar.MONTH, 8);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        
        Shift s = db.getShift(b, gc.getTimeInMillis());
        
        //System.out.println("Shift 1");
        //s.printWeeklySchedule();
        /* Retrieve Punch List #1 */
        
        ArrayList<Punch> p1 = db.getPayPeriodPunchList(b, gc.getTimeInMillis());
        
        /* Adjust Punches */
        
        for (Punch p : p1) {
            p.adjust(s);
        }
        
        /* Calculate Pay Period 09-09-2018 Absenteeism */
        
        double percentage = TASLogic.calculateAbsenteeism(p1, s);
        Absenteeism a1 = new Absenteeism(b.getId(), gc.getTimeInMillis(), percentage);
        
        
        /* PART TWO */
        
        /* Get Shift Object for Pay Period Starting 09-16-2018 (should include "Leave Early on Friday" override) */
        
        gc.set(Calendar.DAY_OF_MONTH, 16);
        gc.set(Calendar.MONTH, 8);
        
        s = db.getShift(b, gc.getTimeInMillis());
        
        
        /* Retrieve Punch List #2 */
        
        ArrayList<Punch> p2 = db.getPayPeriodPunchList(b, gc.getTimeInMillis());
        
        /* Adjust Punches */
        
        for (Punch p : p2) {
            p.adjust(s);
        }
        
        /* Calculate Pay Period 09-16-2018 Absenteeism */
        
        percentage = TASLogic.calculateAbsenteeism(p2, s);
        Absenteeism a2 = new Absenteeism(b.getId(), gc.getTimeInMillis(), percentage);
        
        
        /* PART THREE */
        
        /* Get Shift Object for Pay Period Starting 09-23-2018 (should include "Leave Early on Friday" override) */
        
        gc.set(Calendar.DAY_OF_MONTH, 23);
        gc.set(Calendar.MONTH, 8);
        
        s = db.getShift(b, gc.getTimeInMillis());
        
        //System.out.println("Shift 3");
       // s.printWeeklySchedule();
        /* Retrieve Punch List #3 */
        
        ArrayList<Punch> p3 = db.getPayPeriodPunchList(b, gc.getTimeInMillis());
        
        /* Adjust Punches */
        
        for (Punch p : p3) {
            p.adjust(s);
        }
        
        /* Calculate Pay Period 09-23-2018 Absenteeism */
        
        percentage = TASLogic.calculateAbsenteeism(p3, s);
        Absenteeism a3 = new Absenteeism(b.getId(), gc.getTimeInMillis(), percentage);
        
        
        System.out.println(a1.toString());
        
        
        System.out.println(a2.toString());
        
        
        System.out.println(a3.toString());
        
    }
}
