
/**
 * The ClockDisplay class implements a digital clock display for a
 * European-style 24 hour clock. The clock shows hours and minutes. The 
 * range of the clock is 00:00 (midnight) to 23:59 (one minute before 
 * midnight).
 * 
 * The clock display receives "ticks" (via the timeTick method) every minute
 * and reacts by incrementing the display. This is done in the usual clock
 * fashion: the hour increments when the minutes roll over to zero.
 * 
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
public class ClockDisplay12
{
    public static final String AM = "a.m.";
    public static final String PM = "p.m.";
    
    private NumberDisplay hours;
    private NumberDisplay minutes;
    private String amPm;
    private String displayString;    // simulates the actual display
    
    /**
     * Constructor for ClockDisplay objects. This constructor 
     * creates a new clock set at 12:00a.m.
     */
    public ClockDisplay12()
    {
        hours = new NumberDisplay(12);
        minutes = new NumberDisplay(60);
        this.amPm = AM;
        updateDisplay();
    }

    /**
     * Constructor for ClockDisplay objects. This constructor
     * creates a new clock set at the time specified by the 
     * parameters.
     */
    public ClockDisplay12(int hour, int minute,String amPm)
    {
        hours = new NumberDisplay(12);
        minutes = new NumberDisplay(60);
        if (amPm.equals(AM) || amPm.equals(PM)) {
            setTime(hour, minute, amPm);
        } else {
            setTime(hour, minute, AM);
        }
        
    }

    /**
     * This method should get called once every minute - it makes
     * the clock display go one minute forward and will change from AM to PM accordingly.
     */
    public void timeTick()
    {
        minutes.increment();
        if(minutes.getValue() == 0) {  // it just rolled over!
            hours.increment();
            if (hours.getValue() == 0) {
                if (this.amPm.equals(PM)) {
                    this.amPm = AM;
                } else {
                    this.amPm = PM;
                }
            }
        }
        updateDisplay();
    }

    /**
     * Set the time of the display to the specified hour and
     * minute and AM or PM parameter.
     */
    public void setTime(int hour, int minute, String amPm)
    {
        if (hour == 12) {
            hours.setValue(0);
        } else {
            hours.setValue(hour);
        }
        minutes.setValue(minute);
        
        if (amPm.equals(AM) || amPm.equals(PM)) {
           this.amPm = amPm;
        } else {
           this.amPm = AM;
        }
        updateDisplay();
    }

    /**
     * Return the current time of this display in the format HH:MM[a.m./p.m.].
     */
    public String getTime()
    {
        return displayString;
    }
    
    /**
     * Update the internal string that represents the display according
     * to the current hour, minute and timeofday fields
     */
    private void updateDisplay()
    {
        int hourDisp = hours.getValue();
        String minDisp = minutes.getDisplayValue();
       
        
        if (hourDisp == 0) {
            displayString = "12:" + minDisp + this.amPm;
        } else {
            displayString = hourDisp + ":" + minDisp + this.amPm;
        }
    }
}
