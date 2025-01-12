package com.justin.justinmod.mixin;

import com.justin.justinmod.JustinMod;
import com.justin.justinmod.entity.custom.OrbitalLaserAuraEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThreadedAnvilChunkStorage.class)
public class ThreadedAnvilChunkStorageMixin {

    @Shadow @Final
    Int2ObjectMap<ThreadedAnvilChunkStorage.EntityTracker> entityTrackers = new Int2ObjectOpenHashMap<>();


    @Inject(method="shouldTick", at = @At("RETURN"), cancellable = true)
    public void init(ChunkPos pos, CallbackInfoReturnable<Boolean> cir) {
       // if ()
        for (ThreadedAnvilChunkStorage.EntityTracker e : entityTrackers.values()) {
            if (e.entity instanceof OrbitalLaserAuraEntity) {
                cir.setReturnValue(true);
            }
        }
    }

}

