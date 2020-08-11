package net.exenco.listener;

import net.exenco.core.JailPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockPlaceListener implements Listener {

    JailPlugin jailPlugin;
    List<String> blockList;
    public BlockPlaceListener(JailPlugin jailPlugin) {
        this.jailPlugin = jailPlugin;
        FileConfiguration config = jailPlugin.getConfig();
        blockList = config.getStringList("Jail.Blocks");
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if(player.isOp() || player.hasPermission("jail.override.place")) {
            return;
        }

        if(jailPlugin.isInJail(player.getLocation())) {
            boolean contains = false;
            for(String block : blockList) {
                if(event.getBlock().getType().toString().equalsIgnoreCase(block)) {
                    contains = true;
                }
            }
            event.setCancelled(!contains);
            if(!contains)
                player.sendMessage("§7You are not allowed to place that block here.");
        }
    }
}
