package teamproject;

/**
 *
 * @author Brendan
 */

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Shift {
    
    public static final String TIME_FORMAT = "HH:mm";
    public static final int MILLIS_TO_MIN = 60000;
    
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
        this.lunchLength = 0;                       //Ask Snellen about Lunch Length
        this.lunchDeduct = lunchDeduct;
    }
    
    public String toString(){
        
        long start = this.start.getTime();
        long stop = this.stop.getTime();
        long difference = (stop - start)/MILLIS_TO_MIN;
        
        long lunchStart = this.lunchStart.getTime();
        long lunchStop = this.lunchStop.getTime();
        long lunchDiff = (lunchStop - lunchStart)/MILLIS_TO_MIN;
        
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