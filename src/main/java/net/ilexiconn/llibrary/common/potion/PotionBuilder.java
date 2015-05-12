package net.ilexiconn.llibrary.common.potion;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Random;

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

        Random rand = new Random(potionID.hashCode());

        byte red = (byte) ((byte) (rand.nextDouble()) * 255);
        byte green = (byte) ((byte) (rand.nextDouble()) * 255);
        byte blue = (byte) ((byte) (rand.nextDouble()) * 255);

        liquidColor = red << 16 | green << 8 | blue;
    }

    public PotionBuilder setLiquidColor(int red, int green, int blue)
    {
        liquidColor = red << 16 | green << 8 | blue;

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
        setIconX(iconX);
        setIconY(iconY);

        return this;
    }

    public int getIconY()
    {
        return iconY;
    }

    public PotionBuilder setIconY(int iconY)
    {
        this.iconY = iconY;

        return this;
    }

    public int getIconX()
    {
        return iconX;
    }

    public PotionBuilder setIconX(int iconX)
    {
        this.iconX = iconX;

        return this;
    }

    public int getLiquidColor()
    {
        return liquidColor;
    }

    public PotionBuilder setLiquidColor(int color)
    {
        liquidColor = color;

        return this;
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

        for (ItemStack incredient : ingredients)
        {
            if (incredient != null)
            {
                Item item = incredient.getItem();

                if (item == null)
                    throw new IllegalArgumentException("Found null item in recipe");

                String effect = null;

                if (item.isPotionIngredient(incredient))
                {
                    effect = item.getPotionEffect(incredient);
                }
                else
                {
                    ; // TODO: generate an effect
                }

                effects.add(effect);
            }
            else
                throw new IllegalArgumentException("Found null item in recipe");
        }

        int damage = 0;

        for (String effect : effects)
        {
            damage = TempPotionHelper.applyIngredient(damage, effect);
        }

        return potion;
    }

    public PotionBuilder addIngredient(ItemStack item)
    {
        ingredients.add(item);

        return this;
    }
}
