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
        
        Badge b = db.getBadge("CEBCC740");
        
        
        Shift s2 = db.getShift(2);

        Punch p1 = db.getPunch(4943);
        Punch p2 = db.getPunch(5004);
		
        /* Adjust Punches According to Shift Rulesets */
        
        p1.adjust(s2);
        p2.adjust(s2);
        

        
        Shift s1 = db.getShift(1);
        GregorianCalendar gc = new GregorianCalendar(2018,7,1);
        ArrayList<Punch> punchList = db.getDailyPunchList(b,gc.getTimeInMillis() );
        
        int mins = TASLogic.calculateTotalMinutes(punchList, s1);
        
        System.out.println(mins);

        Shift s4 = db.getShift(4);
        
        b = db.getBadge("28DC3FB8");
        gc = new GregorianCalendar(2018,8,7);
        
        punchList = db.getDailyPunchList(b,gc.getTimeInMillis());
        
        mins = TASLogic.calculateTotalMinutes(punchList, s1);
        
        System.out.println(mins);
        
        b = db.getBadge("021890C0");
        gc = new GregorianCalendar(2018,8,12);
        
        punchList = db.getDailyPunchList(b, gc.getTimeInMillis());
        
        mins = TASLogic.calculateTotalMinutes(punchList, s1);
        
        System.out.println(mins);
        System.out.println(s4);

        
        Punch p = db.getPunch(147);
        ArrayList dailyPunches = db.getDailyPunchList(p.getBadge(), p.getOriginaltimestamp().getTime());
        System.out.println(TASLogic.getPunchListAsJSON(dailyPunches));
    }
}
