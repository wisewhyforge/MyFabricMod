package com.justin.justinmod;

import com.justin.justinmod.entity.ModEntities;
import com.justin.justinmod.entity.custom.HelicopterEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.impl.registry.sync.DynamicRegistryViewImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JustinMod implements ModInitializer {

	public static final Identifier JUSTIN_EXPLOSION_BEAM_TEXTURE = new Identifier(JustinMod.MOD_ID, "textures/entity/guardian_beam.png");


	public static final String MOD_ID = "justinmod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			if (entity instanceof PlayerEntity) {
				if (entity.getOffHandStack().getItem() instanceof ExampleItem item) {
					if (item.laser != null) {
						item.laser.kill();
					}
				} else if (entity.getMainHandStack().getItem() instanceof ExampleItem item) {
					if (item.laser != null) {
						item.laser.kill();
					}
				}
			}
		});
	}


}