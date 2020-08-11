package net.exenco.listener;

import net.exenco.core.JailPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandPreProcessListener implements Listener {

    JailPlugin jailPlugin;
    List<String> commandList;
    public CommandPreProcessListener(JailPlugin jailPlugin) {
        this.jailPlugin = jailPlugin;
        FileConfiguration config = jailPlugin.getConfig();
        commandList = config.getStringList("Jail.Commands");
    }

    @EventHandler
    public void onExecute(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if(player.isOp() || player.hasPermission("jail.override.execute")) {
            return;
        }

        if(jailPlugin.isInJail(player.getLocation())) {
            boolean contains = false;
            for(String command : commandList) {
                if(event.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())) {
                    contains = true;
                }
            }
            event.setCancelled(contains);
            if(contains)
                player.sendMessage("§7You are not allowed to execute that command here.");
        }
    }
}

