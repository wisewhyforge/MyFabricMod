package com.justin.justinmod.mixin;

import com.justin.justinmod.EntityHelper;
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


        if (heldItem == null) return;
        LivingEntity livingEntity = EntityHelper.getPlayerLookingAtEntity(playerEntity.getWorld(), playerEntity);
        if (livingEntity != null && playerEntity.isUsingItem()) {
            //JustinMod.LOGGER.info("Pitch: " + pitch);
            //JustinMod.LOGGER.info("Yaw: " + playerEntity.getHeadYaw());
            float yaw = playerEntity.getHeadYaw();
            // Float between 0 and 1
            float beamProgress = 1.0F;
            float currentBeamProgress = 50 + tickDelta;
            float moddedBeamProgress = currentBeamProgress * 0.5F % 1.0F;
            float l = playerEntity.getStandingEyeHeight();
            matrixStack.push();
            matrixStack.translate(0.0F, l, 0F); // Changed from l
            // Linearly interpolated position of the target entity mid offset given a tick delta
            Vec3d vec3d = this.fromLerpedPosition(livingEntity, (double)livingEntity.getHeight() * 0.5, tickDelta);
            // Linearly interpolated position of the player entity eye height offset given a tick delta
            Vec3d vec3d2 = this.fromLerpedPosition(playerEntity, (double)l, tickDelta);
            // Subtract the interpolated position of the target to the interpolated position of the player
            //Vec3d vec3d3 = vec3d.subtract(vec3d2);

            //Vec3d vec3d3 = playerEntity.getRotationVec(tickDelta);
            //Vec3d vec3d3 = Vec3d.fromPolar(pitch, playerEntity.getHeadYaw());
            Vec3d vec3d3 = new Vec3d(0,-.1,-1.0);
            //Vec3d vec3d3 = new Vec3d(0,0,-1.0).add(vec3d.subtract(vec3d2).normalize().subtract(new Vec3d(0,0,-1.0))).normalize();

            JustinMod.LOGGER.info("Entity to Player Vector: " + vec3d3);
            //JustinMod.LOGGER.info();
            JustinMod.LOGGER.info("Looking Vector: " + playerEntity.getRotationVector().normalize());
            // Add one to the length of the unnormalized subtracted vector and set to m
            float unnormalizedDifferenceLength = (float)(vec3d3.length() + 10.0); // Changed from + 1
            // Normalize the subtracted vectors
            vec3d3 = vec3d3.normalize();
            // Inverse Cosine the y component of the normalized subtracted vector
            float n = (float)Math.acos(vec3d3.y);
            // atan2 angle measure between the positive x-axis and the ray form the origin to the point (z component of the subtracted vector, x component of the subtracted vector)
            float o = (float)Math.atan2(vec3d3.z, vec3d3.x);
            //JustinMod.LOGGER.info("Normalized Subtracted Vector: " + vec3d3 + " O: " + o);
            //JustinMod.LOGGER.info("Position Matrix Stack before Y Transform:\n " + matrixStack.peek().getPositionMatrix() + "\nProperties: " + matrixStack.peek().getPositionMatrix().properties());
            //JustinMod.LOGGER.info("Normal Matrix Stack before Y Transform:\n " + matrixStack.peek().getNormalMatrix());

            // Multiply the top stack of the matrix by rotating the y axis by subtracting 90 degrees in radians from the atan2 angle measure and converting to degrees
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float) (Math.PI / 2) - o) * (180.0F / (float)Math.PI)));

            //JustinMod.LOGGER.info("Normal Matrix Stack after Y Transform:\n " + matrixStack.peek().getNormalMatrix());
            //JustinMod.LOGGER.info("Position Matrix Stack After Y Transform:\n " + matrixStack.peek().getPositionMatrix() + "\nProperties: " + matrixStack.peek().getPositionMatrix().properties());
            // Multiply the top of the stack of the matrix by rotating the positive x-axis by multiplying the y component fo the normalized subtracted vector and converting to degrees
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n * (180.0F / (float)Math.PI)));
            //JustinMod.LOGGER.info("Position Matrix Stack After X Transform:\n " + matrixStack.peek().getPositionMatrix());

            // p = 1
            int p = 1;
            // Q is current beam progress
            float currentBeamProgressAnimationModifier = currentBeamProgress * 0.05F * -1.5F; // Is Max is -0.075
            // R is the beam progress squared
            float beamProgressSquared = beamProgress * beamProgress;
            // Colors of the beam based on the progress of the beam squared
            int red = 64 + (int)(beamProgressSquared * 191.0F);
            int blue = 32 + (int)(beamProgressSquared * 191.0F);
            int green = 128 - (int)(beamProgressSquared * 64.0F);
            //float v = 0.2F;
            //float w = 0.282F;
            float x = MathHelper.cos(currentBeamProgressAnimationModifier + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
            float y = MathHelper.sin(currentBeamProgressAnimationModifier + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
            float z = MathHelper.cos(currentBeamProgressAnimationModifier + (float) (Math.PI / 4)) * 0.282F;
            float aaZComponent = MathHelper.sin(currentBeamProgressAnimationModifier + (float) (Math.PI / 4)) * 0.282F;
            float abXComponent = MathHelper.cos(currentBeamProgressAnimationModifier + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
            float acZComponent = MathHelper.sin(currentBeamProgressAnimationModifier + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
            float adXComponent = MathHelper.cos(currentBeamProgressAnimationModifier + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
            float aeZComponent = MathHelper.sin(currentBeamProgressAnimationModifier + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
            float afXComponent = MathHelper.cos(currentBeamProgressAnimationModifier + (float) Math.PI) * 0.2F;
            float agZComponent = MathHelper.sin(currentBeamProgressAnimationModifier + (float) Math.PI) * 0.2F;
            float ahXComponent = MathHelper.cos(currentBeamProgressAnimationModifier + 0.0F) * 0.2F;
            float aiZComponent = MathHelper.sin(currentBeamProgressAnimationModifier + 0.0F) * 0.2F;
            float ajXComponent = MathHelper.cos(currentBeamProgressAnimationModifier + (float) (Math.PI / 2)) * 0.2F;
            float akZComponent = MathHelper.sin(currentBeamProgressAnimationModifier + (float) (Math.PI / 2)) * 0.2F;
            float alXComponent = MathHelper.cos(currentBeamProgressAnimationModifier + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
            float amZComponent = MathHelper.sin(currentBeamProgressAnimationModifier + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
            //float ao = 0.0F;
            //float ap = 0.4999F;
            float aq = -1.0F + moddedBeamProgress;
            float ar = unnormalizedDifferenceLength * 2.5F + aq;
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
            // Peek the top of the matrix stack
            MatrixStack.Entry entry = matrixStack.peek();
            // Get the position matrix of the top of the matrix stack
            Matrix4f matrix4f = entry.getPositionMatrix();
            // Get the normal matrix of the top of the matrix stack
            Matrix3f matrix3f = entry.getNormalMatrix();
            vertex(vertexConsumer, matrix4f, matrix3f, afXComponent, unnormalizedDifferenceLength, agZComponent, red, blue, green, 0.4999F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, afXComponent, 0.0F, agZComponent, red, blue, green, 0.4999F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, ahXComponent, 0.0F, aiZComponent, red, blue, green, 0.0F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, ahXComponent, unnormalizedDifferenceLength, aiZComponent, red, blue, green, 0.0F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, ajXComponent, unnormalizedDifferenceLength, akZComponent, red, blue, green, 0.4999F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, ajXComponent, 0.0F, akZComponent, red, blue, green, 0.4999F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, alXComponent, 0.0F, amZComponent, red, blue, green, 0.0F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, alXComponent, unnormalizedDifferenceLength, amZComponent, red, blue, green, 0.0F, ar);
            float as = 0.0F;
            if (playerEntity.age % 2 == 0) {
                as = 0.5F;
            }

            vertex(vertexConsumer, matrix4f, matrix3f, x, unnormalizedDifferenceLength, y, red, blue, green, 0.5F, as + 0.5F);
            vertex(vertexConsumer, matrix4f, matrix3f, z, unnormalizedDifferenceLength, aaZComponent, red, blue, green, 1.0F, as + 0.5F);
            vertex(vertexConsumer, matrix4f, matrix3f, adXComponent, unnormalizedDifferenceLength, aeZComponent, red, blue, green, 1.0F, as);
            vertex(vertexConsumer, matrix4f, matrix3f, abXComponent, unnormalizedDifferenceLength, acZComponent, red, blue, green, 0.5F, as);
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
