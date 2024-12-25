package com.justin.justinmod.entity.client.render;

import com.justin.justinmod.JustinMod;
import com.justin.justinmod.entity.client.ModModelLayers;
import com.justin.justinmod.entity.client.model.HomingBulletEntityModel;
import com.justin.justinmod.entity.custom.HomingBulletEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ShulkerBulletEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class HomingBulletRenderer extends EntityRenderer<HomingBulletEntity> {
    private static final Identifier TEXTURE = new Identifier(JustinMod.MOD_ID, "textures/entity/spark.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE);
    private final HomingBulletEntityModel<HomingBulletEntity> model;

    public HomingBulletRenderer(EntityRendererFactory.Context context, HomingBulletEntityModel<HomingBulletEntity> m) {
        super(context);
        this.model = m;
    }

    protected int getBlockLight(HomingBulletEntity homingBulletEntity, BlockPos blockPos) {
        return 15;
    }

    public void render(HomingBulletEntity homingBulletEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        float h = MathHelper.lerpAngleDegrees(tickDelta, homingBulletEntity.prevYaw, homingBulletEntity.getYaw());
        float j = MathHelper.lerp(tickDelta, homingBulletEntity.prevPitch, homingBulletEntity.getPitch());
        float k = (float)homingBulletEntity.age + tickDelta;
        matrixStack.translate(0.0F, 0.15F, 0.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.sin(k * 0.1F) * 180.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.cos(k * 0.1F) * 180.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(k * 0.15F) * 360.0F));
        matrixStack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setAngles(homingBulletEntity, 0.0F, 0.0F, 0.0F, h, j);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(LAYER);
        this.model.render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 0.15F);
        matrixStack.pop();
        super.render(homingBulletEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(HomingBulletEntity homingBulletEntity) {
        return TEXTURE;
    }
}
