package net.ilexiconn.llibrary.common.update;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChangelogHandler
{
    public static String[] getChangelog(ModUpdateContainer mod, String version) throws Exception
    {
        ArrayList<String> list = getVersionChangelog(mod, version);
        return list.toArray(new String[4096]);
    }

    private static ArrayList<String> getVersionChangelog(ModUpdateContainer mod, String version) throws Exception
    {
        ArrayList<String> changelog = Lists.newArrayList();

        try
        {
            List<String> list = mod.updateFile;

            for (String line : list)
            {
                String expected = mod.modid + "Log|" + version + ":";

                if (line.contains(expected))
                {
                    String s1 = line.substring(line.indexOf(expected));
                    String s2 = s1.substring(expected.length());
                    String[] astring1 = s2.split(" ENDLINE ");

                    Collections.addAll(changelog, astring1);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return changelog;
    }

    public static boolean hasModGotChangelogForVersion(ModUpdateContainer mod, String version) throws Exception
    {
        try
        {
            List<String> list = mod.updateFile;

            for (String string : list)
            {
                String s = mod.modid + "Log|" + version + ":";

                if (string.contains(s))
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}