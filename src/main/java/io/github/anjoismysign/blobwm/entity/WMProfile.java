package io.github.anjoismysign.blobwm.entity;

import io.github.anjoismysign.bloblib.entities.PlayerDecorator;
import io.github.anjoismysign.bloblib.entities.PlayerDecoratorAware;
import io.github.anjoismysign.blobwm.configuration.DefaultPlayerData;
import io.github.anjoismysign.blobwm.director.manager.WMConfigurationManager;
import io.github.anjoismysign.psa.PostLoadable;
import io.github.anjoismysign.psa.crud.Crudable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class WMProfile implements Crudable, PlayerDecoratorAware, PostLoadable {

    private final String identification;
    private final Map<String, Integer> ammoPouch;

    private transient @Nullable PlayerDecorator playerDecorator;

    public WMProfile(String identification) {
        this.identification = identification;
        this.ammoPouch = new HashMap<>();
        DefaultPlayerData defaults = WMConfigurationManager.getConfiguration().getDefaultPlayerData();
        defaults.getAmmoPouch().forEach(this.ammoPouch::putIfAbsent);
        onPostLoad();
    }

    @Override
    public void onPostLoad() {
    }

    @Override
    public void setPlayerDecorator(@NotNull PlayerDecorator playerDecorator) {
        if (this.playerDecorator != null){
            return;
        }
        this.playerDecorator = playerDecorator;
    }

    @Nullable
    public Player player() {
        return playerDecorator == null ? null : playerDecorator.lookup();
    }

    @NotNull
    public Map<String, Integer> getAmmoPouch() {
        return ammoPouch;
    }

    public void addAmmo(String ammoType, int amount){
        ammoPouch.merge(ammoType, amount, Integer::sum);
    }

    public void removeAmmo(String ammoType, int amount) {
        if (amount <= 0) {
            return;
        }
        ammoPouch.computeIfPresent(ammoType, (k, existing) ->
                existing > amount ? existing - amount : null
        );
    }

    public int getAmmo(String ammoType){
        return ammoPouch.getOrDefault(ammoType, 0);
    }

    @Override
    public @NotNull String getIdentification() {
        return identification;
    }

}