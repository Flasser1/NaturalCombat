package dk.flasser.naturalcombat.managers;

import dk.flasser.naturalcombat.NaturalCombat;

import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class FileManager {
    @Inject
    private NaturalCombat instance;

    private static File messagesFile;
    private static FileConfiguration messages;

    @Async
    public FileConfiguration getMessages() {
        return messages;
    }

    @Async
    public String getMessage(String path) {
        return messages.getString(path).replace("&", "§");
    }

    @Async
    public String[] getListMessage(String path) {
        return messages.getStringList(path).stream()
                .map(s -> s.replace("&", "§"))
                .toArray(String[]::new);
    }

    @Async
    public void createMessages() {
        messagesFile = new File(instance.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            instance.saveResource("messages.yml", false);
        }

        messages = new YamlConfiguration();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(messagesFile), StandardCharsets.UTF_8)) {
            messages.load(reader);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void createData() {
        messagesFile = new File(instance.getDataFolder(), "data.yml");
        if (!messagesFile.exists()) {
            instance.saveResource("data.yml", false);
        }

        messages = new YamlConfiguration();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(messagesFile), StandardCharsets.UTF_8)) {
            messages.load(reader);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}