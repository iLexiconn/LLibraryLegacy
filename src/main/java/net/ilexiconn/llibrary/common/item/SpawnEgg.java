package net.ilexiconn.llibrary.common.item;

import net.ilexiconn.llibrary.common.entity.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;

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
    }

    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * Parameters: world, entityID, x, y, z.
     */
    public static Entity spawnCreature(World world, int entityID, double x, double y, double z)
    {
        if (!List.entities.containsKey(Integer.valueOf(entityID)))
        {
            return null;
        }
        else
        {
            Entity entity = null;

            for (int j = 0; j < 1; ++j)
            {
                entity = EntityList.createEntityByID(entityID, world);

                if (entity != null && entity instanceof EntityLivingBase)
                {
                    EntityLiving entityliving = (EntityLiving) entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.func_180482_a(world.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData) null);
                    world.spawnEntityInWorld(entity);
                    entityliving.playLivingSound();
                }
            }

            return entity;
        }
    }

    public String getItemStackDisplayName(ItemStack itemStack)
    {
        String s = ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
        String s1 = List.entities.get(itemStack.getMetadata()).entityName;

        if (s1 != null)
        {
            s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
        }

        return s;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int renderPass)
    {
        List.Entities entityegginfo = List.entities.get(Integer.valueOf(itemStack.getMetadata()));
        return entityegginfo != null ? (renderPass == 0 ? entityegginfo.background : entityegginfo.forground) : 16777215;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, java.util.List subItems)
    {
        Iterator iterator = List.entities.values().iterator();

        while (iterator.hasNext())
        {
            List.Entities entityegginfo = (List.Entities) iterator.next();
            if (creativeTab == List.entities.get(new ItemStack(item, 1, entityegginfo.id).getMetadata()).creativeTab)
            {
                subItems.add(new ItemStack(item, 1, entityegginfo.id));
            }
        }
    }
}