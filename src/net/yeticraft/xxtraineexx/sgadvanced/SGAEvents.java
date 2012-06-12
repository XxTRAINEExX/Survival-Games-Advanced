package net.yeticraft.xxtraineexx.sgadvanced;

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
		
		//TODO: Loop through alive players and teleport them all to platforms
		return true;
	}
	
	/**
	 * This method will be used when a player dies.
	 * @return
	 */
	public boolean playerDeath(){
		
		//TODO: Mark as dead
		//TODO: squat
		//TODO: create thunder/lightning
		
		return true;

	}
	
	
	/**
	 * This method will be used to queue a player for survival Games (or remove them from the queue)
	 * @return
	 */
	public boolean queuePlayer(){
		
		//TODO: Add player to queue
		return true;
	}
	
	
	/**
	 * This method will adjust the queue based on someone leaving the server or choosing not to play.
	 * @return
	 */
	public boolean unqueuePlayer(){
		
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
		return true;
	}
	
	/**
	 * This method will be used to handle deathmatch voting by spectators.
	 * @return
	 */
	public boolean deathMatchVote(){
		return true;
	}
	
	/**
	 * This method will be used to toggle someone in/out of spectate mode. Taking them out of spectate will return them to the main world
	 * where they can build/play while waiting for the next SG to begin.
	 * @return
	 */
	public boolean spectateMatch(){
		return true;
	}
	
	/**
	 * This method will be used to fill chests in the current world.
	 * @return
	 */
	public boolean fillChests(){
		return true;
	}
	
}
