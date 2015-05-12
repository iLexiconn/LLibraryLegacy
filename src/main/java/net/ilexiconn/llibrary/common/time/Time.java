package net.ilexiconn.llibrary.common.time;

import net.minecraft.world.World;

/**
 * @author FiskFille
 */
public class Time
{
    public long ticks;
    public int seconds;
    public int minutes;
    public int hours;

    public static Time getTimeFromTicks(long ticks)
    {
        Time t = new Time();
        t.ticks = ticks;
        t.seconds = (int) t.ticks / 20;
        t.minutes = t.seconds / 60;
        t.hours = t.minutes / 60;
        t.minutes = t.minutes - t.hours * 60;
        t.seconds = t.seconds - t.seconds / 60 * 60;
        t.ticks = t.ticks - t.ticks / 20 * 20;
        return t;
    }

    public static int getWorldTimeInHours(World world)
    {
        long time = world.getWorldTime() % 24000;
        int hours = (int) time / 1000 + 6 > 24 ? (int) time / 1000 + 6 - 24 : (int) time / 1000 + 6;
        return hours;
    }

    public static String toAmPm(int hours)
    {
        String am = hours + (hours == 12 ? " pm" : " am");
        String pm = hours - 12 + (hours - 12 == 12 ? " am" : " pm");
        return hours > 12 ? pm : am;
    }

    public String toString()
    {
        Object[] aobject = {hours, minutes, seconds, ticks};
        return String.format("%sh %smin %ss %st", aobject);
    }

    public long totalTicks()
    {
        return ticks + seconds * 20 + minutes * 60 * 20 + hours * 60 * 60 * 20;
    }
}