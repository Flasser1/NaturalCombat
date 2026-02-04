package dk.flasser.naturalcombat.commands;

import dk.flasser.naturalcombat.NaturalCombat;
import dk.flasser.naturalcombat.managers.FileManager;
import dk.flasser.naturalcombat.ulility.SetCombatUtil;
import dk.flasser.naturalcombat.ulility.misc.MetadataUtil;

import eu.okaeri.commands.annotation.Arg;
import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Context;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

@Component
@Async
@Command(
        label = "combatadmin",
        aliases = {"cta", "ctadmin", "combata"}
)
@Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta"})
public class CtaCommands implements CommandService {
    
    private @Inject NaturalCombat instance;
    private @Inject FileManager fileManager;
    private @Inject MetadataUtil metadataUtil;
    private @Inject SetCombatUtil setCombatUtil;

    private final List<SubCommand> subCommands = new ArrayList<>();

    @PostConstruct
    public void init() {
        subCommands.add(new SubCommand("reload", "Reload the plugin", "reload"));
        subCommands.add(new SubCommand("delete <player>", "Delete combat tag", "delete"));
        subCommands.add(new SubCommand("status <player>", "Check combat status", "status"));
        subCommands.add(new SubCommand("add <player> <time>", "Add combat time", "add"));
        subCommands.add(new SubCommand("remove <player> <time>", "Remove combat time", "remove"));
        subCommands.add(new SubCommand("set <player> <time>", "Set combat time", "set"));
    }

    @Executor(pattern = "")
    public void _def(@Context Player player) {
        player.sendMessage(new String[]{"&8[ &c&lCombat &f&lSystem &8]", ""});
        for (SubCommand sub : subCommands) {
            if (player.hasPermission("naturalcombat.cta." + sub.getPerm())
                    || player.hasPermission("naturalcombat.cta.*")
                    || player.hasPermission("naturalcombat.*")
                    || player.hasPermission("naturalstuff.*")) {
                player.sendMessage("§8│ §f/" + sub.getUsage() + " §8›› §7" + sub.getDescription());
            }
        }
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    @Executor(pattern = "reload")
    public void reload(@Context Player player) {
        instance.reloadConfig();
        fileManager.createMessages();
        player.sendMessage(fileManager.getMessage("reload_success"));
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.delete"})
    @Executor(pattern = "delete <player>")
    public void delete(@Context Player player, @Arg("player") Player target) {
        if (!target.isOnline()) {
            player.sendMessage(fileManager.getMessage("player_not_online").replace("{player}", target.getName()));
            return;
        }

        target.removeMetadata("combat", instance);
        player.sendMessage(fileManager.getMessage("delete_success").replace("{player}", target.getName()));
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.status"})
    @Executor(pattern = "status <player>")
    public void status(@Context Player player, @Arg("player") Player target) {
        if (!target.isOnline()) {
            player.sendMessage(fileManager.getMessage("player_not_online").replace("{player}", target.getName()));
            return;
        }

        player.sendMessage(target.hasMetadata("combat")
                ? fileManager.getMessage("status_success_true").replace("{player}", target.getName()).replace("{time}", String.valueOf(metadataUtil.getMetadata(target, "combat")))
                : fileManager.getMessage("status_success_false").replace("{player}", target.getName())
        );
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.add"})
    @Executor(pattern = "add <player> <time>")
    public void add(@Context Player player, @Arg("player") Player target, @Arg("time") Integer time) {
        if (!target.isOnline()) {
            player.sendMessage(fileManager.getMessage("player_not_online").replace("{player}", target.getName()));
            return;
        }

        Integer combat = (Integer) metadataUtil.getMetadata(target, "combat");
        if (combat == null) combat = 0;
        setCombatUtil.setCombat(target, combat+time);
        player.sendMessage(fileManager.getMessage("add_success").replace("{player}", target.getName()).replace("{time}", String.valueOf(metadataUtil.getMetadata(target, "combat"))));
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.remove"})
    @Executor(pattern = "remove <player> <time>")
    public void remove(@Context Player player, @Arg("player") Player target, @Arg("time") Integer time) {
        if (!target.isOnline()) {
            player.sendMessage(fileManager.getMessage("player_not_online").replace("{player}", target.getName()));
            return;
        }

        Integer combat = (Integer) metadataUtil.getMetadata(target, "combat");
        if (combat == null) {
            fileManager.getMessage("combat_false");
            return;
        }
        target.setMetadata("combat", new FixedMetadataValue(instance, combat-time));
        player.sendMessage(fileManager.getMessage("remove_success").replace("{player}", target.getName()).replace("{time}", String.valueOf(metadataUtil.getMetadata(target, "combat"))));
    }

    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.set"})
    @Executor(pattern = "set <player> <time>")
    public void set(@Context Player player, @Arg("player") Player target, @Arg("time") Integer time) {
        if (!target.isOnline()) {
            player.sendMessage(fileManager.getMessage("player_not_online").replace("{player}", target.getName()));
            return;
        }

        if (metadataUtil.getMetadata(target, "combat") == null) {
            setCombatUtil.setCombat(target, time);
        } else {
            target.setMetadata("combat", new FixedMetadataValue(instance, time));
        }

        player.sendMessage(fileManager.getMessage("set_success").replace("{player}", target.getName()).replace("{time}", String.valueOf(metadataUtil.getMetadata(target, "combat"))));
    }

    public class SubCommand {
        private final String usage;
        private final String description;
        private final String perm;

        public SubCommand(String usage, String description, String perm) {
            this.usage = usage;
            this.description = description;
            this.perm = perm;
        }

        public String getUsage() {
            return usage;
        }

        public String getDescription() {
            return description;
        }

        public String getPerm() {
            return perm;
        }
    }
}