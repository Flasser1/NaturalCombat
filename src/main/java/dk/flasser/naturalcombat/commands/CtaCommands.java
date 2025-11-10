package dk.flasser.naturalcombat.commands;

import dk.flasser.naturalcombat.NaturalCombat;
import dk.flasser.naturalcombat.managers.FileManager;
import dk.flasser.naturalcombat.ulility.SetCombatUtil;
import dk.flasser.naturalcombat.ulility.misc.MetadataUtil;
import eu.okaeri.commands.annotation.Arg;
import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Completion;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;

@Async
@Component
@Command(
        label = "combatadmin",
        aliases = {"cta", "ctadmin", "combata"}
)
@Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta"})
public class CtaCommands {
    @Inject
    private NaturalCombat instance;
    private FileManager fileManager;
    private MetadataUtil metadataUtil;
    private SetCombatUtil setCombatUtil;

    @Executor(pattern = "")
    public void defaultCommand(Player sender) {
        sender.sendMessage("Combat Things");
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    @Executor(pattern = "reload")
    public void reload(Player sender) {
        sender.sendMessage("Reloading configs...");
        instance.reloadConfig();
        fileManager.createMessages();
        fileManager.createData();
        sender.sendMessage("Configs reloaded!");
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.delete"})
    @Executor(pattern = "delete <player>")
    public void delete(Player sender, @Arg("player") Player target) {
        target.removeMetadata("combat", instance);
        sender.sendMessage("Deleted player: " + target.getName());
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.status"})
    @Executor(pattern = "status <player>")
    public void status(Player sender, @Arg("player") Player target) {
        sender.sendMessage(sender.getName()+": " + metadataUtil.getMetadata(target, "combat"));
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.add"})
    @Executor(pattern = "add <player> <time>")
    public void add(Player sender, @Arg("player") Player target, @Arg("time") Integer time) {
        Integer combat = (Integer) metadataUtil.getMetadata(target, "combat");
        setCombatUtil.setCombat(target, combat+time);
        sender.sendMessage("Added time: " + target.getName());
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.remove"})
    @Executor(pattern = "remove <player> <time>")
    public void remove(Player sender, @Arg("player") Player target, @Arg("time") Integer time) {
        Integer combat = (Integer) metadataUtil.getMetadata(target, "combat");
        setCombatUtil.setCombat(target, combat-time);
        sender.sendMessage("Removed time: " + target.getName());
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.set"})
    @Executor(pattern = "set <player> <time>")
    public void set(Player sender, @Arg("player") Player target, @Arg("time") Integer time) {
        setCombatUtil.setCombat(target, time);
        sender.sendMessage("Set time: " + target.getName());
    }

    @Completion(arg = "time", value = "time")
    public Integer[] completeTime(Player sender, @Arg("time") Integer arg) {
        return new Integer[]{5, 10, 15};
    }
}