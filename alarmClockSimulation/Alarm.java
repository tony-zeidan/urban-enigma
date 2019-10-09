
/**
 * The Alarm class simulates an alarm using the ClockDisplay12 class
 * and a enable boolean
 */
public class Alarm
{
    private ClockDisplay12 clkDisp;
    private Boolean alrmSet;

    /**
     * default constructor for the Alarm class
     * disengages alarm and sets alarm time to midnight
     */
    public Alarm()
    {
        alrmSet = false;
        clkDisp = new ClockDisplay12(12,0,"a.m.");
    }
    
    /**
     * other consturctor for the Alarm class
     * sets alarm time and engages with respect to parameters
     */
    public Alarm(int hour, int minute, String amPm, Boolean isSet) {
        alrmSet = isSet;
        clkDisp = new ClockDisplay12(hour,minute,amPm);
    }
    
    /**
     * method for setting the time for the current instance
     * of the Alarm class
     */
    public void setTime(int hour, int minute, String amPm) {
        clkDisp.setTime(hour,minute,amPm);
    }
    
    /**
     * method turnOn engages the current instance of the Alarm class
     * method turnOff disengages the current instance of the Alarm class
     */
    public void turnOn() {
        alrmSet = true;
    }
    public void turnOff() {
        alrmSet = false;
    }
    
    /**
     * method getTime returns the set time for the
     * current instance of the Alarm class
     */
    public String getTime() {
        return clkDisp.getTime();
    }
    
    /**
     * method isSet returns the activity of the current instance of the Alarm class
     * if the alarm is engaged it will return true, otherwise false
     */
    public Boolean isSet() {
        return alrmSet;
    }
}

