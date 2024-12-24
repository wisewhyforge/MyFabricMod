package com.justin.justinmod.entity.client.render;

import com.justin.justinmod.EntityHelper;
import com.justin.justinmod.JustinMod;
import com.justin.justinmod.entity.client.model.GuardianLaserAuraModel;
import com.justin.justinmod.entity.custom.GuardianLaserAuraEntity;
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

    private static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE);
    private final GuardianLaserAuraModel<GuardianLaserAuraEntity> model;

    public GuardianLaserAuraRenderer(EntityRendererFactory.Context context, GuardianLaserAuraModel<GuardianLaserAuraEntity> m) {
        super(context);
        this.model = m;
    }

    protected int getBlockLight(GuardianLaserAuraEntity homingBulletEntity, BlockPos blockPos) {
        return 15;
    }

    public void render(GuardianLaserAuraEntity guardianLaserAuraEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        float h = MathHelper.lerpAngleDegrees(g, guardianLaserAuraEntity.prevYaw, guardianLaserAuraEntity.getYaw());
        float j = MathHelper.lerp(g, guardianLaserAuraEntity.prevPitch, guardianLaserAuraEntity.getPitch());
        float k = (float)guardianLaserAuraEntity.age + g;
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
        LivingEntity livingEntity = guardianLaserAuraEntity.getBeamTarget();
        if (livingEntity != null) {
            h = guardianLaserAuraEntity.getBeamProgress(g);
            j = guardianLaserAuraEntity.getBeamTicks() + g;
            k = j * 0.5F % 1.0F;
            float l = 0;
            matrixStack.push();
            matrixStack.translate(0.0F, l, 0.0F);
            Vec3d vec3d = this.fromLerpedPosition(livingEntity, (double)livingEntity.getHeight() * 0.5, g);
            Vec3d vec3d2 = this.fromLerpedPosition(guardianLaserAuraEntity, (double)l, g);
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
        super.render(guardianLaserAuraEntity, f, g, matrixStack, vertexConsumerProvider, i);
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
}
