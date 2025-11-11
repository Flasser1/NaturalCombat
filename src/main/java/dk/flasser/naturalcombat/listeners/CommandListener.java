package dk.flasser.naturalcombat.listeners;

import dk.flasser.naturalcombat.NaturalCombat;
import dk.flasser.naturalcombat.managers.FileManager;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

@Component
public class CommandListener implements Listener {
    @Inject
    private NaturalCombat instance;
    private FileManager fileManager;

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        List<String> blockedCmds = instance.getConfig().getStringList("Commands");

        if (blockedCmds.isEmpty()) return;

        if (blockedCmds.contains(e.getMessage().split(" ")[0].substring(1).toLowerCase())) {
            Player player = e.getPlayer();

            if (!(player.hasMetadata("combat"))) return;

            e.setCancelled(true);
            player.sendMessage(fileManager.getMessage("command_disallowed")
                    .replace("{command}", e.getMessage().split(" ")[0].substring(1).toLowerCase()));
        }
    }
}
