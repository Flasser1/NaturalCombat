package dk.flasser.naturalcombat.listeners;

import dk.flasser.naturalcombat.NaturalCombat;
import dk.flasser.naturalcombat.ulility.SetCombatUtil;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@Component
public class DamageListener implements Listener {

    private @Inject NaturalCombat instance;
    private @Inject SetCombatUtil setCombatUtil;

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player victim = (Player) e.getEntity();
        if (!(e.getDamager() instanceof Player)) {
            if (!instance.getConfig().getBoolean("EnvironmentalCombat")) return;
            setCombatUtil.setCombat(victim, instance.getConfig().getInt("BaseCombat"));
        }

        Player attacker = (Player) e.getDamager();

        if (attacker.equals(victim)) {
            if (!instance.getConfig().getBoolean("SelfCombat")) return;
            setCombatUtil.setCombat(victim, instance.getConfig().getInt("BaseCombat"));
        }

        setCombatUtil.setCombat(victim, instance.getConfig().getInt("BaseCombat"));
        setCombatUtil.setCombat(attacker, instance.getConfig().getInt("BaseCombat"));
    }
}
