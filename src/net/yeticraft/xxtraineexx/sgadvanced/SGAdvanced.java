package net.yeticraft.xxtraineexx.sgadvanced;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
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
	public SGAEvents sgaEvents;
	public boolean pluginEnable;
	public boolean debug;
	public List<Integer> breakableBlocks;
	public String worldName;
	public boolean setupMode;
	public int deathmatchBoundary;
	public Location spawnLoc;
	public boolean setChests;
	public boolean setPlatforms;
	public int maxServerPlayers;
	LinkedList<Player> playerQueue = new LinkedList<Player>();
	LinkedList<Player> alivePlayerList = new LinkedList<Player>();
	LinkedList<Player> deadPlayerList = new LinkedList<Player>();
	public HashMap<Integer, Integer> foodList = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> supplyList = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> weaponsList = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> armorList = new HashMap<Integer, Integer>();
	public LinkedList<Integer> itemList = new LinkedList<Integer>();
		
	 static {
	        ConfigurationSerialization.registerClass(SGABlockLoc.class, "blockLocs");
	 }
	
	public void onEnable() {
		
		loadMainConfig();
		customConfig = new SGAConfigHandler(this);
		sgaListener = new SGAListener(this);
		sgaEvents = new SGAEvents(this);
		sgaListener.chestList = customConfig.loadBlocks("chests");
		sgaListener.platformList = customConfig.loadBlocks("platforms");
		customConfig.loadItems();
		PluginDescriptionFile pdffile = this.getDescription();
		CommandExecutor SGACommandExecutor = new SGACommand(this);
		getCommand("sgadvanced").setExecutor(SGACommandExecutor);
    	getCommand("sga").setExecutor(SGACommandExecutor);  
    	maxServerPlayers = this.getServer().getMaxPlayers();
    	Bukkit.getServer().getWorld(worldName).setThunderDuration(40);
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
        setupMode = config.getBoolean("setupMode");
        deathmatchBoundary = config.getInt("deathmatchBoundary");
        List<Integer> spawnLocValues = config.getIntegerList("spawnLoc");
        Location spawnLoc = new Location(Bukkit.getWorld(worldName), spawnLocValues.get(0).doubleValue(), spawnLocValues.get(1).doubleValue(), spawnLocValues.get(2).doubleValue());;
                
    	log.info(prefix + "Config loaded.");
    	
    	if (debug) {
    		log.info(prefix + "[pluginEnable: " + String.valueOf(pluginEnable) + "]");
    		log.info(prefix + "[debug: " + String.valueOf(debug) + "]");}
    		log.info(prefix + "[breakableBlocks: " + breakableBlocks.toString() + "]");
    		log.info(prefix + "[worldName: " + worldName + "]");
    		log.info(prefix + "[setupMode: " + setupMode + "]");
    		log.info(prefix + "[deathmatchBoundary: " + deathmatchBoundary + "]");
    		log.info(prefix + "[spawnLoc: " + spawnLoc.toString() + "]");
    		
	}
	
	/**
	 * Config saving method.
	 */
	public void saveMainConfig(){
	
		config.set("pluginEnable", pluginEnable);
		config.set("debug", debug);
		config.set("breakableBlocks", breakableBlocks);
		config.set("worldName", worldName);
		config.set("setupMode", setupMode);
		config.set("deathmatchBoundary", deathmatchBoundary);
		config.set("spawnLoc", spawnLoc);
		
		saveConfig();
		log.info(prefix + "Config saved.");
		if (debug) {
    		log.info(prefix + "[pluginEnable: " + String.valueOf(pluginEnable) + "]");
    		log.info(prefix + "[debug: " + String.valueOf(debug) + "]");
    		log.info(prefix + "[breakableBlocks: " + breakableBlocks.toString() + "]");
    		log.info(prefix + "[worldName: " + worldName + "]");
    		log.info(prefix + "[setupMode: " + setupMode + "]");
    		log.info(prefix + "[deathmatchBoundary: " + deathmatchBoundary + "]");
    		log.info(prefix + "[spawnLoc: " + spawnLoc.toString() + "]");
		}
		
	}
	

}
