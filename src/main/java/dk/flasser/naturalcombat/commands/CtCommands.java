package dk.flasser.naturalcombat.commands;

import dk.flasser.naturalcombat.NaturalCombat;
import dk.flasser.naturalcombat.ulility.misc.MetadataUtil;
import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;

@Async
@Component
@Command(
        label = "ct",
        aliases = {"combat", "combattime", "cttime", "combatt"}
)
public class CtCommands {
    @Inject
    private NaturalCombat instance;
    private MetadataUtil metadataUtil;

    @Executor(pattern = "")
    public void defaultCommand(Player sender) {
        sender.sendMessage("Combat Things");
    }
}
