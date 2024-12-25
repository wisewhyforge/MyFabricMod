package com.justin.justinmod.entity.client.render;

import com.justin.justinmod.EntityHelper;
import com.justin.justinmod.JustinMod;
import com.justin.justinmod.entity.client.model.GuardianLaserAuraModel;
import com.justin.justinmod.entity.custom.GuardianLaserAuraEntity;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class GuardianLaserAuraRenderer extends EntityRenderer<GuardianLaserAuraEntity> {

    private static final Identifier TEXTURE = new Identifier(JustinMod.MOD_ID, "textures/entity/spark.png");
    private static final Identifier BEAM_TEXTURE = new Identifier(JustinMod.MOD_ID, "textures/entity/beam.png");

    private static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE);
    private final GuardianLaserAuraModel<GuardianLaserAuraEntity> model;

    public GuardianLaserAuraRenderer(EntityRendererFactory.Context context, GuardianLaserAuraModel<GuardianLaserAuraEntity> m) {
        super(context);
        this.model = m;
    }

    protected int getBlockLight(GuardianLaserAuraEntity homingBulletEntity, BlockPos blockPos) {
        return 15;
    }

    public void render(GuardianLaserAuraEntity guardianLaserAuraEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(guardianLaserAuraEntity, f, tickDelta, matrixStack, vertexConsumerProvider, i);
        matrixStack.push();
        //Shulker Bullet Rendering
        renderShulkerBullet(guardianLaserAuraEntity, tickDelta, matrixStack, vertexConsumerProvider, i);
        float k;
        float j;
        VertexConsumer vertexConsumer;
        float h;

        // Begin Attack Rendering
        LivingEntity livingEntity = guardianLaserAuraEntity.getBeamTarget();
        if (livingEntity != null) {
            h = guardianLaserAuraEntity.getBeamProgress(tickDelta);
            j = guardianLaserAuraEntity.getBeamTicks() + tickDelta;
            k = j * 0.5F % 1.0F;
            float l = 0;
            matrixStack.push();
            matrixStack.translate(0.0F, l, 0.0F);
            Vec3d vec3d = this.fromLerpedPosition(livingEntity, (double)livingEntity.getHeight() * 0.5, tickDelta);
            Vec3d vec3d2 = this.fromLerpedPosition(guardianLaserAuraEntity, (double)l, tickDelta);
            Vec3d vec3d3 = vec3d.subtract(vec3d2);
            float m = (float)(vec3d3.length() + 1.0);
            vec3d3 = vec3d3.normalize();
            float n = (float)Math.acos(vec3d3.y);
            float o = (float)Math.atan2(vec3d3.z, vec3d3.x);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float) (Math.PI / 2) - o) * (180.0F / (float)Math.PI)));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n * (180.0F / (float)Math.PI)));
            int p = 1;
            float q = j * 0.05F * -1.5F;
            float r = h * h;
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
            vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
            MatrixStack.Entry entry = matrixStack.peek();
            Matrix4f matrix4f = entry.getPositionMatrix();
            Matrix3f matrix3f = entry.getNormalMatrix();

            // Beacon Rendering
            renderBeam(matrixStack, vertexConsumerProvider, tickDelta, h, 0, 1024, new float[]{s, t, u}, livingEntity, guardianLaserAuraEntity);


            //Guardian Beam Rendering
            vertex(vertexConsumer, matrix4f, matrix3f, af, m, ag, s, t, u, 0.4999F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, af, 0.0F, ag, s, t, u, 0.4999F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, 0.0F, ai, s, t, u, 0.0F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, m, ai, s, t, u, 0.0F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, m, ak, s, t, u, 0.4999F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, 0.0F, ak, s, t, u, 0.4999F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, al, 0.0F, am, s, t, u, 0.0F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, al, m, am, s, t, u, 0.0F, ar);
            float as = 0.0F;
            if (guardianLaserAuraEntity.age % 2 == 0) {
                as = 0.5F;
            }

            vertex(vertexConsumer, matrix4f, matrix3f, x, m, y, s, t, u, 0.5F, as + 0.5F);
            vertex(vertexConsumer, matrix4f, matrix3f, z, m, aa, s, t, u, 1.0F, as + 0.5F);
            vertex(vertexConsumer, matrix4f, matrix3f, ad, m, ae, s, t, u, 1.0F, as);
            vertex(vertexConsumer, matrix4f, matrix3f, ab, m, ac, s, t, u, 0.5F, as);
            matrixStack.pop();

        }
    }

    private void renderShulkerBullet(GuardianLaserAuraEntity guardianLaserAuraEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        float h = MathHelper.lerpAngleDegrees(tickDelta, guardianLaserAuraEntity.prevYaw, guardianLaserAuraEntity.getYaw());
        float j = MathHelper.lerp(tickDelta, guardianLaserAuraEntity.prevPitch, guardianLaserAuraEntity.getPitch());
        float k = (float) guardianLaserAuraEntity.age + tickDelta;
        matrixStack.translate(0.0F, 0.15F, 0.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.sin(k * 0.1F) * 180.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.cos(k * 0.1F) * 180.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(k * 0.15F) * 360.0F));
        matrixStack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setAngles(guardianLaserAuraEntity, 0.0F, 0.0F, 0.0F, h, j);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(LAYER);
        this.model.render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 0.15F);
        matrixStack.pop();
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

    private Vec3d fromLerpedPosition(Entity entity, double yOffset, float delta) {
        double d = MathHelper.lerp((double)delta, entity.lastRenderX, entity.getX());
        double e = MathHelper.lerp((double)delta, entity.lastRenderY, entity.getY()) + yOffset;
        double f = MathHelper.lerp((double)delta, entity.lastRenderZ, entity.getZ());
        return new Vec3d(d, e, f);
    }

    public Identifier getTexture(GuardianLaserAuraEntity homingBulletEntity) {
        return TEXTURE;
    }

    private void renderBeam(
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickDelta, float beamProgress, int yOffset, int maxY, float[] color, LivingEntity target, GuardianLaserAuraEntity aura
    ) {
        renderBeam(matrices, vertexConsumers, BEAM_TEXTURE, tickDelta, 1.0F, beamProgress, yOffset, maxY, color, 0.2F, 0.25F, target, aura);
    }

    public void renderBeam(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            Identifier textureId,
            float tickDelta,
            float heightScale,
            float beamProgress,
            int yOffset,
            int maxY,
            float[] color,
            float innerRadius,
            float outerRadius,
            LivingEntity target,
            GuardianLaserAuraEntity aura
    ) {
        int i = yOffset + maxY;
        matrices.push();
        matrices.translate(0, -0.15F, 0);

        Vec3d vec3d = this.fromLerpedPosition(target, (double)target.getHeight() * 0.5, tickDelta);
        Vec3d vec3d2 = this.fromLerpedPosition(aura, (double)0, tickDelta);
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        float distanceLength = (float)(vec3d3.length() + 1.0);
        vec3d3 = vec3d3.normalize();
        float yComponentDiff = (float)Math.acos(vec3d3.y);
        float xZComponentDiff = (float)Math.atan2(vec3d3.z, vec3d3.x);
        //matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(((float)(Math.PI / 2) + xZComponentDiff) * (180.0F / (float)Math.PI)));
        //matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(yComponentDiff * (180.0F / (float)Math.PI)));
        matrices.push();
        float g = maxY < 0 ? beamProgress : -beamProgress;
        float h = MathHelper.fractionalPart(g * 0.2F - (float)MathHelper.floor(g * 0.1F));
        float j = color[0];
        float k = color[1];
        float l = color[2];
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(beamProgress*180.0F));
        float m = 0.0F;
        float p = 0.0F;
        float q = -innerRadius;
        float r = 0.0F;
        float s = 0.0F;
        float t = -innerRadius;
        float u = 0.0F;
        float v = 1.0F;
        float w = -1.0F + h;
        float x = (float)maxY * heightScale * (0.5F / innerRadius) + w;
        renderBeamLayer(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, false)),
                j,
                k,
                l,
                1.0F,
                yOffset,
                i,
                0.0F,
                innerRadius,
                innerRadius,
                0.0F,
                q,
                0.0F,
                0.0F,
                t,
                0.0F,
                1.0F,
                x,
                w
        );
        matrices.pop();
        m = -outerRadius;
        float n = -outerRadius;
        p = -outerRadius;
        q = -outerRadius;
        u = 0.0F;
        v = 1.0F;
        w = -1.0F + h;
        x = (float)maxY * heightScale + w;
        renderBeamLayer(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, true)),
                j,
                k,
                l,
                0.125F,
                yOffset,
                i,
                m,
                n,
                outerRadius,
                p,
                q,
                outerRadius,
                outerRadius,
                outerRadius,
                0.0F,
                1.0F,
                x,
                w
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

    public boolean rendersOutsideBoundingBox(GuardianLaserAuraEntity beaconBlockEntity) {
        return true;
    }

    public boolean isInRenderDistance(GuardianLaserAuraEntity beaconBlockEntity, Vec3d vec3d) {
        return beaconBlockEntity.getPos().multiply(1.0, 0.0, 1.0).isInRange(vec3d.multiply(1.0, 0.0, 1.0), (double)this.getRenderDistance());
    }

    public int getRenderDistance() {
        return 256;
    }
}
