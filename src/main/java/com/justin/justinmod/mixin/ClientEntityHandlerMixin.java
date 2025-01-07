package com.justin.justinmod.mixin;

import com.justin.justinmod.JustinMod;
import com.justin.justinmod.entity.custom.OrbitalLaserAuraEntity;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net.minecraft.client.world.ClientWorld$ClientEntityHandler")
public class ClientEntityHandlerMixin {

    @Inject(method = "stopTicking(Lnet/minecraft/entity/Entity;)V", at=@At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void init(CallbackInfo info, @Local LocalRef<Entity> entity) {
        if (entity.get() instanceof OrbitalLaserAuraEntity && entity.get().isAlive()) {
            JustinMod.LOGGER.info("Client Stop ticking mixin");
            info.cancel();
        }

    }

}
