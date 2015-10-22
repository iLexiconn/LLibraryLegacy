package net.ilexiconn.llibrary.common.update;

import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;

import java.util.List;

/**
 * @author FiskFille
 * @author iLexiconn
 * @since 0.1.0
 */
public class ChangelogHandler
{
    public static String[] getChangelog(JsonModUpdate mod, ArtifactVersion version)
    {
        if (hasModGotChangelogForVersion(mod, version))
        {
            List<String> list = getVersionChangelog(mod, version);
            return list.toArray(new String[list.size()]);
        }
        else return new String[0];
    }

    private static List<String> getVersionChangelog(JsonModUpdate mod, ArtifactVersion version)
    {
        return mod.getVersions().get(version.getVersionString());
    }

    public static boolean hasModGotChangelogForVersion(JsonModUpdate mod, ArtifactVersion version)
    {
        return mod.getVersions().containsKey(version.getVersionString());
    }
}