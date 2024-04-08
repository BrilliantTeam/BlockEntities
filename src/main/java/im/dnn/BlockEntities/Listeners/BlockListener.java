package im.dnn.BlockEntities.Listeners;

import im.dnn.BlockEntities.BlockEntities;
import im.dnn.BlockEntities.Constants.Keys;
import im.dnn.BlockEntities.Constants.Permissions;
import im.dnn.BlockEntities.Managers.BlockManager;
import im.dnn.BlockEntities.Models.BlockItem;
import im.dnn.BlockEntities.Utils.Helpers;
import im.dnn.BlockEntities.Utils.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.inventory.ItemStack;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.api.ResidenceInterface;
import com.bekvon.bukkit.residence.containers.Flags;

public class BlockListener implements Listener {
    private BlockEntities plugin;
    private BlockManager blockManager;
    private ResidenceInterface res = ResidenceApi.getResidenceManager();
    public BlockListener (BlockEntities plugin, BlockManager blockManager) {
        this.plugin = plugin;
        this.blockManager = blockManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlaced (BlockPlaceEvent event) {
    	if(event.isCancelled())
    		return;
        ItemStack item = event.getItemInHand();
        Material material = item.getType();
        if (material.equals(Material.BARRIER) && item.getItemMeta().hasCustomModelData()) {
            Player player = event.getPlayer();

            Location location = event.getBlockPlaced().getLocation();
            BlockItem blockItem = new BlockItem(item);
            float yawRotation = 0;
            switch ((int)Math.round((player.getEyeLocation().getYaw()+180f)/45f)) {
            case 1: case 2:
            	yawRotation = 90;
            	break;
            case 3: case 4:
            	yawRotation = 180;
            	break;
            case 5: case 6:
            	yawRotation = -90;
            	break;
            case 7: case 8: case 0:
            	yawRotation = 0;
            	break;
            default:
            	Logger.importantInfo( "unexception yaw "+ yawRotation);
            }	
            this.blockManager.addBlock(location, blockItem, yawRotation);
        }
    }
    

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWantBlockBroke (PlayerInteractEvent event) {
    	if(event.useItemInHand()==Result.DENY||event.useInteractedBlock()==Result.DENY)
    		return;
    	if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)&&event.getClickedBlock().getType()==Material.BARRIER) {
        	//BlockBreakEvent breakEvent = new BlockBreakEvent(event.getClickedBlock(), event.getPlayer());
    		
    		if(BlockEntities.resAPI.getPermsByLoc(event.getClickedBlock().getLocation()).playerHas(event.getPlayer(),Flags.destroy, true))
    			this.blockManager.breakBlock(event.getClickedBlock().getLocation(), event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWantBlockBreak (BlockBreakEvent event) { 
    	if(event.isCancelled()||event.getBlock().getType()!=Material.BARRIER)
    		return;
    	if(BlockEntities.resAPI.getPermsByLoc(event.getBlock().getLocation()).playerHas(event.getPlayer(),Flags.destroy, true))
    		this.blockManager.breakBlock(event.getBlock().getLocation(), event.getPlayer());
    }
    @EventHandler
    public void onBlockLoad(EntitiesLoadEvent event) {
    	if(event.getChunk()==null)
    		return;
    	for(Entity entity : event.getEntities()) {
    		if(entity instanceof ItemDisplay)
    			blockManager.reload((ItemDisplay)entity);
    	}
    }
}
