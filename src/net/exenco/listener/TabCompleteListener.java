package net.exenco.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

public class TabCompleteListener implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        if(event.getBuffer().equalsIgnoreCase("/jail ")) {
            event.getCompletions().clear();
            event.getCompletions().add("1");
            event.getCompletions().add("2");
        }
    }

}
