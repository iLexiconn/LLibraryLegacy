package net.ilexiconn.llibrary.update;

public class UpdateCheckerThread extends Thread
{
    public void run()
    {
        VersionHandler.searchForOutdatedMods();
    }
}
