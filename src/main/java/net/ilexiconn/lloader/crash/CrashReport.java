package net.ilexiconn.lloader.crash;

import com.google.common.collect.Lists;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.asm.transformers.BlamingTransformer;
import net.minecraftforge.fml.relauncher.CoreModManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class CrashReport
{
    public String description;
    public Throwable cause;
    public Category category = new Category("System Details");
    public List crashReportSections = Lists.newArrayList();
    public StackTraceElement[] stacktrace = new StackTraceElement[0];
    public Random random = new Random();

    public CrashReport(String d, Throwable e)
    {
        description = d;
        cause = e;
        populateEnvironment();
    }

    public static String makeCrashReport(Throwable e, String d)
    {
        return new CrashReport(d, e).getCompleteReport();
    }

    public String getCompleteReport()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- LLibrary Crash Report ----\n");
        BlamingTransformer.onCrash(stringbuilder);
        CoreModManager.onCrash(stringbuilder);
        stringbuilder.append("// ");
        stringbuilder.append(getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append((new SimpleDateFormat()).format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(description);
        stringbuilder.append("\n\n");
        stringbuilder.append(getStackTrace());
        stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

        for (int i = 0; i < 87; ++i) stringbuilder.append("-");

        stringbuilder.append("\n\n");
        writeSections(stringbuilder);
        return stringbuilder.toString();
    }

    public String getWittyComment()
    {
        String[] comments = new String[] {"I should've tested it", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "Oh - I know what I did wrong!", "Don\'t be sad. I\'ll do better next time, I promise!", "Quite honestly, I wouldn\'t worry myself about that.", "Surprise! Haha. Well, this is awkward.", "This doesn\'t make any sense!", "But it works on my machine.", "LLibrary sucks, doesn't it?", "Stop crashing please, thank you."};

        try
        {
            return comments[random.nextInt(comments.length)];
        }
        catch (Throwable throwable)
        {
            return comments[0];
        }
    }

    public void populateEnvironment()
    {
        category.addCrashSectionCallable("Minecraft Version", new Callable()
        {
            public String call()
            {
                return MinecraftForge.MC_VERSION;
            }
        });
        category.addCrashSectionCallable("Operating System", new Callable()
        {
            public String call()
            {
                return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
            }
        });
        category.addCrashSectionCallable("Java Version", new Callable()
        {
            public String call()
            {
                return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
            }
        });
        category.addCrashSectionCallable("Java VM Version", new Callable()
        {
            public String call()
            {
                return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
            }
        });
        category.addCrashSectionCallable("Memory", new Callable()
        {
            public String call()
            {
                Runtime runtime = Runtime.getRuntime();
                long i = runtime.maxMemory();
                long j = runtime.totalMemory();
                long k = runtime.freeMemory();
                long l = i / 1024L / 1024L;
                long i1 = j / 1024L / 1024L;
                long j1 = k / 1024L / 1024L;
                return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
            }
        });
        category.addCrashSectionCallable("JVM Flags", new Callable()
        {
            public String call()
            {
                RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
                List list = runtimemxbean.getInputArguments();
                int i = 0;
                StringBuilder stringbuilder = new StringBuilder();

                for (Object obj : list)
                {
                    String s = (String) obj;
                    if (s.startsWith("-X"))
                    {
                        if (i++ > 0) stringbuilder.append(" ");
                        stringbuilder.append(s);
                    }
                }

                return String.format("%d total; %s", i, stringbuilder.toString());
            }
        });
        category.addCrashSectionCallable("IntCache", new Callable()
        {
            public String call()
            {
                return IntCache.getCacheSizes();
            }
        });
    }

    public void writeSections(StringBuilder builder)
    {
        if ((stacktrace == null || stacktrace.length <= 0) && crashReportSections.size() > 0) stacktrace = ArrayUtils.subarray(((Category) crashReportSections.get(0)).stackTrace, 0, 1);

        if (stacktrace != null && stacktrace.length > 0)
        {
            builder.append("-- Head --\n");
            builder.append("Stacktrace:\n");

            for (StackTraceElement stacktraceelement : stacktrace)
            {
                builder.append("\t").append("at ").append(stacktraceelement.toString());
                builder.append("\n");
            }

            builder.append("\n");
        }

        for (Object crashReportSection : crashReportSections)
        {
            Category crashreportcategory = (Category) crashReportSection;
            crashreportcategory.appendToStringBuilder(builder);
            builder.append("\n\n");
        }

        category.appendToStringBuilder(builder);
    }

    public String getStackTrace()
    {
        StringWriter stringwriter = null;
        PrintWriter printwriter = null;
        Object object = cause;

        if (((Throwable)object).getMessage() == null)
        {
            if (object instanceof NullPointerException) object = new NullPointerException(description);
            else if (object instanceof StackOverflowError) object = new StackOverflowError(description);
            else if (object instanceof OutOfMemoryError) object = new OutOfMemoryError(description);

            ((Throwable)object).setStackTrace(cause.getStackTrace());
        }

        String s = object.toString();

        try
        {
            stringwriter = new StringWriter();
            printwriter = new PrintWriter(stringwriter);
            ((Throwable)object).printStackTrace(printwriter);
            s = stringwriter.toString();
        }
        finally
        {
            IOUtils.closeQuietly(stringwriter);
            IOUtils.closeQuietly(printwriter);
        }

        return s;
    }

    class Category
    {
        public String name;
        public List children = Lists.newArrayList();
        public StackTraceElement[] stackTrace = new StackTraceElement[0];

        public Category(String n)
        {
            name = n;
        }

        public void addCrashSectionCallable(String sectionName, Callable callable)
        {
            try
            {
                addCrashSection(sectionName, callable.call());
            }
            catch (Throwable throwable)
            {
                addCrashSectionThrowable(sectionName, throwable);
            }
        }

        public void addCrashSection(String sectionName, Object value)
        {
            children.add(new CategoryEntry(sectionName, value));
        }

        public void addCrashSectionThrowable(String sectionName, Throwable throwable)
        {
            addCrashSection(sectionName, throwable);
        }

        public void appendToStringBuilder(StringBuilder builder)
        {
            builder.append("-- ").append(name).append(" --\n");
            builder.append("Details:");

            for (Object aChildren : children)
            {
                CategoryEntry entry = (CategoryEntry) aChildren;
                builder.append("\n\t");
                builder.append(entry.key);
                builder.append(": ");
                builder.append(entry.value);
            }

            if (stackTrace != null && stackTrace.length > 0)
            {
                builder.append("\nStacktrace:");

                for (StackTraceElement stacktraceelement : stackTrace)
                {
                    builder.append("\n\tat ");
                    builder.append(stacktraceelement.toString());
                }
            }
        }
    }

    class CategoryEntry
    {
        public String key;
        public String value;

        public CategoryEntry(String k, Object v)
        {
            key = k;

            if (v == null) value = "~~NULL~~";
            else if (v instanceof Throwable)
            {
                Throwable throwable = (Throwable) v;
                value = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
            }
            else value = v.toString();
        }
    }
}
