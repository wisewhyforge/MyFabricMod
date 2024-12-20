package com.justin.justinmod;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup EXAMPLE_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(JustinMod.MOD_ID, "example"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.example")).icon(() ->
                    new ItemStack(ModItems.EXAMPLE_ITEM)).entries((displayContext, entries) -> {
                    entries.add(ModItems.EXAMPLE_ITEM);
                    entries.add(ModBlocks.EXAMPLE_BLOCK);
            }).build());


    public static void registerItemGroups() {
        JustinMod.LOGGER.info("Registering Item Groups for " + JustinMod.MOD_ID);
    }

}
