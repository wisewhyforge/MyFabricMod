package com.justin.justinmod.mixin;

import com.justin.justinmod.JustinMod;
import com.justin.justinmod.entity.custom.HelicopterEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class HelicopterRidingMixin extends AbstractClientPlayerEntity {

	@Shadow
	private boolean riding;

	@Shadow
	public Input input;

	public HelicopterRidingMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(at = @At("TAIL"), method = "tickRiding")
	private void init(CallbackInfo info) {
		// This code is injected into the start of MinecraftServer.loadWorld()
		//Entity controllingEntity = this.getControllingVehicle();
		//Text name = controllingEntity == null ? null : controllingEntity.getName();
		//JustinMod.LOGGER.info("" + controllingEntity + " Name: " + name);
		if (this.getControllingVehicle() instanceof HelicopterEntity helicopterEntity) {
			//JustinMod.LOGGER.info("Controlling a Helicopter!");
			helicopterEntity.setInputs(this.input.pressingLeft, this.input.pressingRight, this.input.pressingForward, this.input.pressingBack, this.input.jumping);
			this.riding = this.riding | (this.input.pressingLeft || this.input.pressingRight || this.input.pressingForward || this.input.pressingBack);
		}
	}
}