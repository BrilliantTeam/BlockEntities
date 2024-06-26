package im.dnn.BlockEntities.Managers;

import im.dnn.BlockEntities.BlockEntities;
import im.dnn.BlockEntities.Models.BlockEntity;
import im.dnn.BlockEntities.Models.BlockItem;
import im.dnn.BlockEntities.Utils.Helpers;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class BlockManager {
    private final BlockEntities plugin;
    private HashMap<String, BlockEntity> blockCollection;
    private NamespacedKey key;
    public BlockManager (BlockEntities plugin) {
        blockCollection = new HashMap<>();
        key = new NamespacedKey(plugin, "block_entity");
        this.plugin = plugin;
        this.preload();
    }
    
    public void addBlock (Location location, BlockItem blockItem, float yawRotation) {
        String blockID = Helpers.locationToString(location);
        BlockEntity blockEntity = new BlockEntity(location,key, blockItem, yawRotation);
        
        blockCollection.put(blockID, blockEntity);
    } 

    public void removeBlock (Location location) {
        String blockID = Helpers.locationToString(location);

        if (blockCollection.containsKey(blockID)) {
            BlockEntity blockEntity = blockCollection.get(blockID);
            blockEntity.breakBlock();
            blockCollection.remove(blockID);
        }
    }

    public void breakBlock (Location location, Player player) {
        String blockID = Helpers.locationToString(location);
        if (blockCollection.containsKey(blockID)) {
            BlockEntity blockEntity = blockCollection.get(blockID);
            BlockItem blockItem = blockEntity.getBlockItem();
            this.removeBlock(location);
            player.playSound(location, Sound.BLOCK_STONE_BREAK, 1, 1);
            
            
            
            if (player.getGameMode() == GameMode.SURVIVAL) {
                World world = location.getWorld();
                world.dropItemNaturally(location, blockItem.getItem(1));
            }
        }
    }

    private Location getLocationFromEntity (Location location) {
        Location entityLocation = location.clone();
        entityLocation.setX( location.getX() - 0.5 );
        entityLocation.setY( location.getY() - 0.5 );
        entityLocation.setZ( location.getZ() - 0.5 );

        return entityLocation;
    }

    private void preload () {
        for(World world : this.plugin.getServer().getWorlds()) {
            for (ItemDisplay entity : world.getEntitiesByClass(ItemDisplay.class)) {
            	reload(entity);
            }
        }
    }
    
    public boolean isCustomBlock(Entity entity) {
    	return entity.getPersistentDataContainer().has(key, PersistentDataType.BOOLEAN);
    }
    
    public void reload(ItemDisplay entity) {
    	if(!entity.getPersistentDataContainer().has(key, PersistentDataType.BOOLEAN))
    		return;
        Location location = getLocationFromEntity(entity.getLocation());
        ItemStack itemStack = entity.getItemStack();
        BlockItem blockItem = new BlockItem(itemStack);
        entity.remove();
        if(location.getBlock().getType()==Material.BARRIER)
            this.addBlock(location, blockItem,location.getYaw());
    }
}
