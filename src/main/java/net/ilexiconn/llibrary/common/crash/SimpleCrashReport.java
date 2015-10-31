package net.ilexiconn.llibrary.common.crash;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.io.IOUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class SimpleCrashReport
{
    public String description;
    public Throwable throwable;

    public SimpleCrashReport(String d, Throwable t)
    {
        description = d;
        throwable = t;
    }

    public static String makeCrashReport(Throwable e, String d)
    {
        return new SimpleCrashReport(d, e).getCompleteReport();
    }

    public String getCompleteReport()
    {
        return "---- LLibraryUpdater Crash Report ----\n\nDescription: " + description + "\n\n-- Crash Log --\n" + getStackTrace() + "\n-- System Details --\n" + getSystemDetails();
    }

    public String getSystemDetails()
    {
        String s = "";
        s += toPrettyString("Minecraft Version", MinecraftForge.MC_VERSION);
        s += toPrettyString("Operating System", System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")");
        s += toPrettyString("Java Version", System.getProperty("java.version"));
        s += toPrettyString("Development Environment", Launch.blackboard.get("fml.deobfuscatedEnvironment") + "");
        return s;
    }

    public String toPrettyString(String key, String value)
    {
        return "\t" + key + ": " + value + "\n";
    }

    public String getStackTrace()
    {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        throwable.printStackTrace(printWriter);
        String s = stringWriter.toString();

        IOUtils.closeQuietly(stringWriter);
        IOUtils.closeQuietly(printWriter);

        return s;
    }
}
