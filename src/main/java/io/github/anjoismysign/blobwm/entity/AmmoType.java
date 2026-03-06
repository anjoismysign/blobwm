package io.github.anjoismysign.blobwm.entity;

import java.util.Set;

public class AmmoType {
    private String identifier;
    private Set<String> weapons;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Set<String> getWeapons() {
        return weapons;
    }

    public void setWeapons(Set<String> weapons) {
        this.weapons = weapons;
    }
}
