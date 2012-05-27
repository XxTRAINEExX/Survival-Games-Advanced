package net.yeticraft.xxtraineexx.sgadvanced;

import org.bukkit.event.Listener;

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
	
	
}
