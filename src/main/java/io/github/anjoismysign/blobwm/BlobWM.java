package io.github.anjoismysign.blobwm;

import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.managers.BlobPlugin;
import io.github.anjoismysign.bloblib.managers.cruder.AccountCruder;
import io.github.anjoismysign.blobwm.director.WMManagerDirector;
import io.github.anjoismysign.blobwm.director.manager.ConfigurationManager;
import io.github.anjoismysign.blobwm.entity.AmmoBox;
import io.github.anjoismysign.blobwm.entity.AmmoPouch;
import io.github.anjoismysign.blobwm.entity.AmmoType;
import io.github.anjoismysign.blobwm.entity.WMAccount;
import io.github.anjoismysign.blobwm.entity.WMProfile;
import io.github.anjoismysign.blobwm.handler.PlayerHandler;
import io.github.anjoismysign.skeramidcommands.command.Command;
import io.github.anjoismysign.skeramidcommands.command.CommandBuilder;
import io.github.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import io.github.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public final class BlobWM extends BlobPlugin implements BlobWMAPI {
    private static BlobWM INSTANCE;

    public static BlobWM getInstance() {
        return INSTANCE;
    }

    private WMManagerDirector director;
    private AccountCruder<WMAccount, WMProfile> accountCruder;

    @Override
    public void onEnable() {
        INSTANCE = this;
        director = new WMManagerDirector(this);

        Bukkit.getScheduler().runTask(this, ()->{
            accountCruder = new AccountCruder<>(this, WMAccount.class, WMProfile.class);
        });

        Command command = CommandBuilder.of("blobwm").build();
        Command open = command.child("open");
        Command ammoPouch = open.child("ammopouch");
        var onlinePlayers = BukkitCommandTarget.ONLINE_PLAYERS();
        ammoPouch.setParameters(onlinePlayers);
        ammoPouch.onExecute((permissionMessenger, args) -> {
            CommandSender sender = BukkitAdapter.getInstance().of(permissionMessenger);
            Player player = BukkitCommandTarget.ONLINE_PLAYERS().parse(args[0]);
            if (player == null) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("Player.Not-Found", sender)
                        .toCommandSender(sender);
                return;
            }
            new PlayerHandler(player).openAmmoPouch();
        });
    }

    @Override
    public void onDisable(){
        super.onDisable();
        accountCruder.shutdown();
    }

    public WMManagerDirector getManagerDirector() {
        return director;
    }

    @Nullable
    public AmmoPouch getAmmoPouch(Player player, AmmoType ammoType) {
        @Nullable WMProfile profile = accountCruder.getAccount(player);
        if (profile == null){
            return null;
        }
        String identifier = ammoType.getIdentifier();
        return new AmmoPouch() {
            @Override
            public int getAmmo() {
                return profile.getAmmo(identifier);
            }

            @Override
            public void addAmmo(int amount) {
                profile.addAmmo(identifier, amount);
            }

            @Override
            public void subtractAmmo(int amount) {
                profile.removeAmmo(identifier, amount);
            }
        };
    }

    @Override
    public boolean addAmmo(Player player, AmmoBox ammoBox) {
        @Nullable AmmoType ammoType = ammoBox.toAmmoType();
        if (ammoType == null){
            return false;
        }
        @Nullable AmmoPouch ammoPouch = getAmmoPouch(player, ammoType);
        if (ammoPouch == null){
            return false;
        }
        ammoPouch.addAmmo(ammoBox.getAmount());
        return true;
    }

    @Nullable
    public AmmoType getAmmoType(String weaponTitle) {
        return ConfigurationManager.getConfiguration().getAmmoType(weaponTitle);
    }
}
