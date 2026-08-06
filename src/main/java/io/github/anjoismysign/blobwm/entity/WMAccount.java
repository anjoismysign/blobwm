package io.github.anjoismysign.blobwm.entity;

import io.github.anjoismysign.bloblib.storage.AccountCrudable;

public class WMAccount extends AccountCrudable<WMProfile> {
    public WMAccount(String identification) {
        super(identification);
    }
}
