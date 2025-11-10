package dk.flasser.naturalcombat.listeners;

import dk.flasser.naturalcombat.NaturalCombat;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@Component
public class DeathListener implements Listener {
    @Inject
    private NaturalCombat instance;

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if (player.hasMetadata("combat")) {
            player.removeMetadata("combat", instance);
        }
    }
}
