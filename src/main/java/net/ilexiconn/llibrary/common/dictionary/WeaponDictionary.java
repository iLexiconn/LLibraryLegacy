package net.ilexiconn.llibrary.common.dictionary;

import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import java.util.*;

import static net.ilexiconn.llibrary.common.dictionary.WeaponDictionary.Type.*;

public class WeaponDictionary
{
    private static final int WEAPON_LIST_SIZE = 1024 * 10;
    private static WeaponInfo[] weaponList = new WeaponInfo[WEAPON_LIST_SIZE];
    private static ArrayList<Item>[] typeInfoList = new ArrayList[Type.values().length];

    static
    {
        registerVanillaWeapons();
    }

    public static boolean registerWeaponType(Item item, Type... types)
    {
        types = listSubTags(types);

        if (item != null)
        {
            for (Type type : types)
            {
                if (typeInfoList[type.ordinal()] == null)
                {
                    typeInfoList[type.ordinal()] = new ArrayList<Item>();
                }

                typeInfoList[type.ordinal()].add(item);
            }

            if (weaponList[Item.getIdFromItem(item)] == null)
            {
                weaponList[Item.getIdFromItem(item)] = new WeaponInfo(types);
            }
            else
            {
                Collections.addAll(weaponList[Item.getIdFromItem(item)].typeList, types);
            }

            return true;
        }

        return false;
    }

    /**
     * Returns a list of weapons registered with a specific type
     *
     * @param type the Type to look for
     * @return a list of weapons of the specified type, null if there are none
     */
    public static Item[] getWeaponsForType(Type type)
    {
        if (typeInfoList[type.ordinal()] != null)
        {
            return (Item[]) typeInfoList[type.ordinal()].toArray(new Item[0]);
        }

        return new Item[0];
    }

    /**
     * Gets a list of Types that a specific weapon is registered with
     *
     * @param item the weapon to check
     * @return the list of types, null if there are none
     */
    public static Type[] getTypesForWeapon(Item item)
    {
        checkRegistration(item);

        if (weaponList[Item.getIdFromItem(item)] != null)
        {
            return (Type[]) weaponList[Item.getIdFromItem(item)].typeList.toArray(new Type[0]);
        }

        return new Type[0];
    }

