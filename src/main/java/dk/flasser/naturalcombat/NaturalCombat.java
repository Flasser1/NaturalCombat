package dk.flasser.naturalcombat;

import dk.flasser.naturalcombat.managers.FileManager;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.OkaeriBukkitPlugin;
import eu.okaeri.platform.core.annotation.Scan;
import eu.okaeri.platform.core.plan.ExecutionPhase;
import eu.okaeri.platform.core.plan.Planned;
import org.bstats.bukkit.Metrics;

@Scan(deep = true)
public final class NaturalCombat extends OkaeriBukkitPlugin {

    private @Inject FileManager fileManager;

    @Planned(ExecutionPhase.STARTUP)
    public void onStartup() {
        getLogger().info("NATURALCOMBAT: STARTING UP");

        saveDefaultConfig();
    }

    @Planned(ExecutionPhase.POST_SETUP)
    public void afterSetup() {
        fileManager.createMessages();
        getLogger().info("NATURALCOMBAT: Messages file loaded.");

        int pluginId = 25000;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Planned(ExecutionPhase.SHUTDOWN)
    public void onShutdown() {
        this.getLogger().info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));

    }
}
