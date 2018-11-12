package teamproject;

/**
 *
 * @author Brendan
 */

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.lang.Number;
import java.util.HashMap;

public class Shift {
    
    public static final String TIME_FORMAT = "HH:mm";
    public static final String SHIFT_START = "Shift Start";
    public static final String LUNCH_START = "Lunch Start";
    public static final String LUNCH_STOP = "Lunch Stop";
    public static final String SHIFT_STOP = "Shift Stop";
    
    private int id;
    private String description;
    private DailySchedule defaultschedule;
    
    
    
    public Shift(int id, String description, DailySchedule defaultschedule){
        this.id = id;
        this.description = description;
        this.defaultschedule = defaultschedule;
       
    }
    
    public String toString(){
        
        return defaultschedule.toString(description);
    }
    
    public HashMap<String,Timestamp> getParticularShiftValues(Timestamp ts){
        HashMap<String,Timestamp> shiftValues = defaultschedule.getParticularShiftValues(ts);
        
        return shiftValues;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Timestamp getStart() {
        return defaultschedule.getStart();
    }

    public Timestamp getStop() {
        return defaultschedule.getStop();
    }

    public int getInterval() {
        return defaultschedule.getInterval();
    }

    public int getGracePeriod() {
        return defaultschedule.getGracePeriod();
    }

    public int getDock() {
        return defaultschedule.getDock();
    }

    public Timestamp getLunchStart() {
        return defaultschedule.getLunchStart();
    }

    public Timestamp getLunchStop() {
        return defaultschedule.getLunchStop();
    }

    public int getLunchLength() {
        return defaultschedule.getLunchLength();
    }

    public int getLunchDeduct() {
        return defaultschedule.getLunchDeduct();
    }
    public int getShiftLength(){
        return defaultschedule.getShiftLength();
    }
    
    public void setStart(Timestamp start) {
        defaultschedule.setStart(start);
    }

    public void setStop(Timestamp stop) {
        defaultschedule.setStop(stop);
    }

    public void setInterval(int interval) {
        defaultschedule.setInterval(interval);
    }

    public void setGracePeriod(int gracePeriod) {
        defaultschedule.setGracePeriod(gracePeriod);
    }

    public void setDock(int dock) {
        defaultschedule.setDock(dock);
    }

    public void setLunchStart(Timestamp lunchStart) {
        defaultschedule.setLunchStart(lunchStart);
    }

    public void setLunchStop(Timestamp lunchStop) {
        defaultschedule.setLunchStop(lunchStop);
    }

    public void setLunchLength(int lunchLength) {
        defaultschedule.setLunchLength(lunchLength);
    }

    public void setLunchDeduct(int lunchDeduct) {
        defaultschedule.setLunchDeduct(lunchDeduct);
    }

}