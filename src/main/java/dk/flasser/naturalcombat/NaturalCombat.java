package dk.flasser.naturalcombat;

import dk.flasser.naturalcombat.managers.FileManager;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.OkaeriBukkitPlugin;
import eu.okaeri.platform.core.annotation.Scan;
import eu.okaeri.platform.core.plan.ExecutionPhase;
import eu.okaeri.platform.core.plan.Planned;
import org.bstats.bukkit.Metrics;

@Scan(exclusions = "me.reb4ck.tutorial.libs", deep = true)
public final class NaturalCombat extends OkaeriBukkitPlugin {
    @Inject
    private FileManager fileManager;

    public NaturalCombat instance;

    @Planned(ExecutionPhase.STARTUP)
    public void onStartup() {
        this.getLogger().info("NATURALCOMBAT: STARTUP");

        instance = this;

        saveDefaultConfig();
        fileManager.createMessages();

        int pluginId = 25000;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Planned(ExecutionPhase.SHUTDOWN)
    public void onShutdown() {
        this.getLogger().info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));

    }
}
