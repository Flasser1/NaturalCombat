package dk.flasser.naturalcombat.ulility;

import dk.flasser.naturalcombat.NaturalCombat;
import dk.flasser.naturalcombat.managers.FileManager;
import dk.flasser.naturalcombat.ulility.misc.ActionbarUtil;
import dk.flasser.naturalcombat.ulility.misc.MetadataUtil;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

@Component
public class SetCombatUtil {

    private @Inject NaturalCombat instance;
    private @Inject FileManager fileManager;
    private @Inject MetadataUtil metadataUtil;
    private @Inject ActionbarUtil actionbarUtil;

    public void setCombat(Player player, Integer time) {
        if (player.hasMetadata("combat")) {
            Bukkit.broadcastMessage("1");
            if ((Integer) metadataUtil.getMetadata(player, "combat") <= instance.getConfig().getInt("BaseCombat")) {
                Bukkit.broadcastMessage("2");
                player.setMetadata("combat", new FixedMetadataValue(instance, time));
            }
            Bukkit.broadcastMessage("3");
            return;
        }

        Bukkit.broadcastMessage("4");
        player.setMetadata("combat", new FixedMetadataValue(instance, time));

        Bukkit.broadcastMessage("5");
        new BukkitRunnable() {
            @Override
            public void run() {
                Integer combat = (Integer) metadataUtil.getMetadata(player, "combat");

                player.setMetadata("combat", new FixedMetadataValue(instance, combat - 1));

                if (combat <= 0) {
                    player.removeMetadata("combat", instance);
                    actionbarUtil.sendActionBar(player, fileManager.getMessage("combat_expired"));
                    cancel();
                }

                actionbarUtil.sendActionBar(player, fileManager.getMessage("combat_timer")
                        .replace("{time}", String.valueOf(combat)));
            }
        }.runTaskTimer(instance, 0L, 20L);
    }
}