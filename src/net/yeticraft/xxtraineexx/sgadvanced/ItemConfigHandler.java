package net.yeticraft.xxtraineexx.sgadvanced;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemConfigHandler {
	

	private static SGAdvanced plugin;
	
	public ItemConfigHandler(SGAdvanced plugin) {
        ItemConfigHandler.plugin = plugin;
    }
	
	
	
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
	}
	
	
}
