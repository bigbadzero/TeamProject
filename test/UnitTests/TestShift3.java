package UnitTests;

import org.junit.*;
import static org.junit.Assert.*;
import teamproject.*;

public class TestShift3{
	private TASDatabase db;
	
	@Before
	public void setup(){
		db = new TASDatabase();
	}
	
	@Test
	public void testFirstDay(){
		Shift s4 = db.getShift(4);
		
		Punch p1 = db.getPunch(6897);
		Punch p2 = db.getPunch(6898);
		
		p1.adjust(s4);
		p2.adjust(s4);
		
		assertEquals("#TESTMAN1 CLOCKED IN: MON 10/29/2018 22:29:00", p1.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED IN: MON 10/29/2018 22:30:00 (Shift Start)", p1.printAdjustedTimestamp());
		
		assertEquals("#TESTMAN1 CLOCKED OUT: TUE 10/30/2018 07:04:00", p2.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED OUT: TUE 10/30/2018 07:00:00 (Shift Stop)", p2.printAdjustedTimestamp());
	}
	@Test
	public void testSecondDay(){
	
		Shift s4 = db.getShift(4);
		
		Punch p1 = db.getPunch(6899);
		Punch p2 = db.getPunch(6900);
		
		p1.adjust(s4);
		p2.adjust(s4);
		
		assertEquals("#TESTMAN1 CLOCKED IN: TUE 10/30/2018 22:30:00", p1.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED IN: TUE 10/30/2018 22:30:00 (Shift Start)", p1.printAdjustedTimestamp());
		
		assertEquals("#TESTMAN1 CLOCKED OUT: WED 10/31/2018 07:00:00", p2.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED OUT: WED 10/31/2018 07:00:00 (Shift Stop)", p2.printAdjustedTimestamp());
		
	}
	
	@Test
	public void testThirdDay(){
		Shift s4 = db.getShift(4);
		
		Punch p1 = db.getPunch(6901);
		Punch p2 = db.getPunch(6902);
		
		p1.adjust(s4);
		p2.adjust(s4);
		
		assertEquals("#TESTMAN1 CLOCKED IN: WED 10/31/2018 22:31:00", p1.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED IN: WED 10/31/2018 22:30:00 (Shift Start)", p1.printAdjustedTimestamp());
		
		assertEquals("#TESTMAN1 CLOCKED OUT: THU 11/01/2018 06:56:00", p2.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED OUT: THU 11/01/2018 07:00:00 (Shift Stop)", p2.printAdjustedTimestamp());
	}
	
	@Test
	public void testFourthDay(){
		Shift s4 = db.getShift(4);
		
		Punch p1 = db.getPunch(6903);
		Punch p2 = db.getPunch(6904);
		
		p1.adjust(s4);
		p2.adjust(s4);
		
		assertEquals("#TESTMAN1 CLOCKED IN: THU 11/01/2018 22:40:00", p1.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED IN: THU 11/01/2018 22:45:00 (Shift Dock)", p1.printAdjustedTimestamp());
		
		assertEquals("#TESTMAN1 CLOCKED OUT: FRI 11/02/2018 06:50:00", p2.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED OUT: FRI 11/02/2018 06:45:00 (Shift Dock)", p2.printAdjustedTimestamp());
	}
	
	@Test
	public void testFifthDay(){
		Shift s4 = db.getShift(4);
		
		Punch p1 = db.getPunch(6905);
		Punch p2 = db.getPunch(6906);
		Punch p3 = db.getPunch(6907);
		Punch p4 = db.getPunch(6908);
		
		p1.adjust(s4);
		p2.adjust(s4);
		
		assertEquals("#TESTMAN1 CLOCKED IN: FRI 11/02/2018 22:30:00", p1.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED IN: FRI 11/02/2018 22:30:00 (Shift Start)", p1.printAdjustedTimestamp());
		
		assertEquals("#TESTMAN1 CLOCKED OUT: SAT 11/03/2018 02:31:00", p2.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED OUT: SAT 11/03/2018 02:30:00 (Lunch Start)", p2.printAdjustedTimestamp());
		
		assertEquals("#TESTMAN1 CLOCKED IN: SAT 11/03/2018 02:54:00", p3.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED IN: SAT 11/03/2018 03:00:00 (Lunch Stop)", p3.printAdjustedTimestamp());
		
		assertEquals("#TESTMAN1 CLOCKED OUT: SAT 11/03/2018 07:00:00", p4.printOriginalTimestamp());
		assertEquals("#TESTMAN1 CLOCKED OUT: SAT 11/03/2018 07:00:00 (Shift Stop)", p4.printAdjustedTimestamp());
	}
}