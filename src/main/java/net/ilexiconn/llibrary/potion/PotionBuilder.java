package net.ilexiconn.llibrary.potion;

import java.util.*;

import com.google.common.collect.*;

import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;

public class PotionBuilder
{

    private String potionID;
    private boolean effectBad;
    private int liquidColor;
    private ResourceLocation texture;
    private int iconX;
    private int iconY;
    private List<ItemStack> ingredients;

    public PotionBuilder(String potionID)
    {
        ingredients = Lists.newArrayList();

        this.potionID = potionID;

        byte red = (byte) (Math.random() * 255);
        byte green = (byte) (Math.random() * 255);
        byte blue = (byte) (Math.random() * 255);
        liquidColor = red << 16 | green << 8 | blue;
    }

    public PotionBuilder setLiquidColor(int color)
    {
        liquidColor = color;
        return this;
    }

    public ResourceLocation getTexture()
    {
        return texture;
    }

    public PotionBuilder setTexture(ResourceLocation texture)
    {
        this.texture = texture;
        return this;
    }

    public PotionBuilder setIconIndex(int iconX, int iconY)
    {
        this.iconX = iconX;
        this.iconY = iconY;
        return this;
    }

    public PotionBuilder setIconY(int iconY)
    {
        this.iconY = iconY;
        return this;
    }

    public PotionBuilder setIconX(int iconX)
    {
        this.iconX = iconX;
        return this;
    }

    public int getIconY()
    {
        return iconY;
    }

    public int getIconX()
    {
        return iconX;
    }

    public int getLiquidColor()
    {
        return liquidColor;
    }

    public String getPotionID()
    {
        return potionID;
    }

    public PotionBuilder setGood()
    {
        effectBad = false;
        return this;
    }

    public PotionBuilder setBad()
    {
        effectBad = true;
        return this;
    }

    public boolean isBad()
    {
        return effectBad;
    }

    public Potion build()
    {
        Potion potion = null;
        List<String> effects = Lists.newArrayList();
        for (ItemStack i : ingredients)
        {
            Item item = i.getItem();
            if (item == null)
                throw new IllegalArgumentException("Found null item in recipe");
            String effect = null;
            if (item.isPotionIngredient(i))
            {
                effect = item.getPotionEffect(i);
            }
            else
            {
                ; // TODO: generate an effect
            }
            effects.add(effect);
        }

        int damage = 0;
        for (String s : effects)
        {
            damage = TempPotionHelper.applyIngredient(damage, s);
        }
        return potion;
    }

    public PotionBuilder addIngredient(ItemStack item)
    {
        ingredients.add(item);
        return this;
    }

}
