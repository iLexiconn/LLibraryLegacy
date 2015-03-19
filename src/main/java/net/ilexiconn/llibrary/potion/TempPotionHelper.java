package net.ilexiconn.llibrary.potion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TempPotionHelper
{
    public static final String sugarEffect;
    public static final String ghastTearEffect = "+0-1-2-3&4-4+13";
    public static final String spiderEyeEffect;
    public static final String fermentedSpiderEyeEffect;
    public static final String speckledMelonEffect;
    public static final String blazePowderEffect;
    public static final String magmaCreamEffect;
    public static final String redstoneEffect;
    public static final String glowstoneEffect;
    public static final String gunpowderEffect;
    public static final String goldenCarrotEffect;
    public static final String pufferfishEffect;
    private static final HashMap potionRequirements = new HashMap();
    /** Potion effect amplifier map */
    private static final HashMap potionAmplifiers = new HashMap();
    private static final HashMap field_77925_n;
    /** An array of possible potion prefix names, as translation IDs. */
    private static final String[] potionPrefixes;
    private static final String __OBFID = "CL_00000078";

    /**
     * Is the bit given set to 1?
     */
    public static boolean checkFlag(int p_77914_0_, int p_77914_1_)
    {
        return (p_77914_0_ & 1 << p_77914_1_) != 0;
    }

    /**
     * Returns 1 if the flag is set, 0 if it is not set.
     */
    private static int isFlagSet(int p_77910_0_, int p_77910_1_)
    {
        /**
         * Is the bit given set to 1?
         */
        return checkFlag(p_77910_0_, p_77910_1_) ? 1 : 0;
    }

    /**
     * Returns 0 if the flag is set, 1 if it is not set.
     */
    private static int isFlagUnset(int p_77916_0_, int p_77916_1_)
    {
        /**
         * Is the bit given set to 1?
         */
        return checkFlag(p_77916_0_, p_77916_1_) ? 0 : 1;
    }

    public static int func_77909_a(int p_77909_0_)
    {
        return func_77908_a(p_77909_0_, 5, 4, 3, 2, 1);
    }

    /**
     * Given a {@link Collection}<{@link PotionEffect}> will return an Integer color.
     */
    public static int calcPotionLiquidColor(Collection p_77911_0_)
    {
        int i = 3694022;

        if (p_77911_0_ != null && !p_77911_0_.isEmpty())
        {
            float f = 0.0F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            float f3 = 0.0F;
            Iterator iterator = p_77911_0_.iterator();

            while (iterator.hasNext())
            {
                PotionEffect potioneffect = (PotionEffect)iterator.next();
                int j = Potion.potionTypes[potioneffect.getPotionID()].getLiquidColor();

                for (int k = 0; k <= potioneffect.getAmplifier(); ++k)
                {
                    f += (float)(j >> 16 & 255) / 255.0F;
                    f1 += (float)(j >> 8 & 255) / 255.0F;
                    f2 += (float)(j >> 0 & 255) / 255.0F;
                    ++f3;
                }
            }

            f = f / f3 * 255.0F;
            f1 = f1 / f3 * 255.0F;
            f2 = f2 / f3 * 255.0F;
            return (int)f << 16 | (int)f1 << 8 | (int)f2;
        }
        else
        {
            return i;
        }
    }

    public static boolean func_82817_b(Collection p_82817_0_)
    {
        Iterator iterator = p_82817_0_.iterator();
        PotionEffect potioneffect;

        do
        {
            if (!iterator.hasNext())
            {
                return true;
            }

            potioneffect = (PotionEffect)iterator.next();
        }
        while (potioneffect.getIsAmbient());

        return false;
    }

    @SideOnly(Side.CLIENT)
    public static int func_77915_a(int p_77915_0_, boolean p_77915_1_)
    {
        if (!p_77915_1_)
        {
            if (field_77925_n.containsKey(Integer.valueOf(p_77915_0_)))
            {
                return ((Integer)field_77925_n.get(Integer.valueOf(p_77915_0_))).intValue();
            }
            else
            {
                int j = calcPotionLiquidColor(getPotionEffects(p_77915_0_, false));
                field_77925_n.put(Integer.valueOf(p_77915_0_), Integer.valueOf(j));
                return j;
            }
        }
        else
        {
            /**
             * Given a {@link Collection}<{@link PotionEffect}> will return an Integer color.
             */
            return calcPotionLiquidColor(getPotionEffects(p_77915_0_, p_77915_1_));
        }
    }

    public static String func_77905_c(int p_77905_0_)
    {
        int j = func_77909_a(p_77905_0_);
        return potionPrefixes[j];
    }

    private static int func_77904_a(boolean p_77904_0_, boolean p_77904_1_, boolean p_77904_2_, int p_77904_3_, int p_77904_4_, int p_77904_5_, int p_77904_6_)
    {
        int i1 = 0;

        if (p_77904_0_)
        {
            i1 = isFlagUnset(p_77904_6_, p_77904_4_);
        }
        else if (p_77904_3_ != -1)
        {
            if (p_77904_3_ == 0 && countSetFlags(p_77904_6_) == p_77904_4_)
            {
                i1 = 1;
            }
            else if (p_77904_3_ == 1 && countSetFlags(p_77904_6_) > p_77904_4_)
            {
                i1 = 1;
            }
            else if (p_77904_3_ == 2 && countSetFlags(p_77904_6_) < p_77904_4_)
            {
                i1 = 1;
            }
        }
        else
        {
            i1 = isFlagSet(p_77904_6_, p_77904_4_);
        }

        if (p_77904_1_)
        {
            i1 *= p_77904_5_;
        }

        if (p_77904_2_)
        {
            i1 *= -1;
        }

        return i1;
    }

    /**
     * Count the number of bits in an integer set to ON.
     */
    private static int countSetFlags(int par1)
    {
        int j;

        for (j = 0; par1 > 0; ++j)
        {
            par1 &= par1 - 1;
        }

        return j;
    }

    public static int parsePotionEffects(String requirementCode, int par1, int requirementCodeLength, int damageValue)
    {
        if (par1 < requirementCode.length() && requirementCodeLength >= 0 && par1 < requirementCodeLength)
        {
            int l = requirementCode.indexOf(124, par1);
            int i1;
            int j2;

            if (l >= 0 && l < requirementCodeLength)
            {
                i1 = parsePotionEffects(requirementCode, par1, l - 1, damageValue);

                if (i1 > 0)
                {
                    return i1;
                }
                else
                {
                    j2 = parsePotionEffects(requirementCode, l + 1, requirementCodeLength, damageValue);
                    return j2 > 0 ? j2 : 0;
                }
            }
            else
            {
                i1 = requirementCode.indexOf(38, par1);

                if (i1 >= 0 && i1 < requirementCodeLength)
                {
                    j2 = parsePotionEffects(requirementCode, par1, i1 - 1, damageValue);

                    if (j2 <= 0)
                    {
                        return 0;
                    }
                    else
                    {
                        int k2 = parsePotionEffects(requirementCode, i1 + 1, requirementCodeLength, damageValue);
                        return k2 <= 0 ? 0 : (j2 > k2 ? j2 : k2);
                    }
                }
                else
                {
                    boolean hasAsterisk = false;
                    boolean hasNumber = false;
                    boolean flag2 = false;
                    boolean flag3 = false;
                    boolean flag4 = false;
                    byte b0 = -1;
                    int j1 = 0;
                    int k1 = 0;
                    int l1 = 0;

                    for (int iterableInt = par1; iterableInt < requirementCodeLength; ++iterableInt)
                    {
                        char character = requirementCode.charAt(iterableInt);

                        if (character >= 48 && character <= 57) // 0 to 9
                        {
                            if (hasAsterisk)
                            {
                                k1 = character - 48;
                                hasNumber = true;
                            }
                            else
                            {
                                j1 *= 10;
                                j1 += character - 48;
                                flag2 = true;
                            }
                        }
                        else if (character == 42) // *
                        {
                            hasAsterisk = true;
                        }
                        else if (character == 33)
                        {
                            if (flag2)
                            {
                                l1 += func_77904_a(flag3, hasNumber, flag4, b0, j1, k1, damageValue);
                                flag3 = false;
                                flag4 = false;
                                hasAsterisk = false;
                                hasNumber = false;
                                flag2 = false;
                                k1 = 0;
                                j1 = 0;
                                b0 = -1;
                            }

                            flag3 = true;
                        }
                        else if (character == 45)
                        {
                            if (flag2)
                            {
                                l1 += func_77904_a(flag3, hasNumber, flag4, b0, j1, k1, damageValue);
                                flag3 = false;
                                flag4 = false;
                                hasAsterisk = false;
                                hasNumber = false;
                                flag2 = false;
                                k1 = 0;
                                j1 = 0;
                                b0 = -1;
                            }

                            flag4 = true;
                        }
                        else if (character != 61 && character != 60 && character != 62)
                        {
                            if (character == 43 && flag2)
                            {
                                l1 += func_77904_a(flag3, hasNumber, flag4, b0, j1, k1, damageValue);
                                flag3 = false;
                                flag4 = false;
                                hasAsterisk = false;
                                hasNumber = false;
                                flag2 = false;
                                k1 = 0;
                                j1 = 0;
                                b0 = -1;
                            }
                        }
                        else
                        {
                            if (flag2)
                            {
                                l1 += func_77904_a(flag3, hasNumber, flag4, b0, j1, k1, damageValue);
                                flag3 = false;
                                flag4 = false;
                                hasAsterisk = false;
                                hasNumber = false;
                                flag2 = false;
                                k1 = 0;
                                j1 = 0;
                                b0 = -1;
                            }

                            if (character == 61)
                            {
                                b0 = 0;
                            }
                            else if (character == 60)
                            {
                                b0 = 2;
                            }
                            else if (character == 62)
                            {
                                b0 = 1;
                            }
                        }
                    }

                    if (flag2)
                    {
                        l1 += func_77904_a(flag3, hasNumber, flag4, b0, j1, k1, damageValue);
                    }

                    return l1;
                }
            }
        }
        else
        {
            return 0;
        }
    }

    /**
     * Returns a list of effects for the specified potion damage value.
     */
    public static List getPotionEffects(int damageValue, boolean isInstant)
    {
        ArrayList arraylist = null;
        Potion[] potionTypes = Potion.potionTypes;
        int maxPotionAmount = potionTypes.length;

        for (int iterableInt = 0; iterableInt < maxPotionAmount; ++iterableInt)
        {
            Potion potion = potionTypes[iterableInt];

            if (potion != null && (!potion.isUsable() || isInstant))
            {
                String requirementCode = (String)potionRequirements.get(Integer.valueOf(potion.getId()));

                if (requirementCode != null)
                {
                    int l = parsePotionEffects(requirementCode, 0, requirementCode.length(), damageValue);

                    if (l > 0)
                    {
                        int i1 = 0;
                        String amplifierCode = (String)potionAmplifiers.get(Integer.valueOf(potion.getId()));

                        if (amplifierCode != null)
                        {
                            i1 = parsePotionEffects(amplifierCode, 0, amplifierCode.length(), damageValue);

                            if (i1 < 0)
                            {
                                i1 = 0;
                            }
                        }

                        if (potion.isInstant())
                        {
                            l = 1;
                        }
                        else
                        {
                            l = 1200 * (l * 3 + (l - 1) * 2);
                            l >>= i1;
                            l = (int)Math.round((double)l * potion.getEffectiveness());

                            if ((damageValue & 16384) != 0)
                            {
                                l = (int)Math.round((double)l * 0.75D + 0.5D);
                            }
                        }

                        if (arraylist == null)
                        {
                            arraylist = new ArrayList();
                        }

                        PotionEffect potioneffect = new PotionEffect(potion.getId(), l, i1);

                        if ((damageValue & 16384) != 0)
                        {
                            potioneffect.setSplashPotion(true);
                        }

                        arraylist.add(potioneffect);
                    }
                }
            }
        }

        return arraylist;
    }

    /**
     * Does bit operations for brewPotionData, given data, the index of the bit being operated upon, whether the bit
     * will be removed, whether the bit will be toggled (NOT), or whether the data field will be set to 0 if the bit is
     * not present.
     */
    private static int brewBitOperations(int id, int k, boolean hasMinus, boolean hasExclamation, boolean hasAmpersAnd)
    {
        if (hasAmpersAnd)
        {
            if (!checkFlag(id, k))
            {
                return 0;
            }
        }
        else if (hasMinus)
        {
            id &= ~(1 << k);
        }
        else if (hasExclamation)
        {
            if ((id & 1 << k) == 0)
            {
                id |= 1 << k;
            }
            else
            {
                id &= ~(1 << k);
            }
        }
        else
        {
            id |= 1 << k;
        }

        return id;
    }

    /**
     * Generate a data value for a potion, given its previous data value and the encoded string of new effects it will
     * receive
     */
    public static int applyIngredient(int id, String ingredientEffectCode)
    {
        int stringLength = ingredientEffectCode.length();
        boolean hasNumber = false;
        boolean hasExclamationMark = false;
        boolean hasMinus = false;
        boolean hasAmpersAnd = false;
        int k = 0;

        for (int iterableInt = 0; iterableInt < stringLength; ++iterableInt)
        {
            char character = ingredientEffectCode.charAt(iterableInt);

            if (character >= 48 && character <= 57) // 0 to 9
            {
                k *= 10;
                k += character - 48; // Add the digit to k
                hasNumber = true;
            }
            else if (character == 33) // !
            {
                if (hasNumber)
                {
                    id = brewBitOperations(id, k, hasMinus, hasExclamationMark, hasAmpersAnd);
                    hasAmpersAnd = false;
                    hasExclamationMark = false;
                    hasMinus = false;
                    hasNumber = false;
                    k = 0;
                }

                hasExclamationMark = true;
            }
            else if (character == 45) // -
            {
                if (hasNumber)
                {
                    id = brewBitOperations(id, k, hasMinus, hasExclamationMark, hasAmpersAnd);
                    hasAmpersAnd = false;
                    hasExclamationMark = false;
                    hasMinus = false;
                    hasNumber = false;
                    k = 0;
                }

                hasMinus = true;
            }
            else if (character == 43) // +
            {
                if (hasNumber)
                {
                    id = brewBitOperations(id, k, hasMinus, hasExclamationMark, hasAmpersAnd);
                    hasAmpersAnd = false;
                    hasExclamationMark = false;
                    hasMinus = false;
                    hasNumber = false;
                    k = 0;
                }
            }
            else if (character == 38) // &
            {
                if (hasNumber)
                {
                    id = brewBitOperations(id, k, hasMinus, hasExclamationMark, hasAmpersAnd);
                    hasAmpersAnd = false;
                    hasExclamationMark = false;
                    hasMinus = false;
                    hasNumber = false;
                    k = 0;
                }

                hasAmpersAnd = true;
            }
        }

        if (hasNumber)
        {
            id = brewBitOperations(id, k, hasMinus, hasExclamationMark, hasAmpersAnd);
        }

        return id & 32767;
    }

    public static int func_77908_a(int p_77908_0_, int p_77908_1_, int p_77908_2_, int p_77908_3_, int p_77908_4_, int p_77908_5_)
    {
        return (checkFlag(p_77908_0_, p_77908_1_) ? 16 : 0) | (checkFlag(p_77908_0_, p_77908_2_) ? 8 : 0) | (checkFlag(p_77908_0_, p_77908_3_) ? 4 : 0) | (checkFlag(p_77908_0_, p_77908_4_) ? 2 : 0) | (checkFlag(p_77908_0_, p_77908_5_) ? 1 : 0);
    }

    static
    {
        potionRequirements.put(Integer.valueOf(Potion.regeneration.getId()), "0 & !1 & !2 & !3 & 0+6");
        sugarEffect = "-0+1-2-3&4-4+13";
        potionRequirements.put(Integer.valueOf(Potion.moveSpeed.getId()), "!0 & 1 & !2 & !3 & 1+6");
        magmaCreamEffect = "+0+1-2-3&4-4+13";
        potionRequirements.put(Integer.valueOf(Potion.fireResistance.getId()), "0 & 1 & !2 & !3 & 0+6");
        speckledMelonEffect = "+0-1+2-3&4-4+13";
        potionRequirements.put(Integer.valueOf(Potion.heal.getId()), "0 & !1 & 2 & !3");
        spiderEyeEffect = "-0-1+2-3&4-4+13";
        potionRequirements.put(Integer.valueOf(Potion.poison.getId()), "!0 & !1 & 2 & !3 & 2+6");
        fermentedSpiderEyeEffect = "-0+3-4+13";
        potionRequirements.put(Integer.valueOf(Potion.weakness.getId()), "!0 & !1 & !2 & 3 & 3+6");
        potionRequirements.put(Integer.valueOf(Potion.harm.getId()), "!0 & !1 & 2 & 3");
        potionRequirements.put(Integer.valueOf(Potion.moveSlowdown.getId()), "!0 & 1 & !2 & 3 & 3+6");
        blazePowderEffect = "+0-1-2+3&4-4+13";
        potionRequirements.put(Integer.valueOf(Potion.damageBoost.getId()), "0 & !1 & !2 & 3 & 3+6");
        goldenCarrotEffect = "-0+1+2-3+13&4-4";
        potionRequirements.put(Integer.valueOf(Potion.nightVision.getId()), "!0 & 1 & 2 & !3 & 2+6");
        potionRequirements.put(Integer.valueOf(Potion.invisibility.getId()), "!0 & 1 & 2 & 3 & 2+6");
        pufferfishEffect = "+0-1+2+3+13&4-4";
        potionRequirements.put(Integer.valueOf(Potion.waterBreathing.getId()), "0 & !1 & 2 & 3 & 2+6");
        glowstoneEffect = "+5-6-7";
        potionAmplifiers.put(Integer.valueOf(Potion.moveSpeed.getId()), "5");
        potionAmplifiers.put(Integer.valueOf(Potion.digSpeed.getId()), "5");
        potionAmplifiers.put(Integer.valueOf(Potion.damageBoost.getId()), "5");
        potionAmplifiers.put(Integer.valueOf(Potion.regeneration.getId()), "5");
        potionAmplifiers.put(Integer.valueOf(Potion.harm.getId()), "5");
        potionAmplifiers.put(Integer.valueOf(Potion.heal.getId()), "5");
        potionAmplifiers.put(Integer.valueOf(Potion.resistance.getId()), "5");
        potionAmplifiers.put(Integer.valueOf(Potion.poison.getId()), "5");
        redstoneEffect = "-5+6-7";
        gunpowderEffect = "+14&13-13";
        field_77925_n = new HashMap();
        potionPrefixes = new String[] {"potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky"};
    }
}