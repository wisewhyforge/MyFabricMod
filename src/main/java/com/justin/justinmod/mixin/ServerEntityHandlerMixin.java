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

@Mixin(targets = "net.minecraft.server.world.ServerWorld$ServerEntityHandler")
public class ServerEntityHandlerMixin {

    @Inject(method = "stopTicking(Lnet/minecraft/entity/Entity;)V", at=@At("HEAD"), cancellable = true)
    public void init(CallbackInfo info, @Local LocalRef<Entity> entity) {
        if (entity.get() instanceof OrbitalLaserAuraEntity && entity.get().isAlive()) {
            JustinMod.LOGGER.info("Server Entity Handler Mixin");
            info.cancel();
        }
    }

}
