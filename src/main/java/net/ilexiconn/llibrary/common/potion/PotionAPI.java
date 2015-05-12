package net.ilexiconn.llibrary.common.potion;

import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class PotionAPI
{
    public static final int MAX_SIZE = 128;
    public static Potion potion;

    public PotionAPI()
    {
        Potion[] potionTypes = null;

        for (Field f : Potion.class.getDeclaredFields())
        {
            f.setAccessible(true);

            try
            {
                if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a"))
                {
                    Field modfield = Field.class.getDeclaredField("modifiers");
                    modfield.setAccessible(true);
                    modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

                    potionTypes = (Potion[]) f.get(null);
                    final Potion[] newPotionTypes = new Potion[MAX_SIZE];
                    System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
                    f.set(null, newPotionTypes);
                }
            }
            catch (Exception e)
            {
                System.err.println("Severe error, please report this to the mod author:");
                System.err.println(e);
            }
        }

        potion = PotionAPI.createPotion("test", 100, true, 43789, new ResourceLocation("llibrary", "textures/potion/test.png"), 0, 0);

        HashMap potionRequirements = getField(HashMap.class, PotionHelper.class, "potionRequirements");
        HashMap potionAmplifiers = getField(HashMap.class, PotionHelper.class, "potionAmplifiers");
        Map potionList = getField(Map.class, ItemPotion.class, "field_77835_b");

        potionRequirements.put(100, "!0 & 1 & !2 & !3 & 1+6");
        potionAmplifiers.put(100, "5");

        System.out.println("-------------------Requirements");

        for (int i = 0; i < potionRequirements.keySet().toArray().length; ++i)
        {
            String s = String.valueOf(potionRequirements.entrySet().toArray()[i]).split("=")[1];
            System.out.println(potionRequirements.keySet().toArray()[i] + ", " + s);
        }

        PotionBuilder builder = new PotionBuilder("testbuilder").setTexture(new ResourceLocation("llibrary", "textures/potion/test.png")).setIconIndex(0, 0);
        builder.setLiquidColor(0xFF00FF).setBad();
        builder.addIngredient(new ItemStack(Items.apple, 1));
        Potion built = builder.build();
    }

    public static Potion createPotion(String name, int id, boolean isEffectBad, int liquidColor, ResourceLocation texture, int iconIndexX, int iconIndexY)
    {
        if (Potion.potionTypes.length == MAX_SIZE)
        {
            Potion potion = new PotionCustom(id, isEffectBad, liquidColor, texture, iconIndexX, iconIndexY);
            potion.setPotionName(name);
            return potion;
        }
        return new PotionCustom(0, false, 0, new ResourceLocation("missingno"), 0, 0);
    }

    private <T> T getField(Class<? extends T> type, Class clazz, String... astring)
    {
        for (Field f : clazz.getDeclaredFields())
        {
            f.setAccessible(true);

            try
            {
                if (f.getName().equals(astring[0]) || astring.length >= 2 && f.getName().equals(astring[1]))
                {
                    return (T) f.get(clazz);
                }
            }
            catch (Exception e)
            {
                System.err.println("Severe error, please report this to the mod author:");
                e.printStackTrace();
            }
        }

        return null;
    }
}