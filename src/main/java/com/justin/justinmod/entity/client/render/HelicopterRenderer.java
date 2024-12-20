package com.justin.justinmod.entity.client.render;

import com.justin.justinmod.JustinMod;
import com.justin.justinmod.entity.client.ModModelLayers;
import com.justin.justinmod.entity.custom.HelicopterEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;


public class HelicopterRenderer<T extends HelicopterEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    private static final Identifier TEXTURE = new Identifier(JustinMod.MOD_ID, "textures/entity/helicopter.png");

    private M model;

    public HelicopterRenderer(EntityRendererFactory.Context ctx, M model) {
        super(ctx);
        this.model = model;
    }


    @Override
    public Identifier getTexture(HelicopterEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - yaw));
        // Get the appropriate render layer (texture, outline, translucent, etc.)
        RenderLayer renderLayer = this.getRenderLayer(entity);
        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
            //int overlay = getOverlay(livingEntity, this.getAnimationCounter(livingEntity, g));  // Get overlay for entity (like hurt or death effects)
            this.model.render(matrices, vertexConsumer, light, 0, 1.0F, 1.0F, 1.0F, 1.0F);  // Render the entity model with appropriate effects
        }
        matrices.scale(1.0F,1.0F, 1.0F);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumerProvider, light);
    }

    // Determines the appropriate render layer (texture, outline, etc.) for the entity
    @Nullable
    protected RenderLayer getRenderLayer(T entity) {
        Identifier identifier = this.getTexture(entity);

        return this.model.getLayer(identifier);  // Regular body render layer

    }
}
