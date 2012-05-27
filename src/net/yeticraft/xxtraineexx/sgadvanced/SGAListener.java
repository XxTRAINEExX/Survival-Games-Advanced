package net.yeticraft.xxtraineexx.sgadvanced;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.server.PluginEnableEvent;


public class SGAListener implements Listener{

	public static SGAdvanced plugin;
	public boolean setupPlatforms; 
	
	public SGAListener(SGAdvanced plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		SGAListener.plugin = plugin;
		setupPlatforms = false;
	}
	
	
	public void onPluginEnable (PluginEnableEvent event) {
		
		plugin.log.info(("Plugin detected: " + event.getPlugin().toString()));
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent e) {	
		
		int blockType = e.getBlock().getTypeId();
	
		
		if (blockType != 18 || blockType != 39 || blockType != 40){
			e.setCancelled(true);
		}
		
		return;
		
	}
	
}
