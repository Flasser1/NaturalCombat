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
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Component
@Async
@Command(
        label = "combatadmin",
        aliases = {"cta", "ctadmin", "combata"}
)
@Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta"})
public class CtaCommands implements CommandService {
    @Inject
    private NaturalCombat instance;
    @Inject
    private FileManager fileManager;
    @Inject
    private MetadataUtil metadataUtil;
    @Inject
    private SetCombatUtil setCombatUtil;

    @Executor(pattern = "")
    public void defaultCommand(CommandSender sender) {
        sender.sendMessage("Combat Things");
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    @Executor(pattern = "reload")
    public void reload(CommandSender sender) {
        instance.reloadConfig();
        fileManager.createMessages();
        sender.sendMessage(fileManager.getMessage("reload_success"));
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.delete"})
    @Executor(pattern = "delete <player>")
    public void delete(CommandSender sender, @Arg("player") Player target) {
        if (!target.isOnline()) {
            sender.sendMessage(fileManager.getMessage("player_not_online").replace("{player}", target.getName()));
            return;
        }

        target.removeMetadata("combat", instance);
        sender.sendMessage(fileManager.getMessage("delete_success").replace("{player}", target.getName()));
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.status"})
    @Executor(pattern = "status <player>")
    public void status(CommandSender sender, @Arg("player") Player target) {
        if (!target.isOnline()) {
            sender.sendMessage(fileManager.getMessage("player_not_online").replace("{player}", target.getName()));
            return;
        }

        sender.sendMessage(target.hasMetadata("combat")
                ? fileManager.getMessage("status_success_true").replace("{player}", target.getName()).replace("{time}", String.valueOf(metadataUtil.getMetadata(target, "combat")))
                : fileManager.getMessage("status_success_false").replace("{player}", target.getName())
        );
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.add"})
    @Executor(pattern = "add <player> <time>")
    public void add(CommandSender sender, @Arg("player") Player target, @Arg("time") Integer time) {
        if (!target.isOnline()) {
            sender.sendMessage(fileManager.getMessage("player_not_online").replace("{player}", target.getName()));
            return;
        }

        Integer combat = (Integer) metadataUtil.getMetadata(target, "combat");
        if (combat == null) combat = 0;
        setCombatUtil.setCombat(target, combat+time);
        sender.sendMessage(fileManager.getMessage("add_success").replace("{player}", target.getName()).replace("{time}", String.valueOf(metadataUtil.getMetadata(target, "combat"))));
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.remove"})
    @Executor(pattern = "remove <player> <time>")
    public void remove(CommandSender sender, @Arg("player") Player target, @Arg("time") Integer time) {
        if (!target.isOnline()) {
            sender.sendMessage(fileManager.getMessage("player_not_online").replace("{player}", target.getName()));
            return;
        }

        Integer combat = (Integer) metadataUtil.getMetadata(target, "combat");
        if (combat == null) {
            fileManager.getMessage("combat_false");
            return;
        }
        setCombatUtil.setCombat(target, combat-time);
        sender.sendMessage(fileManager.getMessage("remove_success").replace("{player}", target.getName()).replace("{time}", String.valueOf(metadataUtil.getMetadata(target, "combat"))));
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.set"})
    @Executor(pattern = "set <player> <time>")
    public void set(CommandSender sender, @Arg("player") Player target, @Arg("time") Integer time) {
        if (!target.isOnline()) {
            sender.sendMessage(fileManager.getMessage("player_not_online").replace("{player}", target.getName()));
            return;
        }

        setCombatUtil.setCombat(target, time);
        sender.sendMessage(fileManager.getMessage("set_success").replace("{player}", target.getName()).replace("{time}", String.valueOf(metadataUtil.getMetadata(target, "combat"))));
    }

    @Completion(arg = "time", value = "time")
    public Integer[] completeTime(CommandSender sender, @Arg("time") Integer arg) {
        return new Integer[]{5, 10, 15};
    }
}