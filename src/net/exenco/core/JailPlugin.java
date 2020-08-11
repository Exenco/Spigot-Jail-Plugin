package net.exenco.core;

import net.exenco.executor.JailExecutor;
import net.exenco.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;

public class JailPlugin extends JavaPlugin {

    Location minLocation;
    Location maxLocation;

    @Override
    public void onEnable() {

        /* Set up config */
        saveConfig();
        addDefaults();
        saveDefaultConfig();
        saveConfig();
        loadConfig();

        /* Register executor */
        Objects.requireNonNull(getCommand("jail")).setExecutor(new JailExecutor(this));

        /* Register events */
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BlockPlaceListener(this), this);
        pluginManager.registerEvents(new BlockBreakListener(this), this);
        pluginManager.registerEvents(new TabCompleteListener(), this);
        pluginManager.registerEvents(new CommandPreProcessListener(this), this);

        super.onEnable();
    }

    private void addDefaults() {
        FileConfiguration config = getConfig();

        ArrayList<String> defaultCommandList = new ArrayList<>();
        defaultCommandList.add("spawn");
        defaultCommandList.add("home");
        defaultCommandList.add("tpa");
        defaultCommandList.add("tp");
        defaultCommandList.add("back");
        config.addDefault("Jail.Commands", defaultCommandList);

        ArrayList<String> defaultBlockList = new ArrayList<>();
        defaultBlockList.add("red_bed");
        defaultBlockList.add("torch");
        defaultBlockList.add("wall_torch");
        defaultBlockList.add("bookshelf");
        defaultBlockList.add("cyan_bed");
        defaultBlockList.add("black_bed");
        config.addDefault("Jail.Blocks", defaultBlockList);

        config.options().copyDefaults(true);
    }

    public void loadConfig() {
        Location loc1 = readLocation("Jail.1", getConfig());
        Location loc2 = readLocation("Jail.2", getConfig());
        if(loc1 == null || loc2 == null) return;

        double x1 = Math.min(loc1.getX(), loc2.getX());
        double y1 = Math.min(loc1.getY(), loc2.getY());
        double z1 = Math.min(loc1.getZ(), loc2.getZ());

        double x2 = Math.max(loc1.getX(), loc2.getX());
        double y2 = Math.max(loc1.getY(), loc2.getY());
        double z2 = Math.max(loc1.getZ(), loc2.getZ());

        loc1.setX(x1);
        loc1.setY(y1);
        loc1.setZ(z1);

        loc2.setX(x2);
        loc2.setY(y2);
        loc2.setZ(z2);

        minLocation = loc1;
        maxLocation = loc2;
    }

    private Location readLocation(String path, FileConfiguration config) {
        try {
            Location loc = new Location(null, 0, 0, 0);

            loc.setWorld(Bukkit.getWorld(Objects.requireNonNull(config.getString(path + ".world"))));
            loc.setX(config.getDouble(path + ".x"));
            loc.setY(config.getDouble(path + ".y"));
            loc.setZ(config.getDouble(path + ".z"));
            loc.setYaw((float) config.getDouble(path + ".yaw"));
            loc.setYaw((float) config.getDouble(path + ".pitch"));

            return loc;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public boolean isInJail(Location location) {
        if(maxLocation == null || minLocation == null) return false;

        double x1 = minLocation.getX();
        double y1 = minLocation.getY();
        double z1 = minLocation.getZ();

        double x2 = maxLocation.getX();
        double y2 = maxLocation.getY();
        double z2 = maxLocation.getZ();

        return (location.getX() >= x1) && (location.getY() >= y1) && (location.getZ() >= z1) && (location.getX() <= x2) && (location.getY() <= y2) && (location.getZ() <= z2);
    }
}