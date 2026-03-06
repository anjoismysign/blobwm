package io.github.anjoismysign.blobwm.entity;

import io.github.anjoismysign.blobwm.director.manager.ConfigurationManager;
import org.jetbrains.annotations.Nullable;

public class AmmoBox {
    private String identifier;
    private int amount;
    private String ammoType;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAmmoType() {
        return ammoType;
    }

    public void setAmmoType(String ammoType) {
        this.ammoType = ammoType;
    }

    @Nullable
    public AmmoType toAmmoType(){
        return ConfigurationManager.getConfiguration().getAmmoTypes()
                .stream()
                .filter(type->type.getIdentifier().equals(ammoType))
                .findFirst()
                .orElse(null);
    }
}
