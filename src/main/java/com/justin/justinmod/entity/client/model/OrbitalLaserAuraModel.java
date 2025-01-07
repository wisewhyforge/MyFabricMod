package com.justin.justinmod.entity.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class OrbitalLaserAuraModel<T extends Entity> extends SinglePartEntityModel<T> {


    private final ModelPart reticle1;
    private final ModelPart reticle2;
    private final ModelPart reticle3;
    private final ModelPart reticle4;
    private final ModelPart bb_main;

    private final ModelPart root;
    public OrbitalLaserAuraModel(ModelPart root) {
        this.root = root;
        this.reticle1 = root.getChild("reticle1");
        this.reticle2 = root.getChild("reticle2");
        this.reticle3 = root.getChild("reticle3");
        this.reticle4 = root.getChild("reticle4");
        this.bb_main = root.getChild("bb_main");

    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        float offsetY = -1.0F;
        ModelPartData reticle1 = modelPartData.addChild("reticle1", ModelPartBuilder.create().uv(18, 9).cuboid(2.0F, -1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 11).cuboid(1.0F, offsetY, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 13).cuboid(2.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 15).cuboid(0.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 17).cuboid(1.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 19).cuboid(1.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 21).cuboid(-1.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 9).cuboid(0.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 11).cuboid(0.0F, offsetY, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-7.0F, 24.0F, 8.0F));

        ModelPartData reticle2 = modelPartData.addChild("reticle2", ModelPartBuilder.create().uv(22, 13).cuboid(2.0F, -1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 15).cuboid(1.0F, offsetY, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 17).cuboid(2.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 19).cuboid(0.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 21).cuboid(1.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 23).cuboid(1.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 23).cuboid(-1.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 25).cuboid(0.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 25).cuboid(0.0F, offsetY, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(8.0F, 24.0F, 7.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData reticle3 = modelPartData.addChild("reticle3", ModelPartBuilder.create().uv(26, 9).cuboid(2.0F, -1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(26, 11).cuboid(1.0F, offsetY, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(26, 13).cuboid(2.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(26, 15).cuboid(0.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(26, 17).cuboid(1.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(26, 19).cuboid(1.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(26, 21).cuboid(-1.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(26, 23).cuboid(0.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(26, 25).cuboid(0.0F, offsetY, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, 24.0F, -7.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData reticle4 = modelPartData.addChild("reticle4", ModelPartBuilder.create().uv(0, 27).cuboid(2.0F, -1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(4, 27).cuboid(1.0F, offsetY, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(8, 27).cuboid(2.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 27).cuboid(0.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 27).cuboid(1.0F, offsetY, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(20, 27).cuboid(1.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 27).cuboid(-1.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(28, 27).cuboid(0.0F, offsetY, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 29).cuboid(0.0F, offsetY, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(7.0F, 24.0F, -8.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(7.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 9).cuboid(-8.0F, offsetY, -4.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData cube_r1 = bb_main.addChild("cube_r1", ModelPartBuilder.create().uv(18, 0).cuboid(7.0F, offsetY, -8.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 0.0F, 15.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r2 = bb_main.addChild("cube_r2", ModelPartBuilder.create().uv(0, 18).cuboid(7.0F, offsetY, -8.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        reticle1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        reticle2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        reticle3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        reticle4.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

//    /**
//     * The key of the main model part, whose value is {@value}.
//     */
//    private static final String MAIN = "main";
//    private final ModelPart root;
//    private final ModelPart bullet;
//
//    public OrbitalLaserAuraModel(ModelPart root) {
//        this.root = root;
//        this.bullet = root.getChild("main");
//    }
//
//    public static TexturedModelData getTexturedModelData() {
//        ModelData modelData = new ModelData();
//        ModelPartData modelPartData = modelData.getRoot();
//        ModelPartData bb_main = modelPartData.addChild("main", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F))
//                .uv(0, 9).cuboid(-3.0F, -2.0F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
//                .uv(0, 16).cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
//                .uv(0, 23).cuboid(-2.0F, -3.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
//                .uv(16, 23).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
//                .uv(24, 9).cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
//                .uv(24, 14).cuboid(-2.0F, 2.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
//        return TexturedModelData.of(modelData, 64, 64);
//    }
//
//    @Override
//    public ModelPart getPart() {
//        return this.root;
//    }
//
//    @Override
//    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
//        this.bullet.yaw = headYaw * (float) (Math.PI / 180.0);
//        this.bullet.pitch = headPitch * (float) (Math.PI / 180.0);
//    }
}
