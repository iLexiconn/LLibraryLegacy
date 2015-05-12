package net.ilexiconn.llibrary.common.time;

import java.util.Calendar;

/**
 * Helper class for getting time and date for the current system.
 * NOTE: The time returned from the methods in this class are only snapshots of what it was
 * when the game was started.
 *
 * @author FiskFille
 */
public class SystemTime
{
    private static String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static String[] weekDayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private static Calendar c = Calendar.getInstance();

    public static int getYear()
    {
        return c.get(c.YEAR);
    }

    public static int getMonth()
    {
        return c.get(c.MONTH) + 1;
    }

    public static int getWeekOfMonth()
    {
        return c.get(c.WEEK_OF_MONTH);
    }

    public static int getWeekOfYear()
    {
        return c.get(c.WEEK_OF_YEAR);
    }

    public static int getDayOfMonth()
    {
        return c.get(c.DAY_OF_MONTH);
    }

    public static int getDayOfWeek()
    {
        int i = c.get(c.DAY_OF_WEEK);
        return i == 1 ? 7 : i - 1;
    }

    public static int getDayOfWeek(int year, int month, int day)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.YEAR, year);
        cal.set(cal.MONTH, month - 1);
        cal.set(cal.DAY_OF_MONTH, day);
        int i = cal.get(cal.DAY_OF_WEEK);
        return i == 1 ? 7 : i - 1;
    }

    public static int getDayOfWeek(int month, int day)
    {
        return getDayOfWeek(getYear(), month, day);
    }

    public static int getDayOfWeek(int year, String month, int day)
    {
        return getDayOfWeek(year, getMonthFromName(month), day);
    }

    public static int getDayOfYear()
    {
        return c.get(c.DAY_OF_YEAR);
    }

    public static int getHourOfDay()
    {
        return c.get(c.HOUR_OF_DAY);
    }

    public static int getMinuteOfHour()
    {
        return c.get(c.MINUTE);
    }

    public static int getSecondOfMinute()
    {
        return c.get(c.SECOND);
    }

    public static Time getTime()
    {
        Time time = new Time();
        time.hours = getHourOfDay();
        time.minutes = getMinuteOfHour();
        time.seconds = getSecondOfMinute();
        return time;
    }

    public static String getMonthName(int month)
    {
        return month > 0 && month < 13 ? monthNames[month - 1] : "missingno";
    }

    public static int getMonthFromName(String month)
    {
        for (int i = 0; i < monthNames.length; ++i)
        {
            if (monthNames[i].equals(month))
            {
                return i + 1;
            }
        }
        return 0;
    }

    public static String getDayOfWeekName(int dayOfWeek)
    {
        return dayOfWeek > 0 && dayOfWeek < 8 ? weekDayNames[dayOfWeek - 1] : "missingno";
    }

    public static int getDayOfWeekFromName(String dayOfWeek)
    {
        for (int i = 0; i < weekDayNames.length; ++i)
        {
            if (weekDayNames[i].equals(dayOfWeek))
            {
                return i + 1;
            }
        }
        return 0;
    }

    public static String getNumberSuffix(int number)
    {
        String s = String.valueOf(number);

        if (s.endsWith("1"))
        {
            return "st";
        }
        else if (s.endsWith("2"))
        {
            return "nd";
        }
        else if (s.endsWith("3"))
        {
            return "rd";
        }
        else
        {
            return "th";
        }
    }
}