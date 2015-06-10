package net.ilexiconn.llibrary.common.playerhead;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author gegy1000
 * @since 0.3.0 //TODO ?
 */
public class SkullHelper
{
    public static ItemStack createSkull(EntityPlayer player)
    {
        return createSkull(player.getUniqueID());
    }

    public static ItemStack createSkull(UUID uuid)
    {
        ItemStack stack = new ItemStack(Items.skull, 1, 3);

        NBTTagCompound tagCompound = stack.getTagCompound();

        if (tagCompound == null)
        {
            tagCompound = new NBTTagCompound();
        }

        tagCompound.setString("SkullOwner", uuid.toString());

        stack.setTagCompound(tagCompound);

        return stack;
    }

    /**
     * @deprecated should not be using usernames anymore, use uuid's!
     */
    @Deprecated
    public static ItemStack createSkullFromUsername(String username)
    {
        ItemStack stack = new ItemStack(Items.skull, 1, 3);

        NBTTagCompound tagCompound = stack.getTagCompound();

        if (tagCompound == null)
        {
            tagCompound = new NBTTagCompound();
        }

        tagCompound.setString("SkullOwner", username);

        stack.setTagCompound(tagCompound);

        return stack;
    }

    public static ItemStack getCowSkull()
    {
        return createMojangSkull("Cow");
    }

    public static ItemStack getSquidSkull()
    {
        return createMojangSkull("Squid");
    }

    public static ItemStack getSheepSkull()
    {
        return createMojangSkull("Sheep");
    }

    public static ItemStack getAlexSkull()
    {
        return createMojangSkull("Alex");
    }

    public static ItemStack getBlazeSkull()
    {
        return createMojangSkull("Blaze");
    }

    public static ItemStack getCaveSpiderSkull()
    {
        return createMojangSkull("CaveSpider");
    }

    public static ItemStack getChickenSkull()
    {
        return createMojangSkull("Chicken");
    }

    public static ItemStack getEndermanSkull()
    {
        return createMojangSkull("Enderman");
    }

    public static ItemStack getGhastSkull()
    {
        return createMojangSkull("Ghast");
    }

    public static ItemStack getGolemSkull()
    {
        return createMojangSkull("Golem");
    }

    public static ItemStack getHerobrineSkull()
    {
        return createMojangSkull("Herobrine");
    }

    public static ItemStack getLavaSlimeSkull()
    {
        return createMojangSkull("LavaSlime");
    }

    public static ItemStack getMooshroomCowSkull()
    {
        return createMojangSkull("MushroomCow");
    }

    public static ItemStack getOcelotSkull()
    {
        return createMojangSkull("Ocelot");
    }

    public static ItemStack getPigSkull()
    {
        return createMojangSkull("Pig");
    }

    public static ItemStack getPigZombieSkull()
    {
        return createMojangSkull("PigZombie");
    }

    public static ItemStack getSlimeSkull()
    {
        return createMojangSkull("Slime");
    }

    public static ItemStack getSpiderSkull()
    {
        return createMojangSkull("Spider");
    }

    public static ItemStack getVillagerSkull()
    {
        return createMojangSkull("Villager");
    }

    public static ItemStack getCactusSkull()
    {
        return createMojangSkull("Cactus");
    }

    public static ItemStack getCakeSkull()
    {
        return createMojangSkull("Cake");
    }

    public static ItemStack getCoconutBSkull()
    {
        return createMojangSkull("CoconutB");
    }

    public static ItemStack getCoconutGSkull()
    {
        return createMojangSkull("CoconutG");
    }

    public static ItemStack getMelonSkull()
    {
        return createMojangSkull("Melon");
    }

    public static ItemStack getOakLogSkull()
    {
        return createMojangSkull("OakLog");
    }

    public static ItemStack getPresent1Skull()
    {
        return createMojangSkull("Present1");
    }

    public static ItemStack getPresent2Skull()
    {
        return createMojangSkull("Present2");
    }

    public static ItemStack getTNT1Skull()
    {
        return createMojangSkull("TNT1");
    }

    public static ItemStack getTNT2Skull()
    {
        return createMojangSkull("TNT2");
    }

    public static ItemStack getArrowUpSkull()
    {
        return createMojangSkull("ArrowUp");
    }

    public static ItemStack getArrowDownSkull()
    {
        return createMojangSkull("ArrowDown");
    }

    public static ItemStack getArrowLeftSkull()
    {
        return createMojangSkull("ArrowLeft");
    }

    public static ItemStack getArrowRightSkull()
    {
        return createMojangSkull("ArrowRight");
    }

    public static ItemStack getExclamationSkull()
    {
        return createMojangSkull("Exclamation");
    }

    public static ItemStack getQuestionSkull()
    {
        return createMojangSkull("Question");
    }

    public static ItemStack getWitherSkeletonSkull()
    {
        return new ItemStack(Items.skull, 1, 1);
    }

    public static ItemStack getZombieSkull()
    {
        return new ItemStack(Items.skull, 1, 2);
    }

    public static ItemStack getCreeperSkull()
    {
        return new ItemStack(Items.skull, 1, 4);
    }

    public static ItemStack getSteveSkull()
    {
        return new ItemStack(Items.skull, 1, 3);
    }

    public static ItemStack getSkeletonSkull()
    {
        return new ItemStack(Items.skull, 1, 0);
    }

    public static ItemStack[] getMojangSkulls()
    {
        return new ItemStack[] { getAlexSkull(), getArrowDownSkull(), getArrowLeftSkull(), getArrowRightSkull(), getArrowUpSkull(), getBlazeSkull(), getCactusSkull(), getCakeSkull(), getCaveSpiderSkull(), getChickenSkull(), getCoconutBSkull(), getCoconutGSkull(), getCowSkull(), getEndermanSkull(), getExclamationSkull(), getGhastSkull(), getGolemSkull(), getHerobrineSkull(), getLavaSlimeSkull(), getMelonSkull(), getMooshroomCowSkull(), getOakLogSkull(), getOcelotSkull(), getPigSkull(), getPigZombieSkull(), getPresent1Skull(), getPresent2Skull(), getQuestionSkull(), getSheepSkull(), getSlimeSkull(), getSpiderSkull(), getSquidSkull(), getTNT1Skull(), getTNT2Skull(), getVillagerSkull() };
    }

    public static ItemStack createMojangSkull(String mojangSkullType)
    {
        return createSkullFromUsername("MHF_" + mojangSkullType);
    }
}
