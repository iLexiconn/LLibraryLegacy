package net.ilexiconn.llibrary.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemModelledArmor extends ItemArmor
{
    @SideOnly(Side.CLIENT)
    private ModelBiped armorModel;

    private IArmorModelReceiver armorModelReceiver;

    public ItemModelledArmor(String textureName, ArmorMaterial material, int armorType, IArmorModelReceiver receiver)
    {
        super(material, 4, armorType);
        setUnlocalizedName(receiver.getModelTextureName() + "_" + textureName);
        setTextureName(textureName);
        armorModelReceiver = receiver;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon(getIconString());
    }

    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        ItemArmor item = (ItemArmor) stack.getItem();
        switch (item.armorType)
        {
            case 2:
                return armorModelReceiver.getModelTextureName() + "_1.png";
            default:
                return armorModelReceiver.getModelTextureName() + "_0.png";
        }
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
    {
        int type = ((ItemArmor) itemStack.getItem()).armorType;
        if (type == 1 || type == 3) armorModel = armorModelReceiver.getArmorModel(0);
        else armorModel = armorModelReceiver.getArmorModel(1);

        if (armorModel != null)
        {
            armorModel.bipedHead.showModel = armorSlot == 0;
            armorModel.bipedHeadwear.showModel = armorSlot == 0;
            armorModel.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
            armorModel.bipedRightArm.showModel = armorSlot == 1;
            armorModel.bipedLeftArm.showModel = armorSlot == 1;
            armorModel.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
            armorModel.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
            armorModel.isSneak = entityLiving.isSneaking();
            armorModel.isRiding = entityLiving.isRiding();
            armorModel.isChild = entityLiving.isChild();
            armorModel.heldItemRight = entityLiving.getEquipmentInSlot(0) != null ? 1 : 0;

            if (entityLiving instanceof EntityPlayer)
                armorModel.aimedBow = ((EntityPlayer) entityLiving).getItemInUseDuration() > 2;

            return armorModel;
        }

        return null;
    }
}
