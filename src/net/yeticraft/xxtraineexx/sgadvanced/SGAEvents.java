package net.yeticraft.xxtraineexx.sgadvanced;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class SGAEvents {
	public static SGAdvanced plugin;
	
	
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
	        plugin.alivePlayerList.get(i).teleport(platform.toLocation());
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
	 * This method will toggle someone in the queue.
	 * @return
	 */
	public boolean toggleQueue(){
		
		//TODO: Adjust list of players in queue
		//TODO: Send message to all players waiting to play
		return true;
	}
	
	/**
	 * This method will mark platforms as booby trapped or remove the booby trap. (Instantly killing players 
	 * leaving the platform)
	 * @return
	 */
	public boolean boobyTrapPlatforms(){
		
		//TODO: Toggle platform booby trap 
		return true;
	}
	
	/**
	 * This method will be used to start a match
	 * @return
	 */
	public boolean startMatch(){
		
		//TODO: Store players current locations in home world
		//TODO: Fill chests
		//TODO: boobytrapplatforms
		//TODO: Store player items and strip from player
		//TODO: teleport players
		
		return true;
	}
	
	/**
	 * This method will be used when a match ends. 
	 * @return
	 */
	public boolean endMatch(){
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
	
}
