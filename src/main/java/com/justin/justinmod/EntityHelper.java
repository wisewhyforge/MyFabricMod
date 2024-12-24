package com.justin.justinmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EntityHelper {

    public static final Predicate<LivingEntity> CAN_ATTACK_PREDICATE = entity -> entity.isMobOrPlayer();
    public static final TargetPredicate HEAD_TARGET_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance(50.0).setPredicate(CAN_ATTACK_PREDICATE);


    public static LivingEntity getPlayerLookingAtEntity(World world, PlayerEntity user) {
        LivingEntity target;

        List<LivingEntity> entityList = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), user.getBoundingBox().expand(50), Entity::isAlive);

        Vec3d eyePos = user.getEyePos();
        Vec3d playerDirection = Vec3d.fromPolar(user.getPitch(), user.getYaw());
        Vec3d playerPosition = user.getPos();
        //JustinMod.LOGGER.info("Looking at " + playerDirection);

        List<LivingEntity> livingEntitiesInSight = new ArrayList<>();

        for (LivingEntity entity : entityList) {

            // calculate the direction of the entity with respect to the player
            Vec3d entityDirection = entity.getPos().subtract(eyePos).normalize();

            // calculate the angle between the direction of the player and the direction of the entity
            double angleBetweenEntityPlayer = Math.acos(playerDirection.dotProduct(entityDirection) / (playerDirection.length() * entityDirection.length())) ;
            // Convert to degrees from radians
            double angle = angleBetweenEntityPlayer * 180 / Math.PI;
            //JustinMod.LOGGER.info("Angle Between " + entity.getName() + " and Player in degrees " + angle);

            // add the entity to the list if it is in the player's cone of vision
            if ((angle) <= 10) {
                livingEntitiesInSight.add(entity);

            }
        }

        if (livingEntitiesInSight.isEmpty()) {
            return null;
        }

        target = world.getClosestEntity(livingEntitiesInSight, HEAD_TARGET_PREDICATE, user, user.getX(), user.getY(), user.getZ());

        return target;
    }


    public static LivingEntity getLivingEntityLookingAtEntity(World world, LivingEntity user) {
        LivingEntity target;

        List<LivingEntity> entityList = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), user.getBoundingBox().expand(50), Entity::isAlive);

        Vec3d eyePos = user.getEyePos();
        Vec3d playerDirection = Vec3d.fromPolar(user.getPitch(), user.getYaw());
        Vec3d playerPosition = user.getPos();
        //JustinMod.LOGGER.info("Looking at " + playerDirection);

        List<LivingEntity> livingEntitiesInSight = new ArrayList<>();

        for (LivingEntity entity : entityList) {

            // calculate the direction of the entity with respect to the player
            Vec3d entityDirection = entity.getPos().subtract(eyePos).normalize();

            // calculate the angle between the direction of the player and the direction of the entity
            double angleBetweenEntityPlayer = Math.acos(playerDirection.dotProduct(entityDirection) / (playerDirection.length() * entityDirection.length())) ;
            // Convert to degrees from radians
            double angle = angleBetweenEntityPlayer * 180 / Math.PI;
            //JustinMod.LOGGER.info("Angle Between " + entity.getName() + " and Player in degrees " + angle);

            // add the entity to the list if it is in the player's cone of vision
            if ((angle) <= 10) {
                livingEntitiesInSight.add(entity);

            }
        }

        if (livingEntitiesInSight.isEmpty()) {
            return null;
        }

        target = world.getClosestEntity(livingEntitiesInSight, HEAD_TARGET_PREDICATE, user, user.getX(), user.getY(), user.getZ());

        return target;
    }
}
