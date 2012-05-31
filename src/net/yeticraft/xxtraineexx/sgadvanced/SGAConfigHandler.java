package net.yeticraft.xxtraineexx.sgadvanced;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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
	
	
	
	public boolean loadItems(){
		
		// Attaching to the file on disk.
		File itemConfigFile = new File(plugin.getDataFolder(), "items.yml");
		FileConfiguration itemConfig;
		
		// Lets see if the item config exists. If not we need to create it.
		if (!itemConfigFile.exists()){
			if (plugin.debug) plugin.log.info(plugin.prefix + "items.yml file not found on disk.  Loading default from JAR");
	    	copy(plugin.getResource("items.yml"), itemConfigFile);}
		else{
			if (plugin.debug) plugin.log.info(plugin.prefix + "items.yml found on disk. Loading...");}
		
		itemConfig = YamlConfiguration.loadConfiguration(itemConfigFile);
		
		// Loading item lists from items.yml
		Set<String> food = itemConfig.getConfigurationSection("foodList").getKeys(true);
		Set<String> supplies = itemConfig.getConfigurationSection("supplyList").getKeys(true);
		Set<String> weapons = itemConfig.getConfigurationSection("weaponsList").getKeys(true);
		Set<String> armor = itemConfig.getConfigurationSection("armorList").getKeys(true);
		
		if (plugin.debug) {
			plugin.log.info(plugin.prefix + "Food list: " + food.toString());
			plugin.log.info(plugin.prefix + "Supply list: " + supplies.toString());
			plugin.log.info(plugin.prefix + "Weapons list: " + weapons.toString());
			plugin.log.info(plugin.prefix + "Armor list: " + armor.toString());
		}
		
		// Inflating foodList
		Iterator<String> itr = food.iterator();
	    while (itr.hasNext()){
	    	int currentFood = Integer.parseInt(itr.next());
	    	plugin.foodList.put(currentFood, itemConfig.getInt("foodList." + String.valueOf(currentFood)));}
	    
	    // Inflating supplyList
	    Iterator<String> its = supplies.iterator();
	    while (its.hasNext()){
	    	int currentSupply = Integer.parseInt(its.next());
	     	plugin.supplyList.put(currentSupply, itemConfig.getInt("supplyList." + String.valueOf(currentSupply)));}

		// Inflating weaponsList
		Iterator<String> itw = weapons.iterator();
	    while (itw.hasNext()){
	    	int currentWeapon = Integer.parseInt(itw.next());
	    	plugin.weaponsList.put(currentWeapon, itemConfig.getInt("weaponsList." + String.valueOf(currentWeapon)));}
	    
	    // Inflating armorList
	    Iterator<String> ita = armor.iterator();
	    while (ita.hasNext()){
	    	int currentArmor = Integer.parseInt(ita.next());
	     	plugin.armorList.put(currentArmor, itemConfig.getInt("armorList." + String.valueOf(currentArmor)));}

		if (plugin.debug){
			plugin.log.info(plugin.prefix + "Loaded FoodList: " + plugin.foodList.toString());
			plugin.log.info(plugin.prefix + "Loaded SupplyList: " + plugin.supplyList.toString());
			plugin.log.info(plugin.prefix + "Loaded WeaponsList: " + plugin.weaponsList.toString());
			plugin.log.info(plugin.prefix + "Loaded ArmorList: " + plugin.armorList.toString());}
		
		return true;
	}
	
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
	
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
}
