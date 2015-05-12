package net.ilexiconn.llibrary.common.update;

public class UpdateCheckerThread extends Thread
{
    public void run()
    {
        VersionHandler.searchForOutdatedMods();
    }
}
