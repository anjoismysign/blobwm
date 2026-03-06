package io.github.anjoismysign.blobwm;

import io.github.anjoismysign.blobwm.entity.AmmoBox;
import io.github.anjoismysign.blobwm.entity.AmmoPouch;
import io.github.anjoismysign.blobwm.entity.AmmoType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface BlobWMAPI {
    /**
     * @param player The player to get the ammo pouch
     * @param ammoType The ammo type
     * @return The AmmoPouch, or null if player is not cached.
     */
    @Nullable AmmoPouch getAmmoPouch(Player player, AmmoType ammoType);

    /**
     * @param player The player to add the ammo box
     * @param ammoBox The ammo box
     * @return True if successful, false otherwise
     */
    boolean addAmmo(Player player, AmmoBox ammoBox);

    /**
     * @param weaponTitle The weaponTitle to get the ammo type
     * @return The ammo type this weaponTitle belongs to, null if no ammo type contains this weaponTitle
     */
    @Nullable AmmoType getAmmoType(String weaponTitle);
}
