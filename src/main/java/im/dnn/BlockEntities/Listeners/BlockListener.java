package im.dnn.BlockEntities.Listeners;

import im.dnn.BlockEntities.BlockEntities;
import im.dnn.BlockEntities.Constants.Keys;
import im.dnn.BlockEntities.Constants.Permissions;
import im.dnn.BlockEntities.Managers.BlockManager;
import im.dnn.BlockEntities.Models.BlockItem;
import im.dnn.BlockEntities.Utils.Helpers;
import im.dnn.BlockEntities.Utils.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {
    private BlockEntities plugin;
    private BlockManager blockManager;

    public BlockListener (BlockEntities plugin, BlockManager blockManager) {
        this.plugin = plugin;
        this.blockManager = blockManager;
    }

    @EventHandler
    public void onBlockPlaced (BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        Material material = item.getData().getItemType();

        if (material.equals(Material.LEGACY_STONE) && item.getItemMeta().hasCustomModelData()) {
            Player player = event.getPlayer();

            if (!Helpers.hasPermission(player, Permissions.PLACE)) {
                Helpers.sendMessage(this.plugin, player, Keys.MESSAGES_CANT_PLACE);
                event.setCancelled(true);
                return;
            } 

            Location location = event.getBlockPlaced().getLocation();
            BlockItem blockItem = new BlockItem(item);
            float yawRotation = 0;
            switch ((int)(player.getEyeLocation().getYaw()+180)/45) {
            case 1: case 2:
            	yawRotation = -90;
            	break;
            case 3: case 4:
            	yawRotation = 0;
            	break;
            case 5: case 6:
            	yawRotation = 90;
            	break;
            case 7: case 8:
            	yawRotation = 180;
            	break;
            default:
            	Logger.importantInfo( "unexception yaw "+ yawRotation);
            }	
            this.blockManager.addBlock(location, blockItem, yawRotation);
        }
    }

    @EventHandler
    public void onWantBlockBroke (PlayerInteractEvent event) {
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Location location = event.getClickedBlock().getLocation();
            Player player = event.getPlayer();

            if (!Helpers.hasPermission(player, Permissions.BREAK)) {
                Helpers.sendMessage(this.plugin, player, Keys.MESSAGES_CANT_BREAK);
                event.setCancelled(true);
                return;
            }

            this.blockManager.breakBlock(location, player);
        }
    }
}
