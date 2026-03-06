package io.github.anjoismysign.blobwm.handler;

import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.bloblib.middleman.itemstack.ItemStackBuilder;
import io.github.anjoismysign.bloblib.utilities.PlayerUtil;
import io.github.anjoismysign.blobwm.BlobWM;
import io.github.anjoismysign.blobwm.director.manager.WMConfigurationManager;
import io.github.anjoismysign.blobwm.entity.AmmoBox;
import io.github.anjoismysign.blobwm.entity.AmmoPouch;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public record PlayerHandler(Player player) {

    public void openAmmoPouch(){
        BlobLibInventoryAPI.getInstance().customSelector(
                "AmmoPouch",
                player,
                "White-Background",
                "AmmoBox",
                ()->{
                    return WMConfigurationManager.getConfiguration().getAmmoTypes().stream().toList();
                },
                ammoType -> {
                    BlobWM plugin = BlobWM.getInstance();
                    @Nullable AmmoPouch ammoPouch = plugin.getAmmoPouch(player, ammoType);
                    if (ammoPouch == null) {
                        player.closeInventory();
                        BlobLibMessageAPI.getInstance()
                                .getMessage("BlobWM.Player-Not-Cached", player)
                                .handle(player);
                        return;
                    }
                    String typeIdentifier = ammoType.getIdentifier();
                    int currentAmmo = ammoPouch.getAmmo();
                    @Nullable AmmoBox ammoBox = WMConfigurationManager.getConfiguration().getAmmoBoxes()
                            .stream()
                            .filter(box -> box.getAmmoType().equals(typeIdentifier))
                            .filter(box -> box.getAmount() <= currentAmmo)
                            .max(Comparator.comparingInt(AmmoBox::getAmount))
                            .orElse(null);
                    if (ammoBox == null){
                        player.closeInventory();
                        BlobLibMessageAPI.getInstance()
                                .getMessage("BlobWM.Insufficient-Ammo", player)
                                .handle(player);
                        return;
                    }
                    String boxIdentifier = ammoBox.getIdentifier();
                    @Nullable TranslatableItem translatableItem = TranslatableItem.by(boxIdentifier);
                    if (translatableItem == null){
                        player.closeInventory();
                        player.sendMessage("This ammo box ("+boxIdentifier+") has no tangible item configured. Contact staff");
                        return;
                    }
                    ItemStack clone = translatableItem.localize(player).getClone();
                    PlayerUtil.giveItemToInventoryOrDrop(player, clone);
                    ammoPouch.subtractAmmo(ammoBox.getAmount());
                    openAmmoPouch();
                },
                ammoType -> {
                    String identifier = ammoType.getIdentifier();
                    @Nullable TranslatableItem translatableItem = TranslatableItem.by(identifier);
                    return translatableItem == null ?
                            ItemStackBuilder.build(Material.CHEST).itemName("&6"+ammoType.getIdentifier()).lore("&7Missing AmmoType's TranslatableItem").build() :
                            translatableItem.localize(player).getClone();
                },
                null,
                null,
                null);
    }

}
