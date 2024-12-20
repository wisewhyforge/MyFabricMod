package com.justin.justinmod.entity;

import com.justin.justinmod.JustinMod;
import com.justin.justinmod.entity.custom.HelicopterEntity;
import com.justin.justinmod.entity.custom.HomingBulletEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<HelicopterEntity> HELICOPTER = Registry.register(Registries.ENTITY_TYPE, new Identifier(JustinMod.MOD_ID, "helicopter"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HelicopterEntity::new).build());
    public static final EntityType<HomingBulletEntity> HOMING_BULLET = Registry.register(Registries.ENTITY_TYPE, new Identifier(JustinMod.MOD_ID, "homing_bullet"),
            FabricEntityTypeBuilder.<HomingBulletEntity>create(SpawnGroup.MISC, HomingBulletEntity::new).build());

}
