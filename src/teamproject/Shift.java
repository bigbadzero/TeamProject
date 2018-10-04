package teamproject;

/**
 *
 * @author Brendan
 */

import java.sql.Time;

public class Shift {
    private int id;
    private String description;
    private Time start;
    private Time stop;
    private int interval;
    private int gracePeriod;
    private int dock;
    private Time lunchStart;
    private Time lunchStop;
    private int lunchLength;
    private int lunchDeduct;
    
    
    public Shift(int id, String description, Time start, Time stop, int interval, int gracePeriod, int dock, Time lunchStart, Time lunchStop, int lunchLength, int lunchDeduct){
        this.id = id;
        this.description = description;
        this.start = start;
        this.stop = stop;
        this.interval = interval;
        this.gracePeriod = gracePeriod;
        this.dock = dock;
        this.lunchStart = lunchStart;
        this.lunchStop = lunchStop;
        this.lunchLength = lunchLength;
        this.lunchDeduct = lunchDeduct;
    }
    
    public String toString(){
        String output = "";
        
        return output;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Time getStart() {
        return start;
    }

    public Time getStop() {
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

    public Time getLunchStart() {
        return lunchStart;
    }

    public Time getLunchStop() {
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

    public void setStart(Time start) {
        this.start = start;
    }

    public void setStop(Time stop) {
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

    public void setLunchStart(Time lunchStart) {
        this.lunchStart = lunchStart;
    }

    public void setLunchStop(Time lunchStop) {
        this.lunchStop = lunchStop;
    }

    public void setLunchLength(int lunchLength) {
        this.lunchLength = lunchLength;
    }

    public void setLunchDeduct(int lunchDeduct) {
        this.lunchDeduct = lunchDeduct;
    }
    
    
}