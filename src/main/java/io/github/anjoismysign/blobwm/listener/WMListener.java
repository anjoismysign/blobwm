package io.github.anjoismysign.blobwm.listener;

import io.github.anjoismysign.bloblib.entities.BlobListener;
import io.github.anjoismysign.blobwm.director.manager.WMListenerManager;

public abstract class WMListener implements BlobListener {
    private final WMListenerManager listenerManager;

    public WMListener(WMListenerManager listenerManager) {
        this.listenerManager = listenerManager;
    }

    public WMListenerManager getListenerManager() {
        return listenerManager;
    }
}
