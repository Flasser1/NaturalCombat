package dk.flasser.naturalcombat.ulility.misc;

import dk.flasser.naturalcombat.NaturalCombat;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class MetadataUtil {
    @Inject
    public NaturalCombat instance;

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
