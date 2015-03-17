package net.ilexiconn.llibrary.update;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ModUpdateContainer
{
	public String modid;
	public String version;
	public String name;
	public String pastebinId;
	public URL website;
	public String latestVersion;
	
	public List<String> updateFile = new ArrayList<String>();
}