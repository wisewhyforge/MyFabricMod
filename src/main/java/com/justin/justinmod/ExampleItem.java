package com.justin.justinmod;

import com.justin.justinmod.entity.custom.HomingBulletEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ExampleItem extends Item {

    private static final Predicate<LivingEntity> CAN_ATTACK_PREDICATE = entity -> entity.isMobOrPlayer();
    private static final TargetPredicate HEAD_TARGET_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance(50.0).setPredicate(CAN_ATTACK_PREDICATE);


    public ExampleItem(Settings settings) {
        super(settings);
    }

    public LivingEntity getLookingAtEntity(World world, PlayerEntity user) {
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

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(Hand.OFF_HAND);
        user.getStackInHand(hand);
        LivingEntity target = getLookingAtEntity(world, user);

        if (target == null) {
            return TypedActionResult.fail(itemStack);
        }

        //HomingBulletEntity bullet = new HomingBulletEntity(world, user, target, Direction.Axis.X);


        /*boolean spawned = world.spawnEntity(bullet) || world.spawnEntity(new LightningEntity(EntityType.LIGHTNING_BOLT, world));
        if (!spawned) {
            return TypedActionResult.fail(itemStack);
        }*/
        user.sendMessage(Text.literal("Targeting " + target.getClass().getName()), true);
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
