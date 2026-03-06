package io.github.anjoismysign.blobwm.director.manager;

import io.github.anjoismysign.bloblib.entities.ListenerManager;
import io.github.anjoismysign.blobwm.director.WMManagerDirector;
import io.github.anjoismysign.blobwm.listener.AmmoBoxListener;
import io.github.anjoismysign.blobwm.listener.AmmoPouchListener;

public class WMListenerManager extends ListenerManager {
    public WMListenerManager(WMManagerDirector managerDirector) {
        super(managerDirector);
        add(new AmmoPouchListener(this));
        add(new AmmoBoxListener(this));

        reload();
    }
}
