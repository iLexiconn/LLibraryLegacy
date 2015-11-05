package net.ilexiconn.llibrary.common.particle;

import net.minecraft.world.World;

public enum ParticleHelper {
    EXPLOSION_NORMAL("explode"),
    EXPLOSION_LARGE("largeexplode"),
    EXPLOSION_HUGE("hugeexplosion"),
    FIREWORKS_SPARK("fireworksSpark"),
    WATER_BUBBLE("bubble"),
    WATER_SPLASH("splash"),
    WATER_WAKE("wake"),
    SUSPENDED("suspended"),
    SUSPENDED_DEPTH("depthsuspend"),
    CRIT("crit"),
    CRIT_MAGIC("magicCrit"),
    SMOKE_NORMAL("smoke"),
    SMOKE_LARGE("largesmoke"),
    SPELL("spell"),
    SPELL_INSTANT("instantSpell"),
    SPELL_MOB("mobSpell"),
    SPELL_MOB_AMBIENT("mobSpellAmbient"),
    SPELL_WITCH("witchMagic"),
    DRIP_WATER("dripWater"),
    DRIP_LAVA("dripLava"),
    VILLAGER_ANGRY("angryVillager"),
    VILLAGER_HAPPY("happyVillager"),
    TOWN_AURA("townaura"),
    NOTE("note"),
    PORTAL("portal"),
    ENCHANTMENT_TABLE("enchantmenttable"),
    FLAME("flame"),
    LAVA("lava"),
    FOOTSTEP("footstep"),
    CLOUD("cloud"),
    REDSTONE("reddust"),
    SNOWBALL("snowballpoof"),
    SNOW_SHOVEL("snowshovel"),
    SLIME("slime"),
    HEART("heart");

    private String particleName;

    private ParticleHelper(String name) {
        particleName = name;
    }

    public String getParticleName() {
        return particleName;
    }

    public static void spawnParticle(World world, ParticleHelper particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        world.spawnParticle(particle.getParticleName(), x, y, z, velocityX, velocityY, velocityZ);
    }
}
