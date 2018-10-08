package teamproject;

/**
 *
 * @author Brendan
 */
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Punch {
    
    
    public final static int CLOCKED_OUT = 0;
    public final static int CLOCKED_IN = 1;
    public final static int TIMED_OUT = 2;
    
    
    public static final String[] PUNCH_TYPES = { "CLOCKED OUT","CLOCKED IN", "TIMED OUT"}; 
    public static final String[] DAYS_OF_WEEK = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
    
    private int id;
    private int terminalId;
    private Badge badge;
    private Timestamp originalTimestamp;
    private Timestamp adjustedTimestamp;
    private int punchTypeId;
    
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