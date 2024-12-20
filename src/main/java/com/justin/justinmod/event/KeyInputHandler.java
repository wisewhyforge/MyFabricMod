package com.justin.justinmod.event;

import com.justin.justinmod.entity.custom.HelicopterEntity;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

    public static final String KEY_CATEGORY_HELICOPTER = "key.category.justinmod.tutorial";
    public static final String KEY_DESCEND = "key.justinmod.descend";

    public static KeyBinding descentKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (descentKey.isPressed()) {

                if (client.player.getVehicle() instanceof HelicopterEntity helicopterEntity) {
                    helicopterEntity.setPressingDown(true);
                    //client.player.sendMessage(Text.literal(""+helicopterEntity.getPressingDown()));
                }

            } else {
                if (client.player != null && client.player.getVehicle() instanceof HelicopterEntity helicopterEntity) {
                    helicopterEntity.setPressingDown(false);
                }
            }


        });
    }

    public static void register() {
        descentKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_DESCEND,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                KEY_CATEGORY_HELICOPTER
        ));

        registerKeyInputs();
    }
}
