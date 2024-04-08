package im.dnn.BlockEntities;

import im.dnn.BlockEntities.Commands.BlockCommand;
import im.dnn.BlockEntities.Commands.MainCommand;
import im.dnn.BlockEntities.Commands.MainCompleter;
import im.dnn.BlockEntities.Constants.Commands;
import im.dnn.BlockEntities.Listeners.BlockListener;
import im.dnn.BlockEntities.Managers.BlockManager;
import im.dnn.BlockEntities.Utils.Logger;
import im.dnn.BlockEntities.Utils.Settings;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.api.ResidenceApi;

public class BlockEntities extends JavaPlugin {
    public Settings settings;
    public static Residence resAPI;
    @Override
    public void onEnable() {
        this.settings = new Settings(this);
        Logger.setPlugin(this);
        Logger.info("Enabled plugin PackManager");

        BlockManager blockManager = new BlockManager(this);
        getServer().getPluginManager().registerEvents(new BlockListener(this, blockManager), this);

        BlockCommand blockCommand = new BlockCommand(this);
        getCommand(Commands.BASE).setExecutor(new MainCommand(this, blockCommand));
        getCommand(Commands.BASE).setTabCompleter(new MainCompleter(blockCommand));
    
        Plugin resPlug = getServer().getPluginManager().getPlugin("Residence");
        if (resPlug != null) {
            try {
				 resAPI = (Residence)resPlug;
				 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}
