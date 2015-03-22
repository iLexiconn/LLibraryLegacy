package net.ilexiconn.llibrary.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.entity.LLibraryEntityList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

/**
 * Spawn Egg item
 *
 * @author Ry_dog101
 */
public class ItemSpawnEgg extends Item
{
    @SideOnly(Side.CLIENT)
    private IIcon theIcon;

    /**
     * @param creativeTab Creative Tab you want your mods eggs to be in
     */
    public ItemSpawnEgg(CreativeTabs creativeTab)
    {
        setUnlocalizedName("spawnEgg");
        setCreativeTab(creativeTab);
        setTextureName("minecraft:spawn_egg");
        setHasSubtypes(true);
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        String s = ("Spawn");
        String s1 = LLibraryEntityList.getNameFromID(stack.getItemDamage());

        if (s1 != null)
        {
            s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
        }

        return s;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int meta)
    {
        LLibraryEntityList.EntityEggInfo info = LLibraryEntityList.entityEggs.get(stack.getItemDamage());
        return info != null ? (meta == 0 ? info.primaryColor : info.secondaryColor) : 16777215;
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int meta, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote) return true;
        else
        {
            Block block = world.getBlock(x, y, z);
            x += Facing.offsetsXForSide[meta];
            y += Facing.offsetsYForSide[meta];
            z += Facing.offsetsZForSide[meta];
            double d0 = 0d;

            if (meta == 1 && block.getRenderType() == 11) d0 = 0.5d;

            Entity entity = spawnCreature(world, stack.getItemDamage(), (double) x + 0.5d, (double) y + d0, (double) z + 0.5d);

            if (entity != null)
            {
                if (entity instanceof EntityLivingBase && stack.hasDisplayName()) ((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
                if (!player.capabilities.isCreativeMode) --stack.stackSize;
            }

            return true;
        }
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (world.isRemote) return stack;
        else
        {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

            if (movingobjectposition == null) return stack;
            else
            {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!world.canMineBlock(player, i, j, k)) return stack;
                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack)) return stack;

                    if (world.getBlock(i, j, k) instanceof BlockLiquid)
                    {
                        Entity entity = spawnCreature(world, stack.getItemDamage(), (double) i, (double) j, (double) k);

                        if (entity != null)
                        {
                            if (entity instanceof EntityLivingBase && stack.hasDisplayName()) ((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
                            if (!player.capabilities.isCreativeMode) --stack.stackSize;
                        }
                    }
                }

                return stack;
            }
        }
    }

    public static Entity spawnCreature(World world, int id, double x, double y, double z)
    {
        if (!LLibraryEntityList.entityEggs.containsKey(Integer.valueOf(id))) return null;
        else
        {
            Entity entity = null;

            for (int j = 0; j < 1; ++j)
            {
                entity = LLibraryEntityList.createEntityByID(id, world);

                if (entity != null && entity instanceof EntityLivingBase)
                {
                    EntityLiving entityliving = (EntityLiving)entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.onSpawnWithEgg(null);
                    world.spawnEntityInWorld(entity);
                    entityliving.playLivingSound();
                }
            }

            return entity;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int dam, int meta)
    {
        return meta > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(dam, meta);
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, java.util.List list)
    {
        for (LLibraryEntityList.EntityEggInfo entityegginfo : LLibraryEntityList.entityEggs.values())
        {
            list.add(new ItemStack(item, 1, entityegginfo.spawnedID));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        theIcon = iconRegister.registerIcon(getIconString() + "_overlay");
    }
}
