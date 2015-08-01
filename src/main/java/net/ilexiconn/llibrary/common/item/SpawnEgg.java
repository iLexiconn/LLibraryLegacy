package net.ilexiconn.llibrary.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Iterator;

import net.ilexiconn.llibrary.common.entity.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * @author Ry_dog101
 */
public class SpawnEgg extends ItemMonsterPlacer
{
    public CreativeTabs creativeTab;

    public SpawnEgg(CreativeTabs creativeTab)
    {
        super();
        this.setCreativeTab(creativeTab);
        this.creativeTab = creativeTab;
        this.setUnlocalizedName("spawnEgg");
        this.setTextureName("minecraft:spawn_egg");
    }

    public String getItemStackDisplayName(ItemStack p_77653_1_)
    {
        String s = ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
        String s1 = List.entities.get(p_77653_1_.getItemDamage()).entityName;

        if (s1 != null)
        {
            s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
        }

        return s;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        List.Entities entityegginfo = List.entities.get(Integer.valueOf(p_82790_1_.getItemDamage()));
        return entityegginfo != null ? (p_82790_2_ == 0 ? entityegginfo.background : entityegginfo.forground) : 16777215;
    }

    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * Parameters: world, entityID, x, y, z.
     */
    public static Entity spawnCreature(World p_77840_0_, int p_77840_1_, double p_77840_2_, double p_77840_4_, double p_77840_6_)
    {
        if (!List.entities.containsKey(Integer.valueOf(p_77840_1_)))
        {
            return null;
        }
        else
        {
            Entity entity = null;

            for (int j = 0; j < 1; ++j)
            {
                entity = EntityList.createEntityByID(p_77840_1_, p_77840_0_);

                if (entity != null && entity instanceof EntityLivingBase)
                {
                    EntityLiving entityliving = (EntityLiving) entity;
                    entity.setLocationAndAngles(p_77840_2_, p_77840_4_, p_77840_6_, MathHelper.wrapAngleTo180_float(p_77840_0_.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.onSpawnWithEgg((IEntityLivingData) null);
                    p_77840_0_.spawnEntityInWorld(entity);
                    entityliving.playLivingSound();
                }
            }

            return entity;
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, java.util.List p_150895_3_)
    {
        Iterator iterator = List.entities.values().iterator();

        while (iterator.hasNext())
        {
            List.Entities entityegginfo = (List.Entities) iterator.next();
            if (creativeTab == List.entities.get(new ItemStack(p_150895_1_, 1, entityegginfo.id).getItemDamage()).creativeTab)
            {
                p_150895_3_.add(new ItemStack(p_150895_1_, 1, entityegginfo.id));
            }
        }
    }
}