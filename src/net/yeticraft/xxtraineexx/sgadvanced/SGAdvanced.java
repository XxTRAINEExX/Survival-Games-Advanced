package net.yeticraft.xxtraineexx.sgadvanced;

import java.util.logging.Logger;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author XxTRAINEExX
 * This is the main class for the plugin. We initialize the listener, load the config, 
 * and set a few global variables.
 *
 */

public class SGAdvanced extends JavaPlugin{

	public final Logger log = Logger.getLogger("Minecraft");
	public String prefix = "[SGAdvanced] ";
	public FileConfiguration config;
	public SGAMatch sgaMatch;
	public SGAListener sgaListener;
	public boolean pluginEnable;
	public boolean debug;
	

	
	public void onEnable() {
		
		sgaMatch = new SGAMatch(this);
		sgaListener = new SGAListener(this);
		PluginDescriptionFile pdffile = this.getDescription();
		loadMainConfig();
		CommandExecutor MSCCommandExecutor = new SGACommand(this);
		getCommand("sgadvanced").setExecutor(MSCCommandExecutor);
    	getCommand("sga").setExecutor(MSCCommandExecutor);  	
    	log.info(prefix + " " + pdffile.getVersion() + " Enabled"); 	
    	
	}
	
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription(); 
		log.info(prefix + " " + pdffile.getVersion() + " Disabled"); 
	}
	
	/**
	 * Config loading method.
	 */
	public void loadMainConfig(){

		// Read the config file
		config = getConfig();
		config.options().copyDefaults(true);
        saveConfig();
		
		
    	// Assign all the local variables
       	pluginEnable = config.getBoolean("pluginEnable");
        debug = config.getBoolean("debug");
        
    	log.info(prefix + "Config loaded.");
    	if (debug) {
    		log.info(prefix + "[pluginEnable: " + String.valueOf(pluginEnable) + "]");
    		log.info(prefix + "[debug: " + String.valueOf(debug) + "]");
    	}
    	
	}
	
	/**
	 * Config saving method.
	 */
	public void saveMainConfig(){
	
		config.set("pluginEnable", pluginEnable);
		config.set("debug", debug);
		
		saveConfig();
		log.info(prefix + "Config saved.");
		if (debug) {
    		log.info(prefix + "[pluginEnable: " + String.valueOf(pluginEnable) + "]");
    		log.info(prefix + "[debug: " + String.valueOf(debug) + "]");
   	}
		
	}
	

}
