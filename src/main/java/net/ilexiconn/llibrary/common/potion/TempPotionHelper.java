package net.ilexiconn.llibrary.common.potion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.*;

public class TempPotionHelper
{
    public static final String sugarEffect;
    public static final String ghastTearEffect;
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
    public static final String netherWartEffect;
    private static final HashMap<Integer, String> potionRequirements = new HashMap<Integer, String>();
    /**
     * Potion effect amplifier map
     */
    private static final HashMap<Integer, String> potionAmplifiers = new HashMap<Integer, String>();
    private static final HashMap<Integer, Integer> cachedLiquidColors;
    /**
     * An array of possible potion prefix names, as translation IDs.
     */
    private static final String[] potionPrefixes;
    // We don't care about: private static final String __OBFID = "CL_00000078";

    private static final int EQUAL = 0;
    private static final int GREATER = 1;
    private static final int LESS = 2;

    static
    {
        potionRequirements.put(Potion.regeneration.getId(), "0 & !1 & !2 & !3 & 0+6");
        sugarEffect = "-0+1-2-3&4-4+13";
        potionRequirements.put(Potion.moveSpeed.getId(), "!0 & 1 & !2 & !3 & 1+6");
        magmaCreamEffect = "+0+1-2-3&4-4+13";
        potionRequirements.put(Potion.fireResistance.getId(), "0 & 1 & !2 & !3 & 0+6");
        speckledMelonEffect = "+0-1+2-3&4-4+13";
        potionRequirements.put(Potion.heal.getId(), "0 & !1 & 2 & !3");
        spiderEyeEffect = "-0-1+2-3&4-4+13";
        potionRequirements.put(Potion.poison.getId(), "!0 & !1 & 2 & !3 & 2+6");
        fermentedSpiderEyeEffect = "-0+3-4+13";
        potionRequirements.put(Potion.weakness.getId(), "!0 & !1 & !2 & 3 & 3+6");
        potionRequirements.put(Potion.harm.getId(), "!0 & !1 & 2 & 3");
        potionRequirements.put(Potion.moveSlowdown.getId(), "!0 & 1 & !2 & 3 & 3+6");
        blazePowderEffect = "+0-1-2+3&4-4+13";
        potionRequirements.put(Potion.damageBoost.getId(), "0 & !1 & !2 & 3 & 3+6");
        goldenCarrotEffect = "-0+1+2-3+13&4-4";
        potionRequirements.put(Potion.nightVision.getId(), "!0 & 1 & 2 & !3 & 2+6");
        potionRequirements.put(Potion.invisibility.getId(), "!0 & 1 & 2 & 3 & 2+6");
        pufferfishEffect = "+0-1+2+3+13&4-4";
        potionRequirements.put(Potion.waterBreathing.getId(), "0 & !1 & 2 & 3 & 2+6");
        glowstoneEffect = "+5-6-7";
        potionAmplifiers.put(Potion.moveSpeed.getId(), "5");
        potionAmplifiers.put(Potion.digSpeed.getId(), "5");
        potionAmplifiers.put(Potion.damageBoost.getId(), "5");
        potionAmplifiers.put(Potion.regeneration.getId(), "5");
        potionAmplifiers.put(Potion.harm.getId(), "5");
        potionAmplifiers.put(Potion.heal.getId(), "5");
        potionAmplifiers.put(Potion.resistance.getId(), "5");
        potionAmplifiers.put(Potion.poison.getId(), "5");
        redstoneEffect = "-5+6-7";
        gunpowderEffect = "+14&13-13";
        ghastTearEffect = "+0-1-2-3&4-4+13";

        netherWartEffect = "+4";

        cachedLiquidColors = Maps.newHashMap();
        potionPrefixes = new String[]{"potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin",
                "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick",
                "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul",
                "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky"};
    }

    /**
     * Is the bit given set to 1?
     */
    public static boolean checkFlag(int valueToCheck, int bitIndex)
    {
        return (valueToCheck & 1 << bitIndex) != 0;
    }

    /**
     * Returns 1 if the flag is set, 0 if it is not set.
     */
    private static int isFlagSet(int valueToCheck, int bitIndex)
    {
        /**
         * Is the bit given set to 1?
         */
        return checkFlag(valueToCheck, bitIndex) ? 1 : 0;
    }

    /**
     * Returns 0 if the flag is set, 1 if it is not set.
     */
    private static int isFlagUnset(int valueToCheck, int bitIndex)
    {
        /**
         * Is the bit given set to 1?
         */
        return checkFlag(valueToCheck, bitIndex) ? 0 : 1;
    }

