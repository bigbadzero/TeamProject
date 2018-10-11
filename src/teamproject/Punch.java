package teamproject;

/**
 *
 * @author Brendan
 */
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.sql.Time;
import java.util.GregorianCalendar;

public class Punch {
    
    
    public final static int CLOCKED_OUT = 0;
    public final static int CLOCKED_IN = 1;
    public final static int TIMED_OUT = 2;
    
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
    }
    
    public Punch(Badge badge,int id, int terminalId, Timestamp ots, int ptid ){
        this.badge = badge;
        this.id = id;
        this.terminalId = terminalId;
        
        this.originalTimestamp = ots;
        this.punchTypeId = ptid;
    }
    
    public void adjust(Shift s){
        
        Time ots = new Time(originalTimestamp.getHours(),originalTimestamp.getMinutes(),originalTimestamp.getSeconds());
        int day = originalTimestamp.getDay();

        Time shiftStart = new Time(s.getStart().getHours(),s.getStart().getMinutes(),s.getStart().getSeconds());
        Time shiftStop = new Time(s.getStop().getHours(),s.getStop().getMinutes(),s.getStart().getSeconds());
        Time lunchStart = new Time(s.getLunchStart().getHours(),s.getLunchStart().getMinutes(),s.getLunchStart().getSeconds());;
        Time lunchStop = new Time(s.getLunchStop().getHours(),s.getLunchStop().getMinutes(),s.getLunchStop().getSeconds());
        int interval = s.getInterval() * Shift.MILLIS_TO_MIN;
        int gracePeriod = s.getGracePeriod() * Shift.MILLIS_TO_MIN;
        int dock = s.getDock() * Shift.MILLIS_TO_MIN;
        String eventData;
        
        Time shiftStartEarly = new Time(shiftStart.getTime() - interval);
        Time shiftStartLate = new Time(shiftStart.getTime() + interval);
        Time shiftStartGracePeriod = new Time(shiftStart.getTime() + gracePeriod);
        
        Time shiftStopEarly = new Time(shiftStop.getTime() - interval);
        Time shiftStopLate = new Time(shiftStop.getTime() + interval);
        Time shiftStopGracePeriod = new Time(shiftStop.getTime() - gracePeriod);

        if((day == this.SUN) || (day == this.SAT)){
            
            /*
            Punch Occurs on a Weekend and Does Not Pertain to a Rule Set.
            Punch Will be Adjusted to the Closest Interval
            */
            
            Time nearestBefore = new Time(ots.getHours(),0,0);
            Time nearestAfter = new Time(nearestBefore.getTime() + interval);
            
            while(nearestAfter.before(ots)){
                nearestBefore.setMinutes(nearestBefore.getMinutes() + interval);
                nearestAfter.setMinutes(nearestAfter.getMinutes() + interval);
            }
            
            long beforeDiff = ots.getTime() - nearestBefore.getTime();
            long afterDiff = nearestAfter.getTime() - ots.getTime();
            
            if(beforeDiff < afterDiff){
                adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                adjustedTimestamp.setHours(nearestBefore.getHours());
                adjustedTimestamp.setMinutes(nearestBefore.getMinutes());
                adjustedTimestamp.setSeconds(nearestBefore.getSeconds());
                
                eventData = "None";
            }
            else{
                adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                adjustedTimestamp.setHours(nearestAfter.getHours());
                adjustedTimestamp.setMinutes(nearestAfter.getMinutes());
                adjustedTimestamp.setSeconds(nearestAfter.getSeconds());
                
                eventData = "None";
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
                    adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                    adjustedTimestamp.setHours(shiftStart.getHours());
                    adjustedTimestamp.setMinutes(shiftStart.getMinutes());
                    adjustedTimestamp.setSeconds(shiftStart.getSeconds());
                    
                    eventData = "Interval Round";
                }
                else{   //Punch is in the Late Interval
                    if(ots.before(shiftStartGracePeriod) || ots.equals(shiftStartGracePeriod)){     //Punch is Within the Grace Period
                        adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                        adjustedTimestamp.setHours(shiftStart.getHours());
                        adjustedTimestamp.setMinutes(shiftStart.getMinutes());
                        adjustedTimestamp.setSeconds(shiftStart.getSeconds());
                    
                        eventData = "Shift Start";
                    }
                    else{   //Punch is Late and Will be Docked
                        adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                        adjustedTimestamp.setHours(shiftStartLate.getHours());
                        adjustedTimestamp.setMinutes(shiftStartLate.getMinutes());
                        adjustedTimestamp.setSeconds(shiftStartLate.getSeconds());
                    
                        eventData = "Dock";
                    }
                }
            }
            else if((ots.after(shiftStopEarly)  && ots.before(shiftStopLate)) || (ots.equals(shiftStopEarly)) || (ots.equals(shiftStopLate))){
                
                //Punch Occurs Within the Shift Stop Period
                
                if(ots.after(shiftStop)){   //Punch is in the Late Interval
                    adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                    adjustedTimestamp.setHours(shiftStop.getHours());
                    adjustedTimestamp.setMinutes(shiftStop.getMinutes());
                    adjustedTimestamp.setSeconds(shiftStop.getSeconds());
                    
                    eventData = "Interval Round";
                }
                else{   //Punch is in the Early Interval
                    if((ots.after(shiftStopGracePeriod)) || ots.equals(shiftStopGracePeriod)){  //Punch is Within the Grace Period
                        adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                        adjustedTimestamp.setHours(shiftStop.getHours());
                        adjustedTimestamp.setMinutes(shiftStop.getMinutes());
                        adjustedTimestamp.setSeconds(shiftStop.getSeconds());
                    
                        eventData = "Shift Stop";
                    }
                    else{   //Punch-Out is Early and is Docked
                        adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                        adjustedTimestamp.setHours(shiftStopEarly.getHours());
                        adjustedTimestamp.setMinutes(shiftStopEarly.getMinutes());
                        adjustedTimestamp.setSeconds(shiftStopEarly.getSeconds());
                    
                        eventData = "Shift Dock";
                    }
                }
            }
            else if((ots.after(lunchStart) && ots.before(lunchStop)) || (ots.equals(lunchStart)) || (ots.equals(lunchStop))){
                
                //Punch Occurs Within the Lunch Period
                
                if(ots.equals(lunchStart)){     
                    adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                    eventData = "Lunch Start";
                }
                else if(ots.equals(lunchStop)){
                    adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                    eventData = "Lunch Stop";
                }
                else{    
                    if(punchTypeId == CLOCKED_OUT){     //Punch is a Clock-Out and is Pushed Back to Lunch Start
                        adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                        adjustedTimestamp.setHours(lunchStart.getHours());
                        adjustedTimestamp.setMinutes(lunchStart.getMinutes());
                        adjustedTimestamp.setSeconds(lunchStart.getSeconds());
                    
                        eventData = "Lunch Start";
                    }
                    else{   //Punch is a Clock-In and is Pushed Back to Lunch Stop
                        adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                        adjustedTimestamp.setHours(lunchStop.getHours());
                        adjustedTimestamp.setMinutes(lunchStop.getMinutes());
                        adjustedTimestamp.setSeconds(lunchStop.getSeconds());
                    
                        eventData = "Lunch Stop";
                    }
                }
                
            }
            else{
                
                //Punch Does Not Occur Within a Designated Rule Set
                //and is Set to the Nearest Interval
                
                Time nearestBefore = new Time(ots.getHours(),0,0);
                Time nearestAfter = new Time(nearestBefore.getTime() + interval);

                while(nearestAfter.before(ots)){
                    nearestBefore.setMinutes(nearestBefore.getMinutes() + interval);
                    nearestAfter.setMinutes(nearestAfter.getMinutes() + interval);
                }

                long beforeDiff = ots.getTime() - nearestBefore.getTime();
                long afterDiff = nearestAfter.getTime() - ots.getTime();

                if(beforeDiff < afterDiff){
                    adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                    adjustedTimestamp.setHours(nearestBefore.getHours());
                    adjustedTimestamp.setMinutes(nearestBefore.getMinutes());
                    adjustedTimestamp.setSeconds(nearestBefore.getSeconds());

                    eventData = "None";
                }
                else{
                    adjustedTimestamp = new Timestamp(originalTimestamp.getTime());
                    adjustedTimestamp.setHours(nearestAfter.getHours());
                    adjustedTimestamp.setMinutes(nearestAfter.getMinutes());
                    adjustedTimestamp.setSeconds(nearestAfter.getSeconds());

                    eventData = "None";
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