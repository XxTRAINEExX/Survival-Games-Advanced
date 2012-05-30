package net.yeticraft.xxtraineexx.sgadvanced;


import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
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
	public SGAConfigHandler customConfig;
	public SGAListener sgaListener;
	public boolean pluginEnable;
	public boolean debug;
	public List<Integer> breakableBlocks;
	public String worldName;
	public boolean setChests;
	public boolean setPlatforms;
	public HashMap<Integer, Integer> foodList = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> supplyList = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> weaponsList = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> armorList = new HashMap<Integer, Integer>();
		
	 static {
	        ConfigurationSerialization.registerClass(SGABlockLoc.class, "blockLocs");
	 }
	
	public void onEnable() {
		
		loadMainConfig();
		customConfig = new SGAConfigHandler(this);
		sgaListener = new SGAListener(this);
		sgaListener.chestList = customConfig.loadBlocks("chests");
		sgaListener.platformList = customConfig.loadBlocks("platforms");
		customConfig.loadItems();
		PluginDescriptionFile pdffile = this.getDescription();
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
        breakableBlocks = config.getIntegerList("breakableBlocks");
        worldName = config.getString("worldName");
        
    	log.info(prefix + "Config loaded.");
    	
    	if (debug) {
    		log.info(prefix + "[pluginEnable: " + String.valueOf(pluginEnable) + "]");
    		log.info(prefix + "[debug: " + String.valueOf(debug) + "]");}
    		log.info(prefix + "[breakableBlocks: " + breakableBlocks.toString() + "]");
    		log.info(prefix + "[worldName: " + worldName + "]");
    	
	}
	
	/**
	 * Config saving method.
	 */
	public void saveMainConfig(){
	
		config.set("pluginEnable", pluginEnable);
		config.set("debug", debug);
		config.set("breakableBlocks", breakableBlocks);
		config.set("worldName", worldName);
		
		saveConfig();
		log.info(prefix + "Config saved.");
		if (debug) {
    		log.info(prefix + "[pluginEnable: " + String.valueOf(pluginEnable) + "]");
    		log.info(prefix + "[debug: " + String.valueOf(debug) + "]");
    		log.info(prefix + "[breakableBlocks: " + breakableBlocks.toString() + "]");
    		log.info(prefix + "[worldName: " + worldName + "]");
		}
		
	}
	

}
