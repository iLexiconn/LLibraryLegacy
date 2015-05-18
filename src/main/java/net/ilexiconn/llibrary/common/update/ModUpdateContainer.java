package net.ilexiconn.llibrary.common.update;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import com.google.common.collect.Lists;

public class ModUpdateContainer
{
    public String modid;
    public String version;
    public String name;
    public String updateTextFileURL;
    public URL website;
    public String latestVersion;
    public BufferedImage thumbnail;

    public List<String> updateFile = Lists.newArrayList();
}