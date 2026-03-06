package io.github.anjoismysign.blobwm.director;

import io.github.anjoismysign.bloblib.entities.GenericManagerDirector;
import io.github.anjoismysign.blobwm.BlobWM;
import io.github.anjoismysign.blobwm.director.manager.ConfigurationManager;
import io.github.anjoismysign.blobwm.director.manager.WMListenerManager;
import org.jetbrains.annotations.NotNull;

public class WMManagerDirector extends GenericManagerDirector<BlobWM> {
    public WMManagerDirector(BlobWM plugin) {
        super(plugin);
        addManager("ConfigurationManager",
                new ConfigurationManager(this));
        addManager("ListenerManager",
                new WMListenerManager(this));
    }

    /**
     * From top to bottom, follow the order.
     */
    @Override
    public void reload() {
        getConfigurationManager().reload();
        getListenerManager().reload();
    }

    @Override
    public void unload() {
    }

    @NotNull
    public final ConfigurationManager getConfigurationManager() {
        return getManager("ConfigurationManager", ConfigurationManager.class);
    }

    @NotNull
    public final WMListenerManager getListenerManager(){
        return getManager("ListenerManager", WMListenerManager.class);
    }
}