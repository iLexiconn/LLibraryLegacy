package net.ilexiconn.llibrary.common.update;

import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;

import java.util.List;

/**
 * @author FiskFille
 * @author iLexiconn
 * @since 0.1.0
 */
public class ChangelogHandler
{
    public static String[] getChangelog(JsonModUpdate mod, String version)
    {
        if (hasModGotChangelogForVersion(mod, version))
        {
            List<String> list = getVersionChangelog(mod, version);
            return list.toArray(new String[list.size()]);
        }
        else return new String[0];
    }

    private static List<String> getVersionChangelog(JsonModUpdate mod, String version)
    {
        return mod.getVersions().get(version);
    }

    public static boolean hasModGotChangelogForVersion(JsonModUpdate mod, String version)
    {
        return mod.getVersions().containsKey(version);
    }
}