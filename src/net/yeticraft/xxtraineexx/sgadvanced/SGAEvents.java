package net.yeticraft.xxtraineexx.sgadvanced;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class SGAEvents {
	public static SGAdvanced plugin;
	public boolean platformBoobyTrap;
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
				playerHome.put(currentPlayer, currentPlayer.getLocation());
				PlayerInventory playerInv = currentPlayer.getInventory();
				
				// Iterating through the players inventory so we can store it elsewhere
				HashSet<ItemStack> playerInventory = new HashSet<ItemStack>();
				ListIterator< ItemStack > itr = playerInv.iterator();
				while (itr.hasNext()){
					ItemStack currentItemStack = itr.next();
					playerInventory.add(currentItemStack);
				}
				
				//Their inventory is now in the hashmap so we can clear it.
				currentPlayer.getInventory().clear();
				//TODO: save this inventory to disk in case the server crashes
			}
			
			
		}
		//TODO: Fill chests
		platformBoobyTrap = true;
		
		//TODO: Store player items and strip from player
		
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
		plugin.deadPlayerList.clear();
		
		// Make everyone visible and turn off flymode
	    for (Player alivePlayer : plugin.alivePlayerList) {
            if (!alivePlayer.equals(alivePlayer) && !alivePlayer.canSee(alivePlayer)) {
                alivePlayer.showPlayer(alivePlayer);
            }
        }
	    
		//TODO: Toggle all players visible
		//TODO: Return player items
		//TODO: teleport players to their starting locations in home world
		return true;
	}
	
	/**
	 * This method will be used to initiate a deathmatch
	 * @return
	 */
	public boolean deathMatch(){

	    //TODO: create deathmatch boundary
	    //TODO: Teleport alive players
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
	public boolean spectateMatch(){
	    
	    //TODO: Toggle player between normal world and SG world
	    
		return true;
	}
	
	/**
	 * This method will be used to fill chests in the current world.
	 * @return
	 */
	public boolean fillChests(){
	    
	    //TODO: Cycle through all chests and fill them
		return true;
	}
	
	public boolean regenWorld(){
		
		//TODO: Roll back world
		return true;
	}
	
	public boolean unloadPlayerInventory(Player player){
		
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
