package com.justin.justinmod;


import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

public class ShockwaveApplier {

    public static Shockwave createShockwave(
            @Nullable Entity entity,
            @Nullable DamageSource damageSource,
            @Nullable ExplosionBehavior behavior,
            double x,
            double y,
            double z,
            float power,
            boolean createFire,
            World.ExplosionSourceType explosionSourceType,
            boolean particles) {
        World world = entity.getWorld();

        Shockwave shockwave = new Shockwave(world, entity, damageSource, behavior, x, y, z, power, createFire, Explosion.DestructionType.KEEP);
        shockwave.collectBlocksAndDamageEntities();
        shockwave.affectWorld(particles);
        return shockwave;
    }

}

