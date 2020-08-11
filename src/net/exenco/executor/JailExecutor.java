package net.exenco.executor;

import net.exenco.core.JailPlugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class JailExecutor implements CommandExecutor {

    JailPlugin jailPlugin;
    public JailExecutor(JailPlugin jailPlugin) {
        this.jailPlugin = jailPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("jail")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("§7You are not a player.");
                return false;
            }
            Player player = (Player) commandSender;

            if(args.length >= 1) {
                String path = "Jail." + args[0];
                Location loc = player.getLocation();
                FileConfiguration config = jailPlugin.getConfig();

                config.set(path + ".world", Objects.requireNonNull(loc.getWorld()).getName());
                config.set(path + ".x", loc.getX());
                config.set(path + ".y", loc.getY());
                config.set(path + ".z", loc.getZ());
                config.set(path + ".yaw", loc.getYaw());
                config.set(path + ".pitch", loc.getPitch());

                jailPlugin.saveConfig();

                commandSender.sendMessage("§7Location " + args[0] + " saved.");
            } else {
                commandSender.sendMessage("§7Not enough arguments.");
            }
        }
        return false;
    }
}