    /**
     * Checks to see if two weapons are registered as having the same type
     *
     * @param itemA
     * @param itemB
     * @return returns true if a common type is found, false otherwise
     */
    public static boolean areWeaponsEquivalent(Item itemA, Item itemB)
    {
        int a = Item.getIdFromItem(itemA);
        int b = Item.getIdFromItem(itemB);

        checkRegistration(itemA);
        checkRegistration(itemB);

        if (weaponList[a] != null && weaponList[b] != null)
        {
            for (Type type : weaponList[a].typeList)
            {
                if (containsType(weaponList[b], type))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isWeaponOfType(Item item, Type type)
    {
        checkRegistration(item);

        return weaponList[Item.getIdFromItem(item)] != null && containsType(weaponList[Item.getIdFromItem(item)], type);

    }

    public static boolean isWeaponRegistered(Item item)
    {
        return weaponList[Item.getIdFromItem(item)] != null;
    }

    public static boolean isWeaponRegistered(int itemID)
    {
        return weaponList[itemID] != null;
    }

    public static void makeBestGuess(Item item)
    {

    }

    private static void checkRegistration(Item item)
    {

    }

    private static boolean containsType(WeaponInfo info, Type type)
    {
        if (type.hasSubTags())
        {
            for (Type remappedType : listSubTags(type))
            {
                if (info.typeList.contains(remappedType))
                {
                    return true;
                }
            }

            return false;
        }

        return info.typeList.contains(type);
    }

    private static Type[] listSubTags(Type... types)
    {
        List<Type> subTags = Lists.newArrayList();

        for (Type type : types)
        {
            if (type.hasSubTags())
            {
                subTags.addAll(type.subTags);
            }
            else
            {
                subTags.add(type);
            }
        }

        return subTags.toArray(new Type[subTags.size()]);
    }

    private static void registerVanillaWeapons()
    {
        registerWeaponType(Items.wooden_sword, SWORD, SHARP, MELEE);
        registerWeaponType(Items.stone_sword, SWORD, SHARP, MELEE);
        registerWeaponType(Items.iron_sword, SWORD, SHARP, MELEE);
        registerWeaponType(Items.diamond_sword, SWORD, SHARP, MELEE);
        registerWeaponType(Items.golden_sword, SWORD, SHARP, MELEE);

        registerWeaponType(Items.wooden_axe, AXE, SHARP, MELEE);
        registerWeaponType(Items.stone_axe, AXE, SHARP, MELEE);
        registerWeaponType(Items.iron_axe, AXE, SHARP, MELEE);
        registerWeaponType(Items.diamond_axe, AXE, SHARP, MELEE);
        registerWeaponType(Items.golden_axe, AXE, SHARP, MELEE);

        registerWeaponType(Items.wooden_pickaxe, PICKAXE, DULL, MELEE);
        registerWeaponType(Items.stone_pickaxe, PICKAXE, DULL, MELEE);
        registerWeaponType(Items.iron_pickaxe, PICKAXE, DULL, MELEE);
        registerWeaponType(Items.diamond_pickaxe, PICKAXE, DULL, MELEE);
        registerWeaponType(Items.golden_pickaxe, PICKAXE, DULL, MELEE);

        registerWeaponType(Items.wooden_shovel, SHOVEL, DULL, MELEE);
        registerWeaponType(Items.stone_shovel, SHOVEL, DULL, MELEE);
        registerWeaponType(Items.iron_shovel, SHOVEL, DULL, MELEE);
        registerWeaponType(Items.diamond_shovel, SHOVEL, DULL, MELEE);
        registerWeaponType(Items.golden_shovel, SHOVEL, DULL, MELEE);
    }

    public enum Type
    {
        /* Generic types which a weapon can be */
        SWORD,
        AXE,
        PICKAXE,
        SHOVEL,
        NUNCHUCKS,
        SPEAR,
        WAND,
        BOW,
        HALBERD,
        GUN,
        MACE,
        HAMMER,
        SICLE,
        SCYTHE,
        KIFE,
        CLAW,
        GLOVE,
        WHIP,
        BOOMERANG,

        SHARP,
        DULL,
        LIGHT,
        HEAVY,

        RANGED,
        MELEE,
        MELEE_RANGED,
        THROWABLE,


        MAGICAL,
        EXPLOSIVE,
        FIERY,
        WATERY,
        ICY,
        ELECTRICAL,
        DEATHLY,
        PLANT_Y,
        EARTHLY,
        WINDY,
        ENDER,
        POISONED,

        FUTURISTIC,
        MEDIEVAL,
        MODERN;

        private List<Type> subTags;

        private Type(Type... subTags)
        {
            this.subTags = Arrays.asList(subTags);
        }

        /**
         * Retrieves a Type value by name,
         * if one does not exist already it creates one.
         * This can be used as interm measure for modders to
         * add there own category of Biome.
         * <p/>
         * There are NO naming conventions besides:
         * MUST be all upper case (enforced by name.toUpper())
         * NO Special characters. {Unenforced, just don't be a pain, if it becomes a issue I WILL
         * make this RTE with no worry about backwards compatibility}
         * <p/>
         * Note: For performance sake, the return value of this function SHOULD be cached.
         * Two calls with the same name SHOULD return the same value.
         *
         * @param name The name of this Type
         * @return An instance of Type for this name.
         */
        public static Type getType(String name, Type... subTypes)
        {
            name = name.toUpperCase();

            for (Type t : values())
            {
                if (t.name().equals(name))
                {
                    return t;
                }
            }

            Type ret = EnumHelper.addEnum(Type.class, name, new Class[]{Type[].class}, new Object[]{subTypes});

            if (ret.ordinal() >= typeInfoList.length)
            {
                typeInfoList = Arrays.copyOf(typeInfoList, ret.ordinal());
            }
            return ret;
        }

        private boolean hasSubTags()
        {
            return subTags != null && !subTags.isEmpty();
        }
    }

    private static class WeaponInfo
    {
        public EnumSet<Type> typeList;

        public WeaponInfo(Type[] types)
        {
            typeList = EnumSet.noneOf(Type.class);

            Collections.addAll(typeList, types);
        }
    }
}
