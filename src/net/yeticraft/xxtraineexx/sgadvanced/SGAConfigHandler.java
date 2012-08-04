package net.yeticraft.xxtraineexx.sgadvanced;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SGAConfigHandler {
	
private static SGAdvanced plugin;
	
/**
 * This is the constructor for the SGAConfigHandler object. We expect a plugin object will be passed to this object on creation
 * @param plugin
 */
public SGAConfigHandler(SGAdvanced plugin) {
        SGAConfigHandler.plugin = plugin;
        }

/**
 * This method will load all items from disk. If nothing is on the disk a default items.yml will be copied from within the JAR.
 * @return
 */
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

	    // Inflating aggregate list
	    // Looping through the foodlist
        for (Integer currentItem: plugin.foodList.keySet()){
            for (int i = 0; i < plugin.foodList.get(currentItem); i++) {
                plugin.itemList.add(currentItem);    
            }
        }
       // Looping through the supplyList
        for (Integer currentItem: plugin.supplyList.keySet()){
            for (int i = 0; i < plugin.supplyList.get(currentItem); i++) {
                plugin.itemList.add(currentItem);    
            }
        }
        // Looping through the weaponList
        for (Integer currentItem: plugin.weaponsList.keySet()){
            for (int i = 0; i < plugin.weaponsList.get(currentItem); i++) {
                plugin.itemList.add(currentItem);    
            }
        }
        // Looping through the armorlist
        for (Integer currentItem: plugin.armorList.keySet()){
            for (int i = 0; i < plugin.armorList.get(currentItem); i++) {
                plugin.itemList.add(currentItem);    
            }
        }
	    
		if (plugin.debug){
			plugin.log.info(plugin.prefix + "Loaded FoodList: " + plugin.foodList.toString());
			plugin.log.info(plugin.prefix + "Loaded SupplyList: " + plugin.supplyList.toString());
			plugin.log.info(plugin.prefix + "Loaded WeaponsList: " + plugin.weaponsList.toString());
			plugin.log.info(plugin.prefix + "Loaded ArmorList: " + plugin.armorList.toString());}
		
		return true;
	}
	
	/**
	 * This method is used to save Blocks to disk. SGA currently uses this method to save both chests and platform locations to disk. (Since they are both blocks)
	 * We pass in the name of the list we want to save (ie. chest) and the actual Hashset we want to save.
	 * @param listType
	 * @param listHash
	 * @return
	 */
	public boolean saveBlocks(String listType, HashSet<SGABlockLoc> listHash){
		
		// Attaching to the file on disk.
		File configFile = new File(plugin.getDataFolder(), listType + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		
		config.set(listType, listHash);
		
		try {
	        config.save(configFile);
	    } catch (IOException ex) {
	        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save block list to " + configFile, ex);
	    }
		
		return true;
	}
	
	/**
	 * This method will save the players current inventory to disk.
	 * @param playerName
	 * @param itemStack
	 * @return
	 */
	public boolean saveInventory (Player player){
	    
		File configFile = new File(plugin.getDataFolder() + "/players", player.getName() + ".yml");
	    FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
	     
        if (configFile.exists()){
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Inventory already exists on disk: " + configFile);
            return false;
        }
        
        ItemStack[] itemStack = player.getInventory().getContents();
        List<ItemStack> stack = Arrays.asList(itemStack);
 	   
	    
	    // writing all inventory to disk
	    config.set("inventory", stack);
	    config.set("helmet", player.getInventory().getHelmet());
	    config.set("boots", player.getInventory().getBoots());
	    config.set("leggings", player.getInventory().getLeggings());
	    config.set("chestplate", player.getInventory().getChestplate());
	    
        
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save player inventory to " + configFile, ex);
            return false;
        }
        
        player.getInventory().clear();
	    player.getInventory().setHelmet(null);
	    player.getInventory().setBoots(null);
	    player.getInventory().setLeggings(null);
	    player.getInventory().setChestplate(null);
	    
	    return true;
	}
	
	/**
	 * This method will load the players current inventory from disk.
	 * @param listType
	 * @return
	 */
	   public boolean loadInventory(Player player){
	        
	        File configFile = new File(plugin.getDataFolder() + "/players", player.getName() + ".yml");
	        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
	        
	        if (!configFile.exists()){
	            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not load player inventory from " + configFile);
	            return false;
	        }
	        
	        player.getInventory().clear();
		    player.getInventory().setHelmet(null);
		    player.getInventory().setBoots(null);
		    player.getInventory().setLeggings(null);
		    player.getInventory().setChestplate(null);
	        
	        @SuppressWarnings("unchecked")
            List<ItemStack> stack = (List<ItemStack>) config.get("inventory");
	        
	        // Iterating through itemstack and assigning to player
	        for (int i=0;i<stack.size();i++) {
	           if (stack.get(i) != null) player.getInventory().setItem(i,stack.get(i));
	        }
	        
	        // bringing back the armor
	        if (config.get("helmet") != null) player.getInventory().setHelmet((ItemStack) config.get("helmet"));
	        if (config.get("boots") != null) player.getInventory().setBoots((ItemStack) config.get("boots"));
	        if (config.get("leggings") != null) player.getInventory().setLeggings((ItemStack) config.get("leggings"));
	        if (config.get("chestplate") != null) player.getInventory().setChestplate((ItemStack) config.get("chestplate"));
	        
	        // Clean up the file on disk
	        plugin.customConfig.deleteInventory(player.getName());
		    return true;
	        
	    }
	   
	   /**
	    * WE use this method to clean up after a player has their items restored.
	    * @param playerName
	    * @return
	    */
	   public boolean deleteInventory(String playerName){
	       boolean success = (new File(plugin.getDataFolder() + "/players", playerName + ".yml")).delete();
	       if (success) {
	           return true;
	       }
	       return false;
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
	
	/**
	 * The following method will copy a file from one place to another.
	 * I use this to copy files out of the JAR and place them on disk
	 * @param in
	 * @param file
	 */
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
