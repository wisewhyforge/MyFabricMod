package com.justin.justinmod.mixin;

import com.justin.justinmod.ExampleItem;
import com.justin.justinmod.JustinMod;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    private static final RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(JustinMod.JUSTIN_EXPLOSION_BEAM_TEXTURE);

    @Inject(at=@At(value = "HEAD"), method = "renderFirstPersonItem")
    private void init(CallbackInfo info, @Local AbstractClientPlayerEntity playerEntity, @Local(ordinal = 0) float tickDelta, @Local(ordinal = 1) float pitch, @Local Hand hand, @Local(ordinal = 2) float swingProgress, @Local ItemStack itemStack, @Local(ordinal = 3) float equipProgress, @Local MatrixStack matrixStack, @Local VertexConsumerProvider vertexConsumerProvider, @Local int light) {
        ExampleItem heldItem = playerEntity.getOffHandStack().getItem() instanceof ExampleItem ? (ExampleItem) playerEntity.getOffHandStack().getItem() : null;

        //JustinMod.LOGGER.info("Held Item: " + heldItem);
        if (heldItem == null) return;
        LivingEntity livingEntity = heldItem.getLookingAtEntity(playerEntity.getWorld(), playerEntity);
        if (livingEntity != null && playerEntity.isUsingItem()) {
            float beamProgress = 1.0F;
            float currentBeamProgress = 50 + tickDelta;
            float k = currentBeamProgress * 0.5F % 1.0F;
            float l = playerEntity.getStandingEyeHeight();
            matrixStack.push();
            matrixStack.translate(0.0F, l, 0.0F);
            Vec3d vec3d = this.fromLerpedPosition(livingEntity, (double)livingEntity.getHeight() * 0.5, tickDelta);
            Vec3d vec3d2 = this.fromLerpedPosition(playerEntity, (double)l, tickDelta);
            Vec3d vec3d3 = vec3d.subtract(vec3d2);
            float m = (float)(vec3d3.length() + 1.0);
            vec3d3 = vec3d3.normalize();
            float n = (float)Math.acos(vec3d3.y);
            float o = (float)Math.atan2(vec3d3.z, vec3d3.x);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float) (Math.PI / 2) - o) * (180.0F / (float)Math.PI)));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n * (180.0F / (float)Math.PI)));
            int p = 1;
            float q = currentBeamProgress * 0.05F * -1.5F;
            float r = beamProgress * beamProgress;
            int s = 64 + (int)(r * 191.0F);
            int t = 32 + (int)(r * 191.0F);
            int u = 128 - (int)(r * 64.0F);
            float v = 0.2F;
            float w = 0.282F;
            float x = MathHelper.cos(q + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
            float y = MathHelper.sin(q + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
            float z = MathHelper.cos(q + (float) (Math.PI / 4)) * 0.282F;
            float aa = MathHelper.sin(q + (float) (Math.PI / 4)) * 0.282F;
            float ab = MathHelper.cos(q + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
            float ac = MathHelper.sin(q + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
            float ad = MathHelper.cos(q + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
            float ae = MathHelper.sin(q + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
            float af = MathHelper.cos(q + (float) Math.PI) * 0.2F;
            float ag = MathHelper.sin(q + (float) Math.PI) * 0.2F;
            float ah = MathHelper.cos(q + 0.0F) * 0.2F;
            float ai = MathHelper.sin(q + 0.0F) * 0.2F;
            float aj = MathHelper.cos(q + (float) (Math.PI / 2)) * 0.2F;
            float ak = MathHelper.sin(q + (float) (Math.PI / 2)) * 0.2F;
            float al = MathHelper.cos(q + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
            float am = MathHelper.sin(q + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
            float ao = 0.0F;
            float ap = 0.4999F;
            float aq = -1.0F + k;
            float ar = m * 2.5F + aq;
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
            MatrixStack.Entry entry = matrixStack.peek();
            Matrix4f matrix4f = entry.getPositionMatrix();
            Matrix3f matrix3f = entry.getNormalMatrix();
            vertex(vertexConsumer, matrix4f, matrix3f, af, m, ag, s, t, u, 0.4999F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, af, 0.0F, ag, s, t, u, 0.4999F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, 0.0F, ai, s, t, u, 0.0F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, m, ai, s, t, u, 0.0F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, m, ak, s, t, u, 0.4999F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, 0.0F, ak, s, t, u, 0.4999F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, al, 0.0F, am, s, t, u, 0.0F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, al, m, am, s, t, u, 0.0F, ar);
            float as = 0.0F;
            if (playerEntity.age % 2 == 0) {
                as = 0.5F;
            }

            vertex(vertexConsumer, matrix4f, matrix3f, x, m, y, s, t, u, 0.5F, as + 0.5F);
            vertex(vertexConsumer, matrix4f, matrix3f, z, m, aa, s, t, u, 1.0F, as + 0.5F);
            vertex(vertexConsumer, matrix4f, matrix3f, ad, m, ae, s, t, u, 1.0F, as);
            vertex(vertexConsumer, matrix4f, matrix3f, ab, m, ac, s, t, u, 0.5F, as);
            matrixStack.pop();
        }
    }


    private static void vertex(
            VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, float z, int red, int green, int blue, float u, float v
    ) {
        vertexConsumer.vertex(positionMatrix, x, y, z)
                .color(red, green, blue, 255)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .next();
    }

    private Vec3d fromLerpedPosition(LivingEntity entity, double yOffset, float delta) {
        double d = MathHelper.lerp((double)delta, entity.lastRenderX, entity.getX());
        double e = MathHelper.lerp((double)delta, entity.lastRenderY, entity.getY()) + yOffset;
        double f = MathHelper.lerp((double)delta, entity.lastRenderZ, entity.getZ());
        return new Vec3d(d, e, f);
    }
}
