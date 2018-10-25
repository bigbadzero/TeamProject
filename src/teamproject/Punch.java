package teamproject;

/**
 *
 * @author Brendan
 */
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.sql.Time;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Punch {
    
    
    public final static int CLOCKED_OUT = 0;
    public final static int CLOCKED_IN = 1;
    public final static int TIMED_OUT = 2;
    
    public final static String EVENT_DATA_SHIFT_START = "Shift Start";
    public final static String EVENT_DATA_SHIFT_STOP = "Shift Stop";
    public final static String EVENT_DATA_LUNCH_START = "Lunch Start";
    public final static String EVENT_DATA_LUNCH_STOP = "Lunch Stop";
    public final static String EVENT_DATA_SHIFT_DOCK = "Shift Dock";
    public final static String EVENT_DATA_INTERVAL_ROUND = "Interval Round";
    public final static String EVENT_DATA_NONE = "None";
    
    public final static int SUN = 0;
    public final static int SAT = 6;
    
    public static final String[] PUNCH_TYPES = { "CLOCKED OUT","CLOCKED IN", "TIMED OUT"}; 
    public static final String[] DAYS_OF_WEEK = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
    
    private int id;
    private int terminalId;
    private Badge badge;
    private Timestamp originalTimestamp;
    private Timestamp adjustedTimestamp;
    private int punchTypeId;
    private String eventData;
    
    public Punch(Badge badge, int terminalId, int punchTypeId ){
        this.badge = badge;
        this.terminalId = terminalId;
        this.punchTypeId = punchTypeId;
        
        this.id = 0;
        GregorianCalendar gc = new GregorianCalendar();
        this.originalTimestamp = new Timestamp(gc.getTimeInMillis());
        this.adjustedTimestamp = null;
        this.eventData = "";
        
    }
    
    public Punch(Badge badge,int id, int terminalId, Timestamp ots, int ptid ){
        this.badge = badge;
        this.id = id;
        this.terminalId = terminalId;
        
        this.originalTimestamp = ots;
        this.adjustedTimestamp = null;
        this.punchTypeId = ptid;
        this.eventData = "";
    }
    
    public void adjust(Shift s){
        
        Timestamp ots = originalTimestamp;
        int day = originalTimestamp.getDay();
  
        HashMap<String,Timestamp> shiftValues = s.getParticularShiftValues(ots);
        Timestamp shiftStart = shiftValues.get(Shift.SHIFT_START);
        Timestamp lunchStart = shiftValues.get(Shift.LUNCH_START);
        Timestamp lunchStop = shiftValues.get(Shift.LUNCH_STOP);
        Timestamp shiftStop = shiftValues.get(Shift.SHIFT_STOP);
        
        if(day != shiftStart.getDay())
            day = shiftStart.getDay();
        
        int interval = s.getInterval() * TASLogic.MILLIS_TO_MIN;
        int gracePeriod = s.getGracePeriod() * TASLogic.MILLIS_TO_MIN;
        int dock = s.getDock() * TASLogic.MILLIS_TO_MIN;  
        
        Timestamp shiftStartEarly = new Timestamp(shiftStart.getTime() - interval);
        Timestamp shiftStartLate = new Timestamp(shiftStart.getTime() + interval);
        Timestamp shiftStartGracePeriod = new Timestamp(shiftStart.getTime() + gracePeriod);
        
        Timestamp shiftStopEarly = new Timestamp(shiftStop.getTime() - interval);
        Timestamp shiftStopLate = new Timestamp(shiftStop.getTime() + interval);
        Timestamp shiftStopGracePeriod = new Timestamp(shiftStop.getTime() - gracePeriod);

        if((day == this.SUN) || (day == this.SAT)){
            
            /*
            Punch Occurs on a Weekend and Does Not Pertain to a Rule Set.
            Punch Will be Adjusted to the Closest Interval
            */
            
            Timestamp nearestBefore = new Timestamp(ots.getYear(),ots.getMonth(),ots.getDate(),ots.getHours(),0,0,0);
            Timestamp nearestAfter = new Timestamp(nearestBefore.getTime() + interval);
            
            Timestamp otsNoSecs = new Timestamp(ots.getYear(),ots.getMonth(),ots.getDate(),ots.getHours(),ots.getMinutes(),0,0);
            
            while(nearestAfter.before(ots)){
                
                nearestBefore.setTime(nearestBefore.getTime() + interval);
                nearestAfter.setTime(nearestAfter.getTime() + interval);   
            }
            
            long beforeDiff = ots.getTime() - nearestBefore.getTime();
            long afterDiff = nearestAfter.getTime() - ots.getTime();
            
            if(otsNoSecs.equals(nearestBefore) || ots.equals(nearestAfter)){
                    adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                    eventData = EVENT_DATA_NONE;
            }
            else if(beforeDiff < afterDiff){
                adjustedTimestamp = new Timestamp(nearestBefore.getTime());
                eventData = EVENT_DATA_INTERVAL_ROUND;
            }
            else{
                adjustedTimestamp = new Timestamp(nearestAfter.getTime());
                eventData = EVENT_DATA_INTERVAL_ROUND;
            }  
        }
        else{
            
            /*
            Punch Occurs on a Weekday and Either Falls Within a Rule Set or
            Will be Rounded to the Nearest Interval
            */
            
            if((ots.after(shiftStartEarly) && ots.before(shiftStartLate)) || (ots.equals(shiftStartEarly)) || (ots.equals(shiftStartLate))){
                
                //Punch Occurs Within the Shift Start Period
                
                if(ots.before(shiftStart)){     //Punch is in the Early Interval
                    adjustedTimestamp = new Timestamp(shiftStart.getTime());
                    eventData = EVENT_DATA_SHIFT_START;
                }
                else{   //Punch is in the Late Interval
                    if(ots.before(shiftStartGracePeriod) || ots.equals(shiftStartGracePeriod)){     //Punch is Within the Grace Period
                        adjustedTimestamp = new Timestamp(shiftStart.getTime());
                        eventData = EVENT_DATA_SHIFT_START;
                    }
                    else{   //Punch is Late and Will be Docked
                        adjustedTimestamp = new Timestamp(shiftStart.getTime() + dock);                   
                        eventData = EVENT_DATA_SHIFT_DOCK;
                    }
                }
            }
            else if((ots.after(shiftStopEarly)  && ots.before(shiftStopLate)) || (ots.equals(shiftStopEarly)) || (ots.equals(shiftStopLate))){
                
                //Punch Occurs Within the Shift Stop Period
                
                if(ots.after(shiftStop)){   //Punch is in the Late Interval
                    adjustedTimestamp = new Timestamp(shiftStop.getTime());
                    eventData = EVENT_DATA_SHIFT_STOP;
                }
                else{   //Punch is in the Early Interval
                    if((ots.after(shiftStopGracePeriod)) || ots.equals(shiftStopGracePeriod)){  //Punch is Within the Grace Period
                        adjustedTimestamp = new Timestamp(shiftStop.getTime());
                        eventData = EVENT_DATA_SHIFT_STOP;
                    }
                    else{   //Punch-Out is Early and is Docked
                        adjustedTimestamp = new Timestamp(shiftStop.getTime() - dock); 
                        eventData = EVENT_DATA_SHIFT_DOCK;
                    }
                }
            }
            else if((ots.after(lunchStart) && ots.before(lunchStop)) || (ots.equals(lunchStart)) || (ots.equals(lunchStop))){
                
                //Punch Occurs Within the Lunch Period
                
                if(ots.equals(lunchStart)){     
                    adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                    eventData = EVENT_DATA_LUNCH_START;
                }
                else if(ots.equals(lunchStop)){
                    adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                    eventData = EVENT_DATA_LUNCH_STOP;
                }
                else{    
                    if(punchTypeId == CLOCKED_OUT){     //Punch is a Clock-Out and is Pushed Back to Lunch Start
                        adjustedTimestamp = new Timestamp(lunchStart.getTime());
                        eventData = EVENT_DATA_LUNCH_START;
                    }
                    else{   //Punch is a Clock-In and is Pushed Back to Lunch Stop
                        adjustedTimestamp = new Timestamp(lunchStop.getTime());
                        eventData = EVENT_DATA_LUNCH_STOP;
                    }
                }
                
            }
            else{
                
                //Punch Does Not Occur Within a Designated Rule Set
                //and is Set to the Nearest Interval
                
                Timestamp nearestBefore = new Timestamp(ots.getYear(),ots.getMonth(),ots.getDate(),ots.getHours(),0,0,0);
                Timestamp nearestAfter = new Timestamp(nearestBefore.getTime() + interval);
                
                Timestamp otsNoSecs = new Timestamp(ots.getYear(),ots.getMonth(),ots.getDate(),ots.getHours(),ots.getMinutes(),0,0);

                while(nearestAfter.before(ots)){
                    nearestBefore.setTime(nearestBefore.getTime() + interval);
                    nearestAfter.setTime(nearestAfter.getTime() + interval);
                }

                long beforeDiff = ots.getTime() - nearestBefore.getTime();
                long afterDiff = nearestAfter.getTime() - ots.getTime();
               
                if(otsNoSecs.equals(nearestBefore) || ots.equals(nearestAfter)){
                    adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                    eventData = EVENT_DATA_NONE;
                }
                
               else if(beforeDiff < afterDiff){
                    adjustedTimestamp = new Timestamp(nearestBefore.getTime());
                    eventData = EVENT_DATA_INTERVAL_ROUND;
                }
                else{
                    adjustedTimestamp = new Timestamp(nearestAfter.getTime());
                    eventData = EVENT_DATA_INTERVAL_ROUND;
                }
            } 
        }
    }
      
    public String printOriginalTimestamp(){
        
        String pt = this.PUNCH_TYPES[punchTypeId];
        String dow = this.DAYS_OF_WEEK[originalTimestamp.getDay()];
        String date = (new SimpleDateFormat(DATE_FORMAT)).format(originalTimestamp);
        
        String output = "#" + badge.getId() + " ";
        output+= pt + ": " + dow + " " + date;
        
        
        return output;
    }
    public String printAdjustedTimestamp(){
        String pt = this.PUNCH_TYPES[punchTypeId];
        String dow = this.DAYS_OF_WEEK[adjustedTimestamp.getDay()];
        String date = (new SimpleDateFormat(DATE_FORMAT)).format(adjustedTimestamp);
        
        String output = "#" + badge.getId() + " ";
        output+= pt + ": " + dow + " " + date + " ";
        output+= "(" + this.eventData + ")";
        
        
        return output;
    }
    
    public String getOriginalformatteddate(){
        String date = (new SimpleDateFormat(DATE_FORMAT)).format(originalTimestamp);
        
        return date;
    }

    public int getId() {
        return id;
    }

    public int getTerminalid() {
        return terminalId;
    }

    public Badge getBadge() {
        return badge;
    }
    public String getBadgeid(){
        return this.badge.getId();
    }
    public String getBadgeDesc(){
        return this.badge.getDescription();
    }

    public Timestamp getOriginaltimestamp() {
        return originalTimestamp;
    }

    public Timestamp getAdjustedtimestamp() {
        return adjustedTimestamp;
    }

    public int getPunchtypeid() {
        return punchTypeId;
    }
    
    public String getEventdata(){
        return this.eventData;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTerminalid(int terminalId) {
        this.terminalId = terminalId;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public void setOriginaltimestamp(Timestamp originalTimestamp) {
        this.originalTimestamp = originalTimestamp;
    }

    public void setAdjustedtimestamp(Timestamp adjustedTimeStamp) {
        this.adjustedTimestamp = adjustedTimeStamp;
    }

    public void setPunchtypeid(int punchTypeId) {
        this.punchTypeId = punchTypeId;
    }
    
    
}