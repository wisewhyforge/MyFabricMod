package com.justin.justinmod;

import com.justin.justinmod.entity.ModEntities;
import com.justin.justinmod.entity.client.model.GuardianLaserAuraModel;
import com.justin.justinmod.entity.client.model.HelicopterModel;
import com.justin.justinmod.entity.client.ModModelLayers;
import com.justin.justinmod.entity.client.model.HomingBulletEntityModel;
import com.justin.justinmod.entity.client.render.GuardianLaserAuraRenderer;
import com.justin.justinmod.entity.client.render.HelicopterRenderer;
import com.justin.justinmod.entity.client.render.HomingBulletRenderer;
import com.justin.justinmod.entity.custom.HomingBulletEntity;
import com.justin.justinmod.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class JustinModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.HELICOPTER, HelicopterModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.HELICOPTER, ctx -> new HelicopterRenderer(ctx, new HelicopterModel(ctx.getPart(ModModelLayers.HELICOPTER))));

        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.HOMING_BULLET, HomingBulletEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.HOMING_BULLET, ctx-> new HomingBulletRenderer(ctx, new HomingBulletEntityModel(ctx.getPart(ModModelLayers.HOMING_BULLET))));

        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.GUARDIAN_LASER_AURA, GuardianLaserAuraModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.GUARDIAN_LASER_AURA, ctx -> new GuardianLaserAuraRenderer(ctx, new GuardianLaserAuraModel(ctx.getPart(ModModelLayers.GUARDIAN_LASER_AURA))));

        KeyInputHandler.register();

    }

}