    public static int func_77909_a(int p_77909_0_)
    {
        return func_77908_a(p_77909_0_, 5, 4, 3, 2, 1);
    }

    /**
     * Given a {@link Collection}<{@link PotionEffect}> will return an Integer color.
     */
    public static int calcPotionLiquidColor(Collection<PotionEffect> potionEffects)
    {
        int i = 3694022;

        if (potionEffects != null && !potionEffects.isEmpty())
        {
            float red = 0f;
            float green = 0f;
            float blue = 0f;
            float invertedAlpha = 0f;

            for (PotionEffect potioneffect : potionEffects)
            {
                int color = Potion.potionTypes[potioneffect.getPotionID()].getLiquidColor();

                for (int k = 0; k <= potioneffect.getAmplifier(); ++k)
                {
                    red += (float) (color >> 16 & 255) / 255f;
                    green += (float) (color >> 8 & 255) / 255f;
                    blue += (float) (color >> 0 & 255) / 255f;
                    ++invertedAlpha;
                }
            }

            red = red / invertedAlpha * 255f;
            green = green / invertedAlpha * 255f;
            blue = blue / invertedAlpha * 255f;
            return (int) red << 16 | (int) green << 8 | (int) blue;
        }
        else
        {
            return i;
        }
    }

    public static boolean areValidBeaconEffects(Collection<PotionEffect> potionEffects)
    {
        Iterator<PotionEffect> iterator = potionEffects.iterator();
        PotionEffect potioneffect;

        do
        {
            if (!iterator.hasNext())
            {
                return true;
            }

            potioneffect = iterator.next();
        }
        while (potioneffect.getIsAmbient());

        return false;
    }

    @SideOnly(Side.CLIENT)
    public static int getPotionLiquidColor(int damageValue, boolean isInstant)
    {
        /**
         * Given a {@link java.util.Collection}<{@link net.minecraft.potion.PotionEffect}> will return an Integer color.
         */
        if (!isInstant)
        {
            if (cachedLiquidColors.containsKey(damageValue))
            {
                return cachedLiquidColors.get(damageValue);
            }
            else
            {
                int color = calcPotionLiquidColor(getPotionEffects(damageValue, false));
                cachedLiquidColors.put(damageValue, color);
                return color;
            }
        }
        else
            return calcPotionLiquidColor(getPotionEffects(damageValue, true));
    }

    public static String func_77905_c(int p_77905_0_)
    {
        int j = func_77909_a(p_77905_0_);
        return potionPrefixes[j];
    }

    private static int performBitOperations(boolean checkUnset, boolean hasNumbers, boolean subtraction, int equalityType, int flagIndex, int p_77904_5_, int value)
    {
        int result = 0;

        if (checkUnset)
        {
            result = isFlagUnset(value, flagIndex);
        }
        else if (equalityType != -1)
        {
            if (equalityType == EQUAL && countSetFlags(value) == flagIndex)
            {
                result = 1;
            }
            else if (equalityType == GREATER && countSetFlags(value) > flagIndex)
            {
                result = 1;
            }
            else if (equalityType == LESS && countSetFlags(value) < flagIndex)
            {
                result = 1;
            }
        }
        else
        {
            result = isFlagSet(value, flagIndex);
        }

        if (hasNumbers)
        {
            result *= p_77904_5_;
        }

        if (subtraction)
        {
            result *= -1;
        }

        return result;
    }

    /**
     * Count the number of bits in an integer set to ON.
     */
    private static int countSetFlags(int par1)
    {
        int result;

        for (result = 0; par1 > 0; ++result)
        {
            par1 &= par1 - 1;
        }

        return result;
    }

