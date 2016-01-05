package net.ilexiconn.llibrary.common.update;

/**
 * @author FiskFille
 * @since 0.1.0
 */
public class UpdateCheckerThread extends Thread {
    public void run() {
        VersionHandler.searchForOutdatedMods();
    }
}
