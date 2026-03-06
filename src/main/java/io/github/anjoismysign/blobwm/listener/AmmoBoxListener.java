package io.github.anjoismysign.blobwm.listener;

import io.github.anjoismysign.bloblib.entities.message.BlobSound;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.blobwm.BlobWM;
import io.github.anjoismysign.blobwm.director.manager.WMConfigurationManager;
import io.github.anjoismysign.blobwm.director.manager.WMListenerManager;
import io.github.anjoismysign.blobwm.entity.AmmoBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class AmmoBoxListener extends WMListener {
    private static final BlobWM PLUGIN = BlobWM.getInstance();

    public AmmoBoxListener(WMListenerManager listenerManager) {
        super(listenerManager);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (event.getHand() != EquipmentSlot.HAND){
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR){
            return;
        }
        @Nullable ItemStack itemStack = event.getItem();
        @Nullable TranslatableItem translatableItem = TranslatableItem.byItemStack(itemStack);
        if (translatableItem == null){
            return;
        }
        String identifier = translatableItem.identifier();
        @Nullable AmmoBox ammoBox = WMConfigurationManager.getConfiguration().getAmmoBoxes()
                .stream()
                .filter(box->box.getIdentifier().equals(identifier))
                .findFirst()
                .orElse(null);
        if (ammoBox == null){
            return;
        }
        Player player = event.getPlayer();
        PLUGIN.addAmmo(player, ammoBox);
        itemStack.setAmount(itemStack.getAmount()-1);
        BlobSound.by("BlobWM.AmmoBox-Use").handle(player);
    }

    @Override
    public boolean checkIfShouldRegister() {
        return true;
    }
}
