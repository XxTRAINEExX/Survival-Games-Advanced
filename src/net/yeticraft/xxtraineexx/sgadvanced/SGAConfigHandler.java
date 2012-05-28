package net.yeticraft.xxtraineexx.sgadvanced;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SGAConfigHandler {
	

	private static SGAdvanced plugin;
	
	public SGAConfigHandler(SGAdvanced plugin) {
        SGAConfigHandler.plugin = plugin;
    }
	
	
	/*
	public boolean loadItems(){
		
		// Attaching to the file on disk.
		File itemConfigFile = new File(plugin.getDataFolder(), "items.yml");
		FileConfiguration itemConfig = YamlConfiguration.loadConfiguration(itemConfigFile);
		
		
		// Lets see if the item config exists. If not we need to create it.
		if (!itemConfigFile.exists()){

			//TODO: Place code to copy standard yml file from JAR
			return true;
		}
		
		List<Integer> items = itemConfig.getIntegerList("items");
		List<Map<?, ?>> itemsMap = itemConfig.getMapList("items");

		
		//TODO: Place code for finding item count and/or saving these items to a global list
		
		return true;
	}*/
	
	public boolean saveBlocks(String listType, HashSet<SGABlockLoc> listHash){
		
		// Attaching to the file on disk.
		File configFile = new File(plugin.getDataFolder(), listType + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		
		config.set(listType, listHash);
		
		try {
	        config.save(configFile);
	    } catch (IOException ex) {
	        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save playerConfig to " + configFile, ex);
	    }
		
		return true;
	}
	
	/**
	 * This method loads a HashSet from a custom yaml config file. The HashSet should be made of
	 * SGABlockLoc objects. The file should be written with saveBlocks() to maintain the proper 
	 * format. The method accepts a String called listType which will act as the yaml file name 
	 * and the KEY value for the stored HashSet.
	 * 
	 * Example: Passing this method "chests" will load a file called chests.yml and return the hashset
	 * stored in the chests.yml under the section called "chests".
	 *  
	 * @param listType
	 * @return HashSet<SGABlockLoc>
	 * 
	 */
	public HashSet<SGABlockLoc> loadBlocks(String listType){
		
		File configFile = new File(plugin.getDataFolder(), listType + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		
		if (!configFile.exists()){
			HashSet<SGABlockLoc> listHash = new HashSet<SGABlockLoc>();
			return listHash;
		}
		
		@SuppressWarnings("unchecked")
		HashSet<SGABlockLoc> listHash = (HashSet<SGABlockLoc>) config.get(listType);
		return listHash;
		
	}
	
	
}
