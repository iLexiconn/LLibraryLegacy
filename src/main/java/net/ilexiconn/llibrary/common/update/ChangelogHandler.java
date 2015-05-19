package net.ilexiconn.llibrary.common.update;

import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;

import java.util.List;

/**
 * @author FiskFille, iLexiconn
 */
public class ChangelogHandler
{
    public static String[] getChangelog(JsonModUpdate mod, String version) throws Exception
    {
        List<String> list = getVersionChangelog(mod, version);
        return list.toArray(new String[4096]);
    }

    private static List<String> getVersionChangelog(JsonModUpdate mod, String version) throws Exception
    {
        return mod.getVersions().get(version);
    }

    public static boolean hasModGotChangelogForVersion(JsonModUpdate mod, String version) throws Exception
    {
        return mod.getVersions().containsKey(version);
    }
}