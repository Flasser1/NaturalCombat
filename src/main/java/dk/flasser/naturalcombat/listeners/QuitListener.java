package dk.flasser.naturalcombat.listeners;

import dk.flasser.naturalcombat.NaturalCombat;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@Component
public class QuitListener implements Listener {
    @Inject
    private NaturalCombat instance;

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (!(player.hasMetadata("combat"))) return;

        player.setHealth(0);

        if (instance.getConfig().getBoolean("AnnounceCombatDeaths")) {
            Bukkit.broadcastMessage("Combat Død");
        }
    }
}
