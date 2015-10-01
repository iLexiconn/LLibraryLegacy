package net.ilexiconn.llibrary.common.enchantment;

import net.minecraft.enchantment.Enchantment;

/**
 * @author JTGhawk137
 * @since 0.2.1
 */
public class EnchantmentHelper
{
    /**
     * Removes the specified enchantment from minecraft
     * 
     * @since 0.2.1
     */
    public static void removeEnchantment(Enchantment enchantment)
    {
        Enchantment[] ench = Enchantment.enchantmentsBookList;
        for (int i = 0; i < ench.length; i++)
        {
            if (ench[i] == enchantment)
            {
                ench[i] = null;
            }
        }
    }
}
