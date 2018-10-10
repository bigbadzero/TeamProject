package teamproject;

/**
 *
 * @author Brendan
 */
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.sql.Time;

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
        this.originalTimestamp = null;
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
            
        }
        else{
            if((ots.after(shiftStartEarly) && ots.before(shiftStartLate)) || (ots.equals(shiftStartEarly)) || (ots.equals(shiftStartLate))){
                if(ots.before(shiftStart)){
                    adjustedTimestamp = originalTimestamp;
                    adjustedTimestamp.setHours(shiftStart.getHours());
                    adjustedTimestamp.setMinutes(shiftStart.getMinutes());
                    adjustedTimestamp.setSeconds(shiftStart.getSeconds());
                    
                    eventData = "Shift Start";
                }
                else{
                    if(ots.before(shiftStartGracePeriod) || ots.equals(shiftStartGracePeriod)){
                        adjustedTimestamp = originalTimestamp;
                        adjustedTimestamp.setHours(shiftStart.getHours());
                        adjustedTimestamp.setMinutes(shiftStart.getMinutes());
                        adjustedTimestamp.setSeconds(shiftStart.getSeconds());
                    
                        eventData = "Shift Start";
                    }
                    else{
                        adjustedTimestamp = originalTimestamp;
                        adjustedTimestamp.setHours(shiftStartLate.getHours());
                        adjustedTimestamp.setMinutes(shiftStartLate.getMinutes());
                        adjustedTimestamp.setSeconds(shiftStartLate.getSeconds());
                    
                        eventData = "Dock";
                    }
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

    public int getId() {
        return id;
    }

    public int getTerminalId() {
        return terminalId;
    }

    public Badge getBadge() {
        return badge;
    }

    public Timestamp getOriginalTimestamp() {
        return originalTimestamp;
    }

    public Timestamp getAdjustedTimestamp() {
        return adjustedTimestamp;
    }

    public int getPunchTypeId() {
        return punchTypeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public void setOriginalTimestamp(Timestamp originalTimestamp) {
        this.originalTimestamp = originalTimestamp;
    }

    public void setAdjustedTimestamp(Timestamp adjustedTimeStamp) {
        this.adjustedTimestamp = adjustedTimeStamp;
    }

    public void setPunchTypeId(int punchTypeId) {
        this.punchTypeId = punchTypeId;
    }
    
    
}