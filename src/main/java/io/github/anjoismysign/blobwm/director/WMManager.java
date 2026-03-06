package io.github.anjoismysign.blobwm.director;

import io.github.anjoismysign.bloblib.entities.GenericManager;
import io.github.anjoismysign.blobwm.BlobWM;

public class WMManager extends GenericManager<BlobWM, WMManagerDirector> {
    public WMManager(WMManagerDirector managerDirector) {
        super(managerDirector);
    }
}