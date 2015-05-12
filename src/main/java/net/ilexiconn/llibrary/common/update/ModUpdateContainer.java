package net.ilexiconn.llibrary.common.update;

import com.google.common.collect.Lists;

import java.net.URL;
import java.util.List;

public class ModUpdateContainer
{
    public String modid;
    public String version;
    public String name;
    public String pastebinId;
    public URL website;
    public String latestVersion;

    public List<String> updateFile = Lists.newArrayList();
}