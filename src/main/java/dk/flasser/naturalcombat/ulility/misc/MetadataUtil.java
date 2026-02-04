package dk.flasser.naturalcombat.ulility.misc;

import dk.flasser.naturalcombat.NaturalCombat;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

@Component
public class MetadataUtil {

    private @Inject NaturalCombat instance;

    public Object getMetadata(Entity entity, String metadata) {
        List<MetadataValue> metas = entity.getMetadata(metadata);

        for (MetadataValue mv : metas) {
            if (mv.getOwningPlugin().equals(instance)) {
                return mv.value();
            }
        }
        return null;
    }
}
