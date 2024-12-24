package com.justin.justinmod.entity.client;

import com.justin.justinmod.JustinMod;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer HELICOPTER = new EntityModelLayer(new Identifier(JustinMod.MOD_ID, "helicopter"), "main");
    public static final EntityModelLayer HOMING_BULLET = new EntityModelLayer(new Identifier(JustinMod.MOD_ID, "homing_bullet"), "main");
    public static final EntityModelLayer GUARDIAN_LASER_AURA = new EntityModelLayer(new Identifier(JustinMod.MOD_ID, "guardian_laser_aura"), "main");
}
