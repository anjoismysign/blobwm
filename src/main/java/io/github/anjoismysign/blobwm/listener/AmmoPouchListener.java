package io.github.anjoismysign.blobwm.listener;

import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.bloblib.utilities.ItemStackUtil;
import io.github.anjoismysign.bloblib.weaponmechanics.WeaponInfoDisplay;
import io.github.anjoismysign.blobwm.BlobWM;
import io.github.anjoismysign.blobwm.director.manager.WMListenerManager;
import io.github.anjoismysign.blobwm.entity.AmmoPouch;
import io.github.anjoismysign.blobwm.entity.AmmoType;
import me.deecaad.weaponmechanics.WeaponMechanics;
import me.deecaad.weaponmechanics.utils.CustomTag;
import me.deecaad.weaponmechanics.weapon.reload.ReloadHandler;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponReloadCompleteEvent;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponReloadEvent;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponShootEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class AmmoPouchListener extends WMListener {
    private static final BlobWM PLUGIN = BlobWM.getInstance();

    public AmmoPouchListener(WMListenerManager listenerManager) {
        super(listenerManager);
    }

    @EventHandler(ignoreCancelled = true)
    public void onReload(WeaponReloadEvent event){
        LivingEntity shooter = event.getShooter();
        if (shooter.getType() != EntityType.PLAYER){
            return;
        }
        Player player = (Player) shooter;
        String weaponTitle = event.getWeaponTitle();
        @Nullable AmmoType ammoType = PLUGIN.getAmmoType(weaponTitle);
        if (ammoType == null){
            return;
        }
        ReloadHandler reloadHandler = WeaponMechanics.getInstance().getWeaponHandler().getReloadHandler();
        int ammoLeft = reloadHandler.getAmmoLeft(event.getWeaponStack(), weaponTitle);
        if (ammoLeft == -1){
            return;
        }
        @Nullable AmmoPouch ammoPouch = PLUGIN.getAmmoPouch(player, ammoType);
        if (ammoPouch == null){
            return;
        }
        int inPouch = ammoPouch.getAmmo();
        if (inPouch <= 0) {
            event.setCancelled(true);
            return;
        }
        int amountNeeded = event.getAmmoPerReload();
        int amountToTake = Math.min(amountNeeded, inPouch);
        ammoPouch.subtractAmmo(amountToTake);
        if (amountToTake < amountNeeded) {
            event.setAmmoPerReload(amountToTake);
        }
    }

    @EventHandler
    public void onReloadComplete(WeaponReloadCompleteEvent event){
        LivingEntity shooter = event.getShooter();
        if (shooter.getType() != EntityType.PLAYER){
            return;
        }
        Player player = (Player) shooter;
        String weaponTitle = event.getWeaponTitle();
        @Nullable String identifier = WeaponInfoDisplay.INSTANCE.map().get(weaponTitle);
        if (identifier == null){
            return;
        }
        @Nullable TranslatableItem item = TranslatableItem.by(identifier);
        if (item == null){
            return;
        }
        ItemStack weaponStack = event.getWeaponStack();
        CustomTag tag = CustomTag.AMMO_LEFT;
        int leftAmmo = tag.hasInteger(weaponStack) ?
                tag.getInteger(weaponStack): 0;
        int inPouch = -1;
        @Nullable AmmoType ammoType = PLUGIN.getAmmoType(weaponTitle);
        if (ammoType != null){
            @Nullable AmmoPouch ammoPouch = PLUGIN.getAmmoPouch(player, ammoType);
            if (ammoPouch != null){
                inPouch = ammoPouch.getAmmo();
            }
        }
        String pouch = inPouch == -1 ? "∞" : String.valueOf(inPouch);
        String locale = player.getLocale();
        ItemStack itemStack = item.localize(locale).getClone();
        String title = ItemStackUtil.display(itemStack);
        BlobLibMessageAPI.getInstance()
                .getMessage("BlobWM.Weapon-Info-Display", player)
                .modder()
                .replace("%name%", title)
                .replace("%ammo%", leftAmmo+"")
                .replace("%pouch%", pouch)
                .get()
                .handle(player);
    }

    @EventHandler
    public void onShoot(WeaponShootEvent event){
        LivingEntity shooter = event.getShooter();
        if (shooter.getType() != EntityType.PLAYER){
            return;
        }
        Player player = (Player) shooter;
        String weaponTitle = event.getWeaponTitle();
        @Nullable String identifier = WeaponInfoDisplay.INSTANCE.map().get(weaponTitle);
        if (identifier == null){
            return;
        }
        @Nullable TranslatableItem item = TranslatableItem.by(identifier);
        if (item == null){
            return;
        }
        ItemStack weaponStack = event.getWeaponStack();
        CustomTag tag = CustomTag.AMMO_LEFT;
        int leftAmmo = tag.hasInteger(weaponStack) ?
                tag.getInteger(weaponStack): 0;
        int inPouch = -1;
        @Nullable AmmoType ammoType = PLUGIN.getAmmoType(weaponTitle);
        if (ammoType != null){
            @Nullable AmmoPouch ammoPouch = PLUGIN.getAmmoPouch(player, ammoType);
            if (ammoPouch != null){
                inPouch = ammoPouch.getAmmo();
            }
        }
        String pouch = inPouch == -1 ? "∞" : String.valueOf(inPouch);
        String locale = player.getLocale();
        ItemStack itemStack = item.localize(locale).getClone();
        String title = ItemStackUtil.display(itemStack);
        BlobLibMessageAPI.getInstance()
                .getMessage("BlobWM.Weapon-Info-Display", player)
                .modder()
                .replace("%name%", title)
                .replace("%ammo%", leftAmmo+"")
                .replace("%pouch%", pouch)
                .get()
                .handle(player);
    }

    @Override
    public boolean checkIfShouldRegister() {
        return true;
    }
}
