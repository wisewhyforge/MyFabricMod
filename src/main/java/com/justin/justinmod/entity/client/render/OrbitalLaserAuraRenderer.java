package com.justin.justinmod.entity.client.render;

import com.justin.justinmod.JustinMod;
import com.justin.justinmod.entity.client.model.OrbitalLaserAuraModel;
import com.justin.justinmod.entity.custom.GuardianLaserAuraEntity;
import com.justin.justinmod.entity.custom.OrbitalLaserAuraEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class OrbitalLaserAuraRenderer extends EntityRenderer<OrbitalLaserAuraEntity> {

    private static final Identifier ORBITAL_LASER_TEXTURE = new Identifier(JustinMod.MOD_ID, "textures/entity/orbital_laser.png");

    private static final Identifier BEAM_TEXTURE = new Identifier(JustinMod.MOD_ID, "textures/entity/beam.png");

    private static final Identifier RETICLE_TEXTURE = new Identifier(JustinMod.MOD_ID, "textures/entity/reticle.png");

    private OrbitalLaserAuraModel<OrbitalLaserAuraEntity> model;

    public OrbitalLaserAuraRenderer(EntityRendererFactory.Context ctx, OrbitalLaserAuraModel<OrbitalLaserAuraEntity> orbitalLaserAuraModel) {
        super(ctx);
        this.model = orbitalLaserAuraModel;
    }

    @Override
    public void render(OrbitalLaserAuraEntity orbitalLaserAuraEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {

        LivingEntity livingEntity = orbitalLaserAuraEntity.getBeamTarget();
        float beamProgress;
        if (livingEntity != null) {
            beamProgress = orbitalLaserAuraEntity.getBeamProgress(tickDelta);
            float l = 0;
            float r = beamProgress * beamProgress;
            int red = 64 + (int)(r * 191.0F);
            int green = 32 + (int)(r * 191.0F);
            int blue = 128 - (int)(r * 64.0F);
            matrixStack.push();
            Vec3d vec3d = this.fromLerpedPosition(livingEntity, (double) livingEntity.getHeight() * 0.5, tickDelta);
            Vec3d vec3d2 = this.fromLerpedPosition(orbitalLaserAuraEntity, (double) l, tickDelta);
            Vec3d vec3d3 = vec3d.subtract(vec3d2);
            vec3d3 = vec3d3.normalize();
            float maxY = 319;
            renderBeam(matrixStack, vertexConsumers, tickDelta, beamProgress, orbitalLaserAuraEntity.getWorld().getTime(), 0, (int) maxY, light, new float[]{red, green, blue}, livingEntity, orbitalLaserAuraEntity);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.translate(0.0F, -1.5F, 0.0F);
            float x_transform = 0.4F * (((orbitalLaserAuraEntity.age) % orbitalLaserAuraEntity.getWarmupTime()) - (orbitalLaserAuraEntity.getWarmupTime() / 2));
            float sigmoidRotationScale = ((float)Math.exp(x_transform) / ((float)Math.exp(x_transform) + 1)) * 7F;
            float rotationDegrees = sigmoidRotationScale * (beamProgress * 90);
            // Just sets the rotation
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationDegrees));
            this.model.render(matrixStack, vertexConsumers.getBuffer(this.model.getLayer(RETICLE_TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pop();
        }
        super.render(orbitalLaserAuraEntity, yaw, tickDelta, matrixStack, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(OrbitalLaserAuraEntity entity) {
        return ORBITAL_LASER_TEXTURE;
    }

    private void renderBeam(
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickDelta, float beamProgress, long worldTime, int yOffset, int maxY, int light, float[] color, LivingEntity target, OrbitalLaserAuraEntity aura
    ) {
        int tackOn = 0;
        renderBeam(matrices, vertexConsumers, BEAM_TEXTURE, tickDelta, 1.0F, beamProgress, worldTime, yOffset, maxY, light, color, tackOn + 0.2F, tackOn + 0.25F, target, aura);
    }

    public void renderBeam(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            Identifier textureId,
            float tickDelta,
            float heightScale,
            float beamProgress,
            long worldTime,
            int yOffset,
            int maxY,
            int light,
            float[] color,
            float innerRadius,
            float outerRadius,
            LivingEntity target,
            OrbitalLaserAuraEntity aura
    ) {
        int height = yOffset + maxY;
        matrices.push();
        //matrices.translate(0, -0.15F, 0);

        matrices.push();
        float f = (float) java.lang.Math.floorMod(worldTime, 40) + tickDelta;
        float g = maxY < 0 ? beamProgress : -beamProgress;
        float v2Offset = MathHelper.fractionalPart(g * 0.2F - (float)MathHelper.floor(g * 0.1F));
        float red = color[0];
        float green = color[1];
        float blue = color[2];
        float smoothRotation = (f * 2.25F - 45.0F);
        float beamProgressScale = ((Math.sin(beamProgress * 2 * (float)Math.PI - ((float) Math.PI)) + 1) * 2);
        float x_transform = 0.4F * (((aura.age) % aura.getWarmupTime()) - (aura.getWarmupTime() / 2));
        float sigmoidRotationScale = ((float)Math.exp(x_transform) / ((float)Math.exp(x_transform) + 1)) * 7F;
        float rotationDegrees = sigmoidRotationScale * (beamProgress * 90);
        // Just sets the rotation
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationDegrees));
        float x1 = 0.0F;
        float z2 = 0.0F;
        float x3 = -innerRadius;
//        float r = 0.0F;
//        float s = 0.0F;
        float z4 = -innerRadius;
        float u = 0.0F;
        float v = 1.0F;
        float v2 = -1.0F + v2Offset;
        float v1 = (float)maxY * heightScale * (0.5F / innerRadius) + v2;
        renderBeamLayer(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, false)),
                red,
                green,
                blue,
                1.0F,
                yOffset,
                height,
                0.0F,
                innerRadius,
                innerRadius,
                0.0F,
                x3,
                0.0F,
                0.0F,
                z4,
                0.0F,
                1.0F,
                v1,
                v2
        );

        //renderSurroundingBeam(matrices, vertexConsumers, textureId, yOffset, maxY, innerRadius, red, green, blue, x3, z4, v2, v1);

        matrices.pop();

        x1 = -outerRadius;
        float z1 = -outerRadius;
        z2 = -outerRadius;
        x3 = -outerRadius;
        u = 0.0F;
        v = 1.0F;
        v2 = -1.0F + v2Offset;
        v1 = (float)maxY * heightScale + v2;
        renderBeamLayer(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, true)),
                red,
                green,
                blue,
                0.125F,
                yOffset,
                height,
                x1,
                z1,
                outerRadius,
                z2,
                x3,
                outerRadius,
                outerRadius,
                outerRadius,
                0.0F,
                1.0F,
                v1,
                v2
        );
        matrices.pop();
    }

    private static void renderSurroundingBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Identifier textureId, int yOffset, int maxY, float innerRadius, float red, float green, float blue, float x3, float z4, float v2, float v1) {
        // How far forward the sub beam is rendered relative to the target
        double beamProgress = 0.1*maxY;
        int beamLength = 5;
        matrices.push();
        matrices.translate(beamLength / 2.0, beamProgress, -beamLength / 2.0);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        renderBeamLayer(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, false)),
                red,
                green,
                blue,
                1.0F,
                yOffset,
                beamLength,
                0.0F,
                innerRadius,
                innerRadius,
                0.0F,
                x3,
                0.0F,
                0.0F,
                z4,
                0.0F,
                1.0F,
                v1,
                v2
        );
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
        renderBeamLayer(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, false)),
                red,
                green,
                blue,
                1.0F,
                yOffset,
                beamLength,
                0.0F,
                innerRadius,
                innerRadius,
                0.0F,
                x3,
                0.0F,
                0.0F,
                z4,
                0.0F,
                1.0F,
                v1,
                v2
        );
        matrices.pop();


        matrices.push();
        matrices.translate(-beamLength / 2.0, beamProgress, beamLength / 2.0);

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
        renderBeamLayer(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, false)),
                red,
                green,
                blue,
                1.0F,
                yOffset,
                beamLength,
                0.0F,
                innerRadius,
                innerRadius,
                0.0F,
                x3,
                0.0F,
                0.0F,
                z4,
                0.0F,
                1.0F,
                v1,
                v2
        );
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90));
        renderBeamLayer(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, false)),
                red,
                green,
                blue,
                1.0F,
                yOffset,
                beamLength,
                0.0F,
                innerRadius,
                innerRadius,
                0.0F,
                x3,
                0.0F,
                0.0F,
                z4,
                0.0F,
                1.0F,
                v1,
                v2
        );
        matrices.pop();
    }

    private static void renderBeamLayer(
            MatrixStack matrices,
            VertexConsumer vertices,
            float red,
            float green,
            float blue,
            float alpha,
            int yOffset,
            int height,
            float x1,
            float z1,
            float x2,
            float z2,
            float x3,
            float z3,
            float x4,
            float z4,
            float u1,
            float u2,
            float v1,
            float v2
    ) {
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x1, z1, x2, z2, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x4, z4, x3, z3, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x2, z2, x4, z4, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x3, z3, x1, z1, u1, u2, v1, v2);
    }

    private static void renderBeamFace(
            Matrix4f positionMatrix,
            Matrix3f normalMatrix,
            VertexConsumer vertices,
            float red,
            float green,
            float blue,
            float alpha,
            int yOffset,
            int height,
            float x1,
            float z1,
            float x2,
            float z2,
            float u1,
            float u2,
            float v1,
            float v2
    ) {
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x1, z1, u2, v1);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x1, z1, u2, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x2, z2, u1, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x2, z2, u1, v1);
    }

    /**
     * @param v the top-most coordinate of the texture region
     * @param u the left-most coordinate of the texture region
     */
    private static void renderBeamVertex(
            Matrix4f positionMatrix,
            Matrix3f normalMatrix,
            VertexConsumer vertices,
            float red,
            float green,
            float blue,
            float alpha,
            int y,
            float x,
            float z,
            float u,
            float v
    ) {
        vertices.vertex(positionMatrix, x, (float)y, z)
                .color(red, green, blue, alpha)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .next();
    }

    private Vec3d fromLerpedPosition(Entity entity, double yOffset, float delta) {
        double d = MathHelper.lerp((double)delta, entity.lastRenderX, entity.getX());
        double e = MathHelper.lerp((double)delta, entity.lastRenderY, entity.getY()) + yOffset;
        double f = MathHelper.lerp((double)delta, entity.lastRenderZ, entity.getZ());
        return new Vec3d(d, e, f);
    }
}
