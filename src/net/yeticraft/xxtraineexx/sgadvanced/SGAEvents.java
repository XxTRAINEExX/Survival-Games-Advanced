package net.yeticraft.xxtraineexx.sgadvanced;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class SGAEvents {
	public static SGAdvanced plugin;
	public boolean platformBoobyTrap;
	public boolean deathmatch = false;
	public HashMap<Player, Location> playerPlatform = new HashMap<Player, Location>();
	public HashMap<Player, Location> playerHome = new HashMap<Player, Location>();
	public HashMap<Player, ItemStack[]> playerInventory = new HashMap<Player, ItemStack[]>();
	
	public SGAEvents(SGAdvanced plugin) {
		SGAEvents.plugin = plugin;	
	}
	
	/**
	 * This method will be used to teleport players to their respective
	 * @return
	 */
	public boolean teleportPlayers(){
		
        //Loop through alive players and teleport them all to platforms
	    Iterator<SGABlockLoc> itr = plugin.sgaListener.platformList.iterator();
	    int i=0;
	    while((itr.hasNext()) && (i < plugin.alivePlayerList.size())){
	        SGABlockLoc platform = itr.next();
	        Player currentPlayer = plugin.alivePlayerList.get(i);
	        // Teleport player to platform
	        currentPlayer.teleport(platform.toLocation());
	        // Assign player to platform
	        playerPlatform.put(currentPlayer, platform.toLocation());
	        if (plugin.debug) plugin.log.info("Teleporting [" + plugin.alivePlayerList.get(i) + "] to platform located at [" + platform.toString() + "]");
	        i++;
	    }
		return true;
	}
	
	/**
	 * This method will be used when a player dies.
	 * @return
	 */
	public boolean playerDeath(Player player){
		
	    // Lightning strike
        Location l = new Location(Bukkit.getWorld(plugin.worldName), 0, 200, 0);
        Bukkit.getServer().getWorld(plugin.worldName).strikeLightning(l);

        // Move them to the appropriate list
	    plugin.alivePlayerList.remove(player);
	    plugin.deadPlayerList.add(player);
	    
		// Hide dead person
	    for (Player alivePlayer : plugin.alivePlayerList) {
            if (!alivePlayer.equals(player) && alivePlayer.canSee(player)) {
                alivePlayer.hidePlayer(player);
            }
        }
	    
	    // Allow dead person to fly, give them potion speed
	    player.setAllowFlight(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 0));
        
	    /* Temporarily taking this out to see if lightning creates a canon-like sound. If not I will put this back in.
	    // Start/stop thundering over 3 seconds
	    Bukkit.getServer().getWorld(plugin.worldName).setThundering(true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		    public void run() {
		        Bukkit.getServer().getWorld(plugin.worldName).setThundering(false);
		        }
		 }, 60L);
	    */
	    
        // Teleporting player to middle.
	    player.teleport(l);
	    
	    return true;

	}
	
	
	/**
	 * This method will pull someone from the game lists and shift a queued player in to the appropriate list.
	 * @return
	 */
	public boolean playerLeave(Player leavingPlayer){
		
		
		boolean alive = true;
		if (plugin.alivePlayerList.contains(leavingPlayer)) {
			plugin.alivePlayerList.remove(leavingPlayer);	
		}
		if (plugin.deadPlayerList.contains(leavingPlayer)) {
			plugin.deadPlayerList.remove(leavingPlayer);
			alive=false;
		}
		// Checking the queue to see if someone needs to be shifted in
		if (plugin.playerQueue.size() > 0){
			Player joiningPlayer = plugin.playerQueue.get(0);
			if (alive) {
				plugin.alivePlayerList.add(joiningPlayer);
			}
			else {
				plugin.deadPlayerList.add(joiningPlayer);
			}
			joiningPlayer.sendMessage(plugin.prefix + "You are now entering the game.");
			// Now that they've been moved to the game we will remove them from the queue
			plugin.playerQueue.remove(joiningPlayer);
			
			// Telling all other players they are moving up in the queue
			for (int i = 0; i < plugin.playerQueue.size(); i++){
				Player currentPlayer = plugin.playerQueue.get(i);
				currentPlayer.sendMessage(plugin.prefix + "You have been moved to queue position: " + i+1);
			}
		}
		// Remove special abilities from player
	    leavingPlayer.setAllowFlight(false);
	    leavingPlayer.removePotionEffect(PotionEffectType.SPEED);
        
	    // giving back their items
	    loadPlayerInventory(leavingPlayer);

	    // teleporting them to their home location
		leavingPlayer.teleport(playerHome.get(leavingPlayer));
		playerHome.remove(leavingPlayer);
	    
		return true;
	}
	
	/**
	 * This method will be used to start a match
	 * @return
	 */
	public boolean startMatch(){
		
		// Move everyone from dead player list to alive player list
		for (int i=0; i < plugin.deadPlayerList.size(); i++){
			plugin.alivePlayerList.add(plugin.deadPlayerList.get(i));
		}
		plugin.deadPlayerList.clear();
		
		// store everyones current location if they arent already in the game
		for (int i=0; i < plugin.alivePlayerList.size(); i++){
			
			// Checking to see if they are already in the game
			Player currentPlayer = plugin.alivePlayerList.get(i);
			if (!currentPlayer.getWorld().equals(Bukkit.getWorld(plugin.worldName))){
				unloadPlayerInventory(currentPlayer);
			}
			
			
		}
		fillChests();
		platformBoobyTrap = true;
		teleportPlayers();
		
		return true;
	}
	
	/**
	 * This method will be used when a match ends. 
	 * @return
	 */
	public boolean endMatch(){
		
		// Move everyone from dead player list to alive player list
		for (int i=0; i < plugin.deadPlayerList.size(); i++){
			plugin.alivePlayerList.add(plugin.deadPlayerList.get(i));
		}
	
		// Clearing all variables
		plugin.deadPlayerList.clear();
		deathmatch = false;
		platformBoobyTrap = false;
		playerPlatform.clear();
	
	    for (Player alivePlayer : plugin.alivePlayerList) {
	        
	        // Making sure everyone can see the alivePlayer
	        for (Player otherPlayer : plugin.alivePlayerList) {
	            if (!otherPlayer.equals(alivePlayer) && !otherPlayer.canSee(alivePlayer)) {
	                otherPlayer.showPlayer(alivePlayer);
	            }
	        }
	        
	        // Making sure aliveplayer is not flying
	        alivePlayer.setAllowFlight(false);
	        // Making sure the aliveplayer does not have potion effect
	        alivePlayer.removePotionEffect(PotionEffectType.SPEED);
	        // Returning player items
	        loadPlayerInventory(alivePlayer);
	        // Teleport player to their home location
	        alivePlayer.teleport(playerHome.get(alivePlayer));
	        
        }
		return true;
	}
	
	/**
	 * This method will be used to initiate a deathmatch
	 * @return
	 */
	public boolean deathMatch(){

	    deathmatch = true;
	    teleportPlayers();
	    return true;
	    
	}
	
	/**
	 * This method will be used to handle deathmatch voting by spectators.
	 * @return
	 */
	public boolean deathMatchVote(){
	    
	    //TODO: Initiate deatmatchvote
	    //TODO: TALLY responses
	    //TODO: call deathmatch and reduce time if vote passes
		return true;
	}
	
	/**
	 * This method will be used to toggle someone in/out of spectate mode. Taking them out of spectate will return them to the main world
	 * where they can build/play while waiting for the next SG to begin.
	 * @return
	 */
	public boolean spectateMatch(Player player){
	    
	    // If player is not currently on the dead player list we should return out.
	    if (!plugin.deadPlayerList.contains(player)) return true;
	    
	    if (player.getWorld().equals(Bukkit.getWorld(plugin.worldName))){

	        // Remove special abilities from player
	        player.setAllowFlight(false);
	        player.removePotionEffect(PotionEffectType.SPEED);
	        
	        // clearing their SG inventory
	        player.getInventory().clear();
	        // giving back their real world inventory
            loadPlayerInventory(player);

	        // teleporting them to their home location
	        player.teleport(playerHome.get(player));
	        
	        // Making them visible to everyone
	        for (Player otherPlayer : plugin.alivePlayerList) {
                if (!otherPlayer.equals(player) && !otherPlayer.canSee(player)) {
                    otherPlayer.showPlayer(player);
                }
            }
	    }
	    else{

            // Making them invisible to everyone
            for (Player otherPlayer : plugin.alivePlayerList) {
                if (!otherPlayer.equals(player) && !otherPlayer.canSee(player)) {
                    otherPlayer.showPlayer(player);
                }
            }

	        // add special abilities to player
            player.setAllowFlight(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 0));
            
            // unload player inventory
            unloadPlayerInventory(player);
            // clearing their main world inventory
            player.getInventory().clear();
            
            // teleporting them to the SG world
            player.teleport(plugin.spawnLoc);
            
	    }
	    
		return true;
	}
	
	/**
	 * This method will be used to fill chests in the current world.
	 * @return
	 */
	public boolean fillChests(){
	    
	    //TODO: Cycle through all chests and fill them
	    // TODO: clear all items from the chests
	   
	    // TODO: This needs to be a clone of an object... not a reference
	    LinkedList<Integer> tempList = new LinkedList<Integer>();
	    
	    tempList = plugin.itemList;
	    
	    Random randomGenerator = new Random();
	    	    
	    Iterator<SGABlockLoc> itr = plugin.sgaListener.chestList.iterator();
	    while (itr.hasNext()){
	        Location currentLoc = itr.next().toLocation();
	        Block currentBlock = Bukkit.getWorld(plugin.worldName).getBlockAt(currentLoc);
	        Chest currentChest = (Chest) currentBlock;
	        Inventory currentInventory = currentChest.getBlockInventory();
	        //Clear the inventory of this chest
	        currentInventory.clear();
	        // This random number will represent the quantity of items in the chest (currently 0-5)
	        int itemsInChest = randomGenerator.nextInt(6);
	        // lets loop a set number of times based on the random number we just generated
	        for (int i=0; i < itemsInChest; i++){
	            // We will use the following random number to find an item in the list.
	            int listIndex = randomGenerator.nextInt(tempList.size());
	            // Creating a new itemstack by pulling the item from the list based on the index we just found randomly
	            ItemStack newStack = new ItemStack(tempList.get(listIndex), 1);
	            // Adding this itemstack to the currentInventory
	            currentInventory.addItem(newStack);            
	            // Removing this particular item from the list so we cant add it again.
	            tempList.remove(listIndex);
	        }

	        // Once this loop is complete we will have filled 1 chest with a random number of items. 
	        // Now we need to proceed to the next chest.
	        
	        
	    }
        
        
	    
		return true;
	}
	
	public boolean regenWorld(){
		
	    // Iterate through the hashmap to replace each broken block
	    for (Location currentLoc: plugin.sgaListener.blockLog.keySet()){
	        Block currentBlock = currentLoc.getBlock();
            Block oldBlock = plugin.sgaListener.blockLog.get(currentLoc);
            
            // Reset the currentBlock
            currentBlock.setTypeId(oldBlock.getTypeId());
            currentBlock.setData(oldBlock.getData());
            
            // remove this block from the map
            plugin.sgaListener.blockLog.remove(currentLoc);
	    }
	    
	    // block log should be clear.. but lets do it anyway.
	    plugin.sgaListener.blockLog.clear();
	    
	    // Iterate through entities on the ground and remove them
	    for (Entity e: Bukkit.getWorld(plugin.worldName).getEntities()){
	        if(e.getType().toString().equalsIgnoreCase("DROPPED_ITEM")){
	            e.remove();
	        }
	    }
	    
	    return true;
	}
	
	public boolean unloadPlayerInventory(Player player){
		
	    //TODO: This needs to be a copy of the object... not a reference
		// Putting the players inventory in a hashmap so we can get it later.
		playerInventory.put(player, player.getInventory().getContents());
		player.getInventory().clear();
		//TODO: save these items to disk in case the server crashes
		return true;
	}
	
	/**
	 * This method will clear their current inventory and load the inventory from the hashmap
	 * @param player
	 * @return
	 */
	public boolean loadPlayerInventory(Player player){
	    
	    player.getInventory().clear();
	    player.getInventory().setContents(playerInventory.get(player));
		//TODO: load all items from disk and return to player
		return true;
	}
}
