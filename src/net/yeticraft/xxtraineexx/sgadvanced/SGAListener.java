package net.yeticraft.xxtraineexx.sgadvanced;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.PluginEnableEvent;


public class SGAListener implements Listener{

	public static SGAdvanced plugin;
	public boolean setupPlatforms; 
	public HashSet<SGABlockLoc> chestList;
	public HashSet<SGABlockLoc> platformList;
	
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
		
		// If plugin is disabled, exit.
		if (!plugin.pluginEnable) return;
		
		// Player is setting chests, has permissions, and the block is a chest
		if (plugin.setChests && e.getPlayer().hasPermission("sga.chests") && e.getBlock().getTypeId() == 54){
			SGABlockLoc blockLoc = new SGABlockLoc(e.getBlock().getLocation());
			if (chestList.contains(blockLoc)) {
				chestList.remove(blockLoc);
				e.getPlayer().sendMessage(ChatColor.AQUA + "Chest removed from memory: " + e.getBlock().getLocation().toString());
				e.getPlayer().sendMessage(ChatColor.AQUA + "When you are finished remove chests, run the SET command again and SAVE");}
			return;}
		 
		// Checking block to see if its on the break list. If so, allow break and return.
		int blockType = e.getBlock().getTypeId();
		if (plugin.breakableBlocks.contains(blockType)) return;
		
		// Block is not on the list, cancel the break and return.
		e.setCancelled(true);
		return;}
	
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent e) {
	
    	// If plugin is disabled, exit.
    	if (!plugin.pluginEnable) return;
    	
    	// If player is setting chests, has permissions, and the block is a chest.
    	if (plugin.setChests && e.getPlayer().hasPermission("sga.chests")  && e.getBlock().getTypeId() == 54) {
    		SGABlockLoc blockLoc = new SGABlockLoc(e.getBlock().getLocation());
			chestList.add(blockLoc);
			e.getPlayer().sendMessage(ChatColor.AQUA + "Chest added to memory: " + e.getBlock().getLocation().toString());
			e.getPlayer().sendMessage(ChatColor.AQUA + "When you are finished adding chests, run the SET command again and SAVE");
			return;}
    	
    	// Cancel the place and return
    	e.setCancelled(true);
    	return;}
    	
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDamage(BlockDamageEvent e) {
    	
    	// If plugin is disabled, exit.
    	if (!plugin.pluginEnable) return;
    	
		// Player is setting up platforms, and has permissions.
		if (plugin.setPlatforms && e.getPlayer().hasPermission("sga.platforms")){
			SGABlockLoc blockLoc = new SGABlockLoc(e.getBlock().getLocation());
			platformList.add(blockLoc);
			e.getPlayer().sendMessage(ChatColor.AQUA + "Platform added to memory: " + e.getBlock().getLocation().toString());
			e.getPlayer().sendMessage(ChatColor.AQUA + "When you are finished adding platforms, run the SET command again and SAVE");
			e.setCancelled(true);
			return;}
    	
		// Return
		return;
		
    }
    
     
    
}