    public static int parsePotionEffects(String requirementCode, int par1, int requirementCodeLength, int damageValue)
    {
        if (par1 < requirementCode.length() && requirementCodeLength >= 0 && par1 < requirementCodeLength)
        {
            int indexOfOR = requirementCode.indexOf('|', par1);
            int indexOfAND;
            int j2;

            if (indexOfOR >= 0 && indexOfOR < requirementCodeLength)
            {
                indexOfAND = parsePotionEffects(requirementCode, par1, indexOfOR - 1, damageValue);

                if (indexOfAND > 0)
                {
                    return indexOfAND;
                }
                else
                {
                    j2 = parsePotionEffects(requirementCode, indexOfOR + 1, requirementCodeLength, damageValue);
                    return j2 > 0 ? j2 : 0;
                }
            }
            else
            {
                indexOfAND = requirementCode.indexOf('&', par1);

                if (indexOfAND >= 0 && indexOfAND < requirementCodeLength)
                {
                    j2 = parsePotionEffects(requirementCode, par1, indexOfAND - 1, damageValue);

                    if (j2 <= 0)
                    {
                        return 0;
                    }
                    else
                    {
                        int k2 = parsePotionEffects(requirementCode, indexOfAND + 1, requirementCodeLength, damageValue);
                        return k2 <= 0 ? 0 : (j2 > k2 ? j2 : k2);
                    }
                }
                else
                {
                    boolean hasAsterisk = false;
                    boolean hasNumber = false;
                    boolean parsingNumber = false;
                    boolean checkUnset = false;
                    boolean isSubtraction = false;
                    byte equalityType = -1;
                    int flagIndex = 0;
                    int multiplier = 0;
                    int result = 0;

                    for (int iterableInt = par1; iterableInt < requirementCodeLength; ++iterableInt)
                    {
                        char character = requirementCode.charAt(iterableInt);

                        if (character >= '0' && character <= '9') // 0 to 9
                        {
                            if (hasAsterisk)
                            {
                                multiplier = character - '0';
                                hasNumber = true;
                            }
                            else
                            {
                                flagIndex *= 10;
                                flagIndex += character - '0';
                                parsingNumber = true;
                            }
                        }
                        else if (character == '*') // *
                        {
                            hasAsterisk = true;
                        }
                        else if (character == '!')
                        {
                            if (parsingNumber)
                            {
                                result += performBitOperations(checkUnset, hasNumber, isSubtraction, equalityType, flagIndex, multiplier, damageValue);
                                checkUnset = false;
                                isSubtraction = false;
                                hasAsterisk = false;
                                hasNumber = false;
                                parsingNumber = false;
                                multiplier = 0;
                                flagIndex = 0;
                                equalityType = -1;
                            }

                            checkUnset = true;
                        }
                        else if (character == '-')
                        {
                            if (parsingNumber)
                            {
                                result += performBitOperations(checkUnset, hasNumber, isSubtraction, equalityType, flagIndex, multiplier, damageValue);
                                checkUnset = false;
                                isSubtraction = false;
                                hasAsterisk = false;
                                hasNumber = false;
                                parsingNumber = false;
                                multiplier = 0;
                                flagIndex = 0;
                                equalityType = -1;
                            }

                            isSubtraction = true;
                        }
                        else if (character != '=' && character != '<' && character != '>')
                        {
                            if (character == '+' && parsingNumber)
                            {
                                result += performBitOperations(checkUnset, hasNumber, isSubtraction, equalityType, flagIndex, multiplier, damageValue);
                                checkUnset = false;
                                isSubtraction = false;
                                hasAsterisk = false;
                                hasNumber = false;
                                parsingNumber = false;
                                multiplier = 0;
                                flagIndex = 0;
                                equalityType = -1;
                            }
                        }
                        else
                        {
                            if (parsingNumber)
                            {
                                result += performBitOperations(checkUnset, hasNumber, isSubtraction, equalityType, flagIndex, multiplier, damageValue);
                                checkUnset = false;
                                isSubtraction = false;
                                hasAsterisk = false;
                                hasNumber = false;
                                parsingNumber = false;
                                multiplier = 0;
                                flagIndex = 0;
                                equalityType = -1;
                            }

                            if (character == '=')
                            {
                                equalityType = EQUAL;
                            }
                            else if (character == '<')
                            {
                                equalityType = LESS;
                            }
                            else if (character == '>')
                            {
                                equalityType = GREATER;
                            }
                        }
                    }

                    if (parsingNumber)
                    {
                        result += performBitOperations(checkUnset, hasNumber, isSubtraction, equalityType, flagIndex, multiplier, damageValue);
                    }

                    return result;
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
    public static List<PotionEffect> getPotionEffects(int damageValue, boolean isInstant)
    {
        ArrayList<PotionEffect> arraylist = null;
        Potion[] potionTypes = Potion.potionTypes;

        for (Potion potion : potionTypes)
        {
            if (potion != null && (!potion.isUsable() || isInstant))
            {
                String requirementCode = potionRequirements.get(potion.getId());

                if (requirementCode != null)
                {
                    int duration = parsePotionEffects(requirementCode, 0, requirementCode.length(), damageValue);

                    if (duration > 0)
                    {
                        int amplifier = 0;
                        String amplifierCode = potionAmplifiers.get(potion.getId());

                        if (amplifierCode != null)
                        {
                            amplifier = parsePotionEffects(amplifierCode, 0, amplifierCode.length(), damageValue);

                            if (amplifier < 0)
                            {
                                amplifier = 0;
                            }
                        }

                        if (potion.isInstant())
                        {
                            duration = 1;
                        }
                        else
                        {
                            duration = 1200 * (duration * 3 + (duration - 1) * 2);
                            duration >>= amplifier;
                            duration = (int) Math.round((double) duration * potion.getEffectiveness());

                            if ((damageValue & 0x4000) != 0)
                            {
                                duration = (int) Math.round((double) duration * 0.75D + 0.5D);
                            }
                        }

                        if (arraylist == null)
                        {
                            arraylist = Lists.newArrayList();
                        }

                        PotionEffect potioneffect = new PotionEffect(potion.getId(), duration, amplifier);

                        if ((damageValue & 0x4000) != 0)
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
    private static int brewBitOperations(int id, int flagIndex, boolean hasMinus, boolean hasExclamation, boolean hasAmpersAnd)
    {
        if (hasAmpersAnd)
        {
            if (!checkFlag(id, flagIndex))
            {
                return 0;
            }
        }
        else if (hasMinus)
        {
            id &= ~(1 << flagIndex);
        }
        else if (hasExclamation)
        {
            if ((id & 1 << flagIndex) == 0)
            {
                id |= 1 << flagIndex;
            }
            else
            {
                id &= ~(1 << flagIndex);
            }
        }
        else
        {
            id |= 1 << flagIndex;
        }

        return id;
    }

    /**
     * Generate a data value for a potion, given its previous data value and the encoded string of new effects it will
     * receive
     */
    public static int applyIngredient(int previousDamage, String ingredientEffectCode)
    {
        int stringLength = ingredientEffectCode.length();
        boolean hasNumber = false;
        boolean hasExclamationMark = false;
        boolean hasMinus = false;
        boolean hasAmpersAnd = false;
        int currentNumber = 0;

        for (int index = 0; index < stringLength; ++index)
        {
            char character = ingredientEffectCode.charAt(index);

            if (character >= '0' && character <= '9') // 0 to 9
            {
                currentNumber *= 10;
                currentNumber += character - '0'; // Add the digit to k
                hasNumber = true;
            }
            else if (character == '!') // !
            {
                if (hasNumber)
                {
                    previousDamage = brewBitOperations(previousDamage, currentNumber, hasMinus, hasExclamationMark, hasAmpersAnd);
                    hasAmpersAnd = false;
                    hasExclamationMark = false;
                    hasMinus = false;
                    hasNumber = false;
                    currentNumber = 0;
                }

                hasExclamationMark = true;
            }
            else if (character == '-') // -
            {
                if (hasNumber)
                {
                    previousDamage = brewBitOperations(previousDamage, currentNumber, hasMinus, hasExclamationMark, hasAmpersAnd);
                    hasAmpersAnd = false;
                    hasExclamationMark = false;
                    hasMinus = false;
                    hasNumber = false;
                    currentNumber = 0;
                }

                hasMinus = true;
            }
            else if (character == '+') // +
            {
                if (hasNumber)
                {
                    previousDamage = brewBitOperations(previousDamage, currentNumber, hasMinus, hasExclamationMark, hasAmpersAnd);
                    hasAmpersAnd = false;
                    hasExclamationMark = false;
                    hasMinus = false;
                    hasNumber = false;
                    currentNumber = 0;
                }
            }
            else if (character == '&') // &
            {
                if (hasNumber)
                {
                    previousDamage = brewBitOperations(previousDamage, currentNumber, hasMinus, hasExclamationMark, hasAmpersAnd);
                    hasAmpersAnd = false;
                    hasExclamationMark = false;
                    hasMinus = false;
                    hasNumber = false;
                    currentNumber = 0;
                }

                hasAmpersAnd = true;
            }
        }

        if (hasNumber)
        {
            previousDamage = brewBitOperations(previousDamage, currentNumber, hasMinus, hasExclamationMark, hasAmpersAnd);
        }

        return previousDamage & 0x7FFF; // Set id to positive if needed
    }

    public static int func_77908_a(int value, int p_77908_1_, int p_77908_2_, int p_77908_3_, int p_77908_4_, int p_77908_5_)
    {
        return (checkFlag(value, p_77908_1_) ? 16 : 0) | (checkFlag(value, p_77908_2_) ? 8 : 0) | (checkFlag(value, p_77908_3_) ? 4 : 0) | (checkFlag(value, p_77908_4_) ? 2 : 0) | (checkFlag(value, p_77908_5_) ? 1 : 0);
    }
}