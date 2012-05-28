package net.yeticraft.xxtraineexx.sgadvanced;

import java.util.HashSet;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.server.PluginEnableEvent;


public class SGAListener implements Listener{

	public static SGAdvanced plugin;
	public boolean setupPlatforms; 
	public HashSet<SGABlockLoc> chestList;
	
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
		
		// Sample code used for checking out serialization
		SGABlockLoc blockLoc = new SGABlockLoc(e.getBlock().getLocation());
		chestList.add(blockLoc);
		
		int blockType = e.getBlock().getTypeId();
	
		
		if (blockType != 18 || blockType != 39 || blockType != 40){
			e.setCancelled(true);
		}
		
		return;
		
	}
	
	
	
}
