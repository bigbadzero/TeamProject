# TeamProject
This program was written by Benjamin Watson, Bendan Roberts, Bryan Luna, and Brody Smith

This Project is designed to allow a company to implement a digital punch card system

This project was written in java and utilises Microsoft SQL as it Database

The main features should include

  1.) Allow an employee to clock in with a badge when he/she gets to work
	
  2.) Calculate if an employee is late and document the absence
	
  3.) Doc time if neccessarry
	
  4.) Calculate overtime if the employee works far enough over their shift
	
  5.) Handle diffrent shifts and calculate hours appropriate regardless of shift
	
  
 This entire program is written to be easy to understand and only contains eight diffrent classes
 
1.) TeamProject.Java
  This is the main class for the project and all it does is create the Database to be used by our other classes 
	
2.) Punch.java
  This class is used to document  punch rules. A punch is anytime the employee uses their badge to clock in or out at the beggining of a shift, lunch, end of shift. It should be able to handle time changes if the employee is on night shift and his time will go into the next day. A punch must follow certain criteria that determines if you are late, early, or on time. An example would be if an employee clocks in 5 minutes late the punch rule would determine that the employee is within the grace period and they would not be docked time. However if they clock in 15 minutes late they would be docked a certain amount of time.
	
3.)Shift.java 
  This class is used to house rules for the diffrennt shifts. The main method of this class is the initScheduleList method. it will create a dailySchedule each day by quering information from the database such what the start time on that day, end time on that day, gracePeriod, and so forth. This criteria will change depending on shift and day. There is a print weekly schedule method that the company can use to print their weekly schedule to hand to an employee as well
	
4.)Badge.java 
  This class is used to get the employees information whenever they use their badge such as their name, badge number, and description
	
5.)DailySchedule.Java
  This class is used to handle the Daily Schedule. It will handle the calculations to determine what day of the week it is, what month it is, and what year it is when a punch was recorded. It utilizes the GregorianCalender within java
	
6.)  Absenteeism.java
  This class calculates Absenteeism. It takes data from the punch and shift classes and calculates how many minutes a person should be docked.
	
7.)TASDatabase.java
  This class houses how to connect to the database. It utilizes prepared statements that querys the database based upon what criteria we won't to view. An example of this is where the system checks for recurring overrides for all employees. The system attempts to do a select statement from the scheduleoverride table where the badge id is null and the string starts with anything and end is Null
	
8.)TASLogic.java
  This class calculates the total time and employee works. Most of this calculation is done in milliseconds before converting the total number of milliseconds to minutes. There are several diffrent calculations done in this class through various methods that are needed to be converted before stored in the database. Several of these are done with GregorianCalendar and several are converted into doubles from milliseconds.
	
This program is easily changed to allow a diffrent company to implement its own abseneeism rules, shift rules, and logic. The program is also capaple of allowing the company to change to a diffrent database by simply modifying the TASDatabase.java class if it wanted to use a completely seperate database for a diffrent location of employees or a diffrent department that would still use the same rules.
