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
    private Timestamp start;
    private Timestamp stop;
    private int interval;
    private int gracePeriod;
    private int dock;
    private Timestamp lunchStart;
    private Timestamp lunchStop;
    private int lunchLength;
    private int lunchDeduct;
    private int shiftLength;
    
    
    public Shift(int id, String description, Timestamp start, Timestamp stop, int interval, int gracePeriod, int dock, Timestamp lunchStart, Timestamp lunchStop,  int lunchDeduct){
        this.id = id;
        this.description = description;
        this.start = start;
        this.stop = stop;
        this.interval = interval;
        this.gracePeriod = gracePeriod;
        this.dock = dock;
        this.lunchStart = lunchStart;
        this.lunchStop = lunchStop;
        this.lunchDeduct = lunchDeduct;
        
        Long lunchLength = (lunchStop.getTime() - lunchStart.getTime())/TASLogic.MILLIS_TO_MIN;
        this.lunchLength = lunchLength.intValue();
        
        Timestamp shiftStop = TASLogic.forceXafterY(stop, start);
        Long shiftLength = (shiftStop.getTime() - start.getTime())/TASLogic.MILLIS_TO_MIN;
        shiftLength -= lunchLength;
        this.shiftLength = shiftLength.intValue();
    }
    
    public String toString(){
        
        Timestamp stop = TASLogic.forceXafterY(this.stop, start);
        Timestamp lunchStart = TASLogic.forceXafterY(this.lunchStart, start);
        Timestamp lunchStop = TASLogic.forceXafterY(this.lunchStop, start);
        
        long startMillis = this.start.getTime();
        long stopMillis = stop.getTime();
        long difference = (stopMillis - startMillis)/TASLogic.MILLIS_TO_MIN;
        
        long lunchStartMillis = lunchStart.getTime();
        long lunchStopMillis = lunchStop.getTime();
        long lunchDiff = (lunchStopMillis - lunchStartMillis)/TASLogic.MILLIS_TO_MIN;
        
        String startHour = (new SimpleDateFormat(TIME_FORMAT)).format(start);
        String stopHour = (new SimpleDateFormat(TIME_FORMAT)).format(stop);
        String lunchStartHour = (new SimpleDateFormat(TIME_FORMAT)).format(lunchStart);
        String lunchStopHour = (new SimpleDateFormat(TIME_FORMAT)).format(lunchStop);
        
        String output = description + ": " ;
        output += startHour + " - " + stopHour;
        output += " (" + difference + " minutes); ";
        output += "Lunch: " + lunchStartHour + " - " + lunchStopHour + " (" + lunchDiff + " minutes)";
        
        return output;
    }
    
    public HashMap<String,Timestamp> getParticularShiftValues(Timestamp ts){
        HashMap<String,Timestamp> shiftValues = new HashMap();
        
        long shiftLength = this.stop.getTime() - this.start.getTime();
        int lunchLength = this.lunchLength;
        long lunchStarts = this.lunchStart.getTime() - this.start.getTime();
        
        long startDiff = 0;
        long stopDiff = 0;
        
        int year = ts.getYear();
        int month = ts.getMonth();
        int date = ts.getDate();
        
        int startHour = this.start.getHours();
        int startMin = this.start.getMinutes();
        Timestamp shiftStart = new Timestamp(year,month,date,startHour,startMin,0,0);
        //Timestamp shiftStartPrevDay = new Timestamp(shiftStartCurrentDay.getTime() - 24*TASLogic.MILLIS_TO_HOURS);
        
        int lunchStartHour = this.lunchStart.getHours();
        int lunchStartMin = this.lunchStart.getMinutes();
        Timestamp lunchStart = new Timestamp(year,month,date,lunchStartHour,lunchStartMin,0,0);
        Timestamp lunchStop = new Timestamp(lunchStart.getTime() + lunchLength*TASLogic.MILLIS_TO_MIN);
        
        int stopHour = this.stop.getHours();
        int stopMin = this.stop.getMinutes();
        Timestamp shiftStop = new Timestamp(year,month,date,stopHour,stopMin,0,0);
        //Timestamp shiftStopNextDay = new Timestamp(shiftStopCurrentDay.getTime() + 24*TASLogic.MILLIS_TO_HOURS);
        
        if(shiftStop.after(shiftStart)){
            shiftValues.put(SHIFT_START, shiftStart);
            shiftValues.put(LUNCH_START, lunchStart);
            shiftValues.put(LUNCH_STOP, lunchStop);
            shiftValues.put(SHIFT_STOP, shiftStop);
        }
        else{
            if(ts.after(shiftStart) || ts.equals(shiftStart))
                startDiff = ts.getTime() - shiftStart.getTime();
            else
                startDiff = shiftStart.getTime() - ts.getTime();

            if(ts.after(shiftStop) || ts.equals(shiftStop))
                stopDiff = ts.getTime() - shiftStop.getTime();
            else
                stopDiff = shiftStop.getTime() - ts.getTime();

            if(startDiff < stopDiff)
                shiftStop = new Timestamp(shiftStop.getTime() + 24*TASLogic.MILLIS_TO_HOURS);
            else
                shiftStart = new Timestamp(shiftStart.getTime() - 24*TASLogic.MILLIS_TO_HOURS);
            
            lunchStart = TASLogic.forceXafterY(lunchStart, shiftStart);
            lunchStop = TASLogic.forceXafterY(lunchStop, lunchStart);
            
            
            shiftValues.put(SHIFT_START, shiftStart);
            shiftValues.put(LUNCH_START, lunchStart);
            shiftValues.put(LUNCH_STOP, lunchStop);
            shiftValues.put(SHIFT_STOP, shiftStop);
        }
        
        
        return shiftValues;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getStart() {
        return start;
    }

    public Timestamp getStop() {
        return stop;
    }

    public int getInterval() {
        return interval;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public int getDock() {
        return dock;
    }

    public Timestamp getLunchStart() {
        return lunchStart;
    }

    public Timestamp getLunchStop() {
        return lunchStop;
    }

    public int getLunchLength() {
        return lunchLength;
    }

    public int getLunchDeduct() {
        return lunchDeduct;
    }
    public int getShiftLength(){
        return shiftLength;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public void setStop(Timestamp stop) {
        this.stop = stop;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setGracePeriod(int gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public void setDock(int dock) {
        this.dock = dock;
    }

    public void setLunchStart(Timestamp lunchStart) {
        this.lunchStart = lunchStart;
    }

    public void setLunchStop(Timestamp lunchStop) {
        this.lunchStop = lunchStop;
    }

    public void setLunchLength(int lunchLength) {
        this.lunchLength = lunchLength;
    }

    public void setLunchDeduct(int lunchDeduct) {
        this.lunchDeduct = lunchDeduct;
    }
    
    
}