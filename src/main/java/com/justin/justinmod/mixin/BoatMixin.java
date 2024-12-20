package com.justin.justinmod.mixin;

import com.justin.justinmod.JustinMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BoatEntity.class)
public class BoatMixin {


    @Shadow private boolean pressingForward;

    @Shadow private boolean pressingBack;

    @Shadow private boolean pressingLeft;

    @Shadow private boolean pressingRight;

    @Inject(at = @At("TAIL"), method="setInputs", locals = LocalCapture.CAPTURE_FAILHARD)
    private void init(CallbackInfo info) {
        JustinMod.LOGGER.info("Boat: Forward: " + this.pressingForward + " Back: " + this.pressingBack + " Left " + this.pressingLeft + " Right " + this.pressingRight);
    }
}
