package dk.flasser.naturalcombat.ulility;

import dk.flasser.naturalcombat.NaturalCombat;
import dk.flasser.naturalcombat.ulility.misc.ActionbarUtil;
import dk.flasser.naturalcombat.ulility.misc.MetadataUtil;

import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class SetCombatUtil {
    @Inject
    private NaturalCombat instance;
    private MetadataUtil metadataUtil;
    private ActionbarUtil actionbarUtil;

    @Async
    public void setCombat(Player player, Integer time) {
        if (player.hasMetadata("combat")) {
            if ((Integer) metadataUtil.getMetadata(player, "combat") <= instance.getConfig().getInt("BaseCombat")) {
                player.setMetadata("combat", new FixedMetadataValue(instance, time));
            }
            return;
        }

        player.setMetadata("combat", new FixedMetadataValue(instance, time));

        new BukkitRunnable() {
            @Override
            public void run() {
                Integer combat = (Integer) metadataUtil.getMetadata(player, "combat");

                if (combat <= 0) {
                    player.removeMetadata("combat", instance);
                    actionbarUtil.sendActionBar(player, "Combat Gone");
                    cancel();
                }

                actionbarUtil.sendActionBar(player, "Combat Set");

                player.setMetadata("combat", new FixedMetadataValue(instance, combat - 1));
            }
        }.runTaskTimer(instance, 0L, 20L);
    }
}