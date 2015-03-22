package net.ilexiconn.llibrary.config;

import net.minecraftforge.common.config.Configuration;

public class LLibraryConfigHandler implements IConfigHandler
{
    public static String survivalInventoryItem;

    public void loadConfig(Configuration config)
    {
        survivalInventoryItem = config.getString("survivalInventoryItem", Configuration.CATEGORY_GENERAL, "minecraft:book", "Can be changed in-game");
    }
}
