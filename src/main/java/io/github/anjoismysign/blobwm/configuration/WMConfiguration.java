package io.github.anjoismysign.blobwm.configuration;

import io.github.anjoismysign.blobwm.entity.AmmoBox;
import io.github.anjoismysign.blobwm.entity.AmmoType;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class WMConfiguration {
    private boolean tinyDebug;
    private Set<AmmoType> ammoTypes;
    private Set<AmmoBox> ammoBoxes;
    private DefaultPlayerData defaultPlayerData;

    WMConfiguration(){}

    public boolean isTinyDebug() {
        return tinyDebug;
    }

    public void setTinyDebug(boolean tinyDebug) {
        this.tinyDebug = tinyDebug;
    }

    public Set<AmmoType> getAmmoTypes() {
        return ammoTypes;
    }

    public void setAmmoTypes(Set<AmmoType> ammoTypes) {
        this.ammoTypes = ammoTypes;
    }

    public Set<AmmoBox> getAmmoBoxes() {
        return ammoBoxes;
    }

    public void setAmmoBoxes(Set<AmmoBox> ammoBoxes) {
        this.ammoBoxes = ammoBoxes;
    }

    public DefaultPlayerData getDefaultPlayerData() {
        return defaultPlayerData;
    }

    public void setDefaultPlayerData(DefaultPlayerData defaultPlayerData) {
        this.defaultPlayerData = defaultPlayerData;
    }

    @Nullable
    public AmmoType getAmmoType(String weaponTitle){
        return ammoTypes
                .stream()
                .filter(ammoType -> ammoType.getWeapons().contains(weaponTitle))
                .findFirst()
                .orElse(null);
    }
}
