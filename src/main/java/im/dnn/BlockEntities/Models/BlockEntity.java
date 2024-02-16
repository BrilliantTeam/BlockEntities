package im.dnn.BlockEntities.Models;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class BlockEntity {
    private final Material BROKEN_MATERIAL = Material.AIR;
    private final Material HITBOX_MATERIAL = Material.BARRIER;

    private Location location;
    private ItemDisplay entity;
    private BlockItem blockItem;

    public BlockEntity (Location location,NamespacedKey key, BlockItem blockItem) {
        this.placeBlock(location, key ,blockItem, 0);
    }
    public BlockEntity (Location location,NamespacedKey key, BlockItem blockItem, float yawRotation) {
        this.placeBlock(location, key ,blockItem, yawRotation);
    }

    private void placeBlock (Location location,NamespacedKey key, BlockItem blockItem, float yawRotation) {
        World world = location.getWorld();

        world.setBlockData(location, HITBOX_MATERIAL.createBlockData());
        Location entityLocation = getLocationFromBlock(location);
        entityLocation.setYaw(yawRotation);
        world.spawn(entityLocation, ItemDisplay.class, entity -> {
            ItemStack item = blockItem.getItem(1);
            entity.setShadowStrength(0.5f);
            entity.setShadowRadius(0.5f);
            entity.setItemStack(item);
            entity.setPersistent(true);
            entity.setInvulnerable(true);
            entity.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
            this.entity = entity;
            this.location = location;
            this.blockItem = blockItem;
        });
    }

    public void breakBlock () {
        this.entity.remove();
        World world = this.location.getWorld();
        world.setBlockData(this.location, BROKEN_MATERIAL.createBlockData());
    }

    public BlockItem getBlockItem () {
        return  this.blockItem;
    }

    private Location getLocationFromBlock (Location location) {
        Location entityLocation = location.clone();
        entityLocation.setX( location.getX() + 0.5 );
        entityLocation.setY( location.getY() + 0.5 );
        entityLocation.setZ( location.getZ() + 0.5 );

        return entityLocation;
    }
}
