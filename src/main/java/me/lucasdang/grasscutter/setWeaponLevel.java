package me.lucasdang.grasscutter;

import emu.grasscutter.plugin.Plugin;

public class setWeaponLevel extends Plugin {
    private static setWeaponLevel instance;

    public static setWeaponLevel getInstance() {
        return instance;
    }

    @Override public void onLoad() {
        // Set the plugin instance.
        instance = this;
    }

    @Override public void onEnable() {

        // Register commands.
        this.getHandle().registerCommand(new me.lucasdang.grasscutter.commands.setLevelWeaponCommand());

        // Log a plugin status message.
        this.getLogger().info("SetLevelWeapon By Lucas");
    }

    @Override public void onDisable() {
        // Log a plugin status message.
        this.getLogger().info("How could you do this to me... setWeaponLevel has been disabled.");
    }
}