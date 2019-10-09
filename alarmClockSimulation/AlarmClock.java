
/**
 * the AlarmClock class utilizes the Alarm class and the ClockDisplay12 class
 * to simulate an alarm clock with the current time and an alarm
 */
public class AlarmClock
{
    // Alarm and Current time
    private Alarm alrm;
    private ClockDisplay12 clk; 

    /**
     * default constructor for the AlarmClock class
     * sets both the current time and the current alarm to midnight
     * disengages the alarm
     */
    public AlarmClock()
    {
        alrm = new Alarm();
        clk = new ClockDisplay12();
    }
    
    /**
     * other constructor for the AlarmClock class
     * sets both the current time and the current alarm with
     * respect to the given parameters
     */
    public AlarmClock(int hourClk,int minuteClk,String amPmClk,int hourAlrm,int minuteAlrm,String amPmAlrm,Boolean isSet) {
        alrm = new Alarm(hourAlrm,minuteAlrm,amPmAlrm,isSet);
        clk = new ClockDisplay12(hourClk,minuteClk,amPmClk);
    }
    
    /**
     * method setTime sets the current time
     * for the current instance of the AlarmClock class
     */
    public void setTime(int hour,int minute,String amPm) {
        clk.setTime(hour, minute, amPm);
    }
    
    /**
     * method setAlarm sets the current alarm time
     * for the current instance of the AlarmClock class
     */
    public void setAlarmTime(int hour,int minute,String amPm) {
        alrm.setTime(hour, minute, amPm);
    }
    
    /**
     * method clockTick simulates the current time increasing by one minute
     * if the current time is equal to the set alarm and the alarm is active
     * send the user the alarm message
     */
    public void clockTick() {
        clk.timeTick();
        if (clk.getTime().equals(alrm.getTime()) && alrm.isSet()) {
            System.out.println("RING RING RING");
            unsetAlarm();
        }
    }
    
    /**
     * method setAlarm engages the alarm for the current instance of the Alarm class
     * method unsetAlarm disengages the alarm for the current instance of the Alarm class
     */
    public void setAlarm() {
        alrm.turnOn();
    }
    public void unsetAlarm() {
        alrm.turnOff();
    }
    
    /**
     * method getTime returns the current cock time for the current
     * instance of the AlarmClock class
     */
    public String getTime() {
        return clk.getTime();
    }
    
    /**
     * method getAlarmTime returns the current time the alarm is set for
     * the current instance of the AlarmClock class
     */
    public String getAlarmTime() {
        return alrm.getTime();
    }
    
    /**
     * method isAlarmSet returns the activity of the alarm
     * for the current instance of the AlarmClock class
     */
    public Boolean isAlarmSet() {
        return alrm.isSet();
    }
}
