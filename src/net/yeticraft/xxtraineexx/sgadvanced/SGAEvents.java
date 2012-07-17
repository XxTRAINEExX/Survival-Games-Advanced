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
	    
	    // Making a deep copy of the itemList so we can manipulate it while not affecting the source itemList
	    LinkedList<Integer> tempItemList = new LinkedList<Integer>(); // This is the temporary itemList we will be manipulating
	    Iterator<Integer> itemItr = plugin.itemList.iterator();
	    while (itemItr.hasNext()){
	        int tempInt = itemItr.next();
	        tempItemList.add(tempInt);
        }
	    
	    // Moving the chest hashset to a linked list so we can move through it sequentially without affecting the source hashset
	    LinkedList<Chest> tempChestList = new LinkedList<Chest>(); // This is the temporary chestList we will be using (Since our original is a hashset)
	    Iterator<SGABlockLoc> chestItr = plugin.sgaListener.chestList.iterator();
        while (chestItr.hasNext()){
            SGABlockLoc tempChest = new SGABlockLoc(chestItr.next());
            Location currentLoc = tempChest.toLocation(); // Loc of the chest
            Block currentBlock = Bukkit.getWorld(plugin.worldName).getBlockAt(currentLoc); // Block holding the chest
            Chest currentChest = (Chest) currentBlock; // Chest object casted from block
            Inventory currentInventory = currentChest.getInventory(); // Inventory from the given chest
            currentInventory.clear(); // Clearing the current inventory
            tempChestList.add(currentChest);
        }
        
	    // Creating a random number object so we can do some randoms
        Random randomGenerator = new Random();
        // Using the following index to step through the chests sequantially
        int chestIndex = 0;
        // Continuing to loop through the item list until all items have been placed in chests.
	    while (tempItemList.size() > 0){
	        
	        // First checking to make sure the temporary chest index has not exceed the actual number of chests in the linkedList.
	        // If so we will reset the chest index back to 0 so we can start over.
	       if (tempChestList.size() > chestIndex){
	            
	           boolean populateChest = randomGenerator.nextBoolean(); // Do we fill the chest or not?
	           if (populateChest){
	                Chest currentChest = tempChestList.get(chestIndex); // Chest object from list
	                Inventory currentInventory = currentChest.getInventory(); // Inventory from the given chest
	                // We will use the following random number to find an item in the list.
	                int listIndex = randomGenerator.nextInt(tempItemList.size());
	                // Creating a new itemstack by pulling the item from the list based on the index we just found randomly
	                ItemStack newStack = new ItemStack(tempItemList.get(listIndex), 1);
	                // Adding this itemstack to the currentInventory
	                currentInventory.addItem(newStack);            
	                // Removing this particular item from the list so we cant add it again.
	                tempItemList.remove(listIndex);
	            }
	            chestIndex++;

	       }
	        else{
	            chestIndex=0;
	        }
	        
	    }
        
	    return true;
	}
	
	public boolean regenWorld(){
		
	    // Iterate through the hashmap to replace each broken block
	    Iterator<SGABlockLoc> it = plugin.sgaListener.blockLog.iterator();
	    while (it.hasNext()){
	        SGABlockLoc oldBlock = it.next();
	        Block currentBlock = Bukkit.getWorld(plugin.worldName).getBlockAt(oldBlock.toLocation());
	        currentBlock.setTypeId(oldBlock.getTypeId());
	        currentBlock.setData(oldBlock.getData());
            plugin.sgaListener.blockLog.remove(oldBlock); // remove this block from the map
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
		
	    // Storing the players inventory to disk
	    plugin.customConfig.saveInventory(player.getName(), player.getInventory().getContents());
	    player.getInventory().clear();
		return true;
	}
	
	/**
	 * This method will clear their current inventory and load the inventory from the hashmap
	 * @param player
	 * @return
	 */
	public boolean loadPlayerInventory(Player player){
	    player.getInventory().clear();
	    plugin.customConfig.loadInventory(player.getName());
	    plugin.customConfig.deleteInventory(player.getName());
	    return true;
	}
}
