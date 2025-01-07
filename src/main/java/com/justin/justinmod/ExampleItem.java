package com.justin.justinmod;

import com.justin.justinmod.entity.ModEntities;
import com.justin.justinmod.entity.custom.GuardianLaserAuraEntity;
import com.justin.justinmod.entity.custom.HomingBulletEntity;
import com.justin.justinmod.entity.custom.OrbitalLaserAuraEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ExampleItem extends Item {


    GuardianLaserAuraEntity laser;


    public ExampleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        LivingEntity target = EntityHelper.getPlayerLookingAtEntity(world, user);

        if (target == null) {
            //return TypedActionResult.fail(itemStack);
            return ItemUsage.consumeHeldItem(world, user, hand);
        }

        HomingBulletEntity bullet = new HomingBulletEntity(world, user, target, Direction.Axis.X);


        if (!world.isClient()) {
            OrbitalLaserAuraEntity orbitalLaserAuraEntity = new OrbitalLaserAuraEntity(world, target);
            JustinMod.LOGGER.info(String.valueOf(world.spawnEntity(orbitalLaserAuraEntity)));

            if (laser == null) {

                laser = new GuardianLaserAuraEntity(world, user);
                laser.setBeamTarget(target.getId());
                boolean spawned = world.spawnEntity(laser);
                JustinMod.LOGGER.info(world.isClient() + " " + spawned + " Laser Entity Spawned At: " + laser.getX() + " " + laser.getY() + " " + laser.getZ());
                if (!spawned) {
                    //return TypedActionResult.fail(itemStack);
                }
            }
        }

        user.sendMessage(Text.literal("Targeting " + target.getClass().getName()), true);
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!world.isClient() && laser != null) {
            laser.discard();
            laser = null;
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }


}
