package teamproject;

/**
 *
 * @author Brendan
 */

import java.sql.Time;
import java.sql.Timestamp;

public class Shift {
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
        String output = "";
        
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