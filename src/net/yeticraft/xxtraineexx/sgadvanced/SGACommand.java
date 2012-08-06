package net.yeticraft.xxtraineexx.sgadvanced;




import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * @author XxTRAINEExX
 * This class holds all of the command structure for the plugin. 
 *
 */
public class SGACommand implements CommandExecutor{

	
	private final SGAdvanced plugin;
	
		
	public SGACommand(SGAdvanced plugin) {
		this.plugin = plugin;
		
	}

	// Sub commands
	
	enum SubCommand {
		HELP,
		PLATFORMS,
		CHESTS,
		DEBUG,
		RELOAD,
		TOGGLE,
		SETUPMODE,
		CREATEWORLD,
		WORLDWARP,
		ADMIN,
		JOIN,
		LEAVE,
		UNKNOWN;
		
		private static SubCommand toSubCommand(String str) {
			try {
				return valueOf(str.toUpperCase());
			} catch (Exception ex) {
				return UNKNOWN;
			}
		}
	}
	
	// Sub Sub Commands
	enum SubSubCommand {
		SET,
		SHOW,
		SAVE,
		CLEAR,
		UNKNOWN;
		
		private static SubSubCommand toSubCommand(String str) {
			try {
				return valueOf(str.toUpperCase());
			} catch (Exception ex) {
				return UNKNOWN;
			}
		}
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	
	if (!sender.hasPermission("sga.command")) {return true;}	

	// Setting up a player object in case we need get their name
	boolean isPlayer = false;
	if (sender instanceof Player) isPlayer = true;
	Player player;
	
	// Not enough params?
  	if (args.length == 0) {
  		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced");
  		sender.sendMessage(ChatColor.DARK_AQUA + "==========");
  		sender.sendMessage(ChatColor.AQUA + "Try /" + command.getName() + " HELP");
		return true;
    	}
  	
  	// Too many params?
  	if (args.length > 6) {
  		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced");
  		sender.sendMessage(ChatColor.DARK_AQUA + "==========");
  		sender.sendMessage(ChatColor.AQUA + "Looks like you typed too many parameters.");
		return true;
	}
	 	
  	
    	switch (SubCommand.toSubCommand(args[0].toUpperCase())) {
	    
     		// ***************************** HELP COMMAND ****************************	    		
    		case HELP:
	    		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced Help");
	    		sender.sendMessage(ChatColor.DARK_AQUA + "===============");

	    		// Too many params?
	    		if (args.length > 1){
	    			sender.sendMessage(ChatColor.AQUA + "Too manyparameters! Try /SGA HELP");
	    			return true;}
	    		
	    		sender.sendMessage(ChatColor.AQUA + " /" + command.getName() + " HELP: Shows this help page");
	    		if (sender.hasPermission("sga.platforms")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " PLATFORMS: Interact with platforms.");
	    		if (sender.hasPermission("sga.chests")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CHESTS: Interact with chests.");
	    		if (sender.hasPermission("sga.debug")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " DEBUG: Enables DEBUG mode on the console.");
	    		if (sender.hasPermission("sga.reload")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " RELOAD: Reloads config from disk.");
	    		if (sender.hasPermission("sga.toggle")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " TOGGLE: Enables/Disables the plugin.");
	    		if (sender.hasPermission("sga.setupmode")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " SETUPMODE: Swaps SGA to config mode.");
                if (sender.hasPermission("sga.createworld")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CREATEWORLD: Creates a new world for SGA.");
                if (sender.hasPermission("sga.worldwarp")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " WORLDWARP: Teleports you to the SGA world.");
                if (sender.hasPermission("sga.admin")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN: Access to embedded methods for troubleshooting.");
                if (sender.hasPermission("sga.join")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " JOIN: Adds you to the Survival Games Queue.");
                if (sender.hasPermission("sga.leave")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " LEAVE: Removes you from the Survival Game Queue.");
                
                
	    		return true;
	
	    	// ***************************** PALTFORMS COMMAND ****************************	    		
	    	case PLATFORMS:
	    		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced PLATFORMS");
	    		sender.sendMessage(ChatColor.DARK_AQUA + "====================");
	    	
	    		// Check permissions for PLATFORMS command
    			if (!sender.hasPermission("sga.platforms")) {
      				sender.sendMessage(ChatColor.AQUA + "Permissions DENIED.");
      				return true;}
	    		
	    		// Too many params?
	    		if (args.length > 2){
	    			sender.sendMessage(ChatColor.AQUA +  "Too manyparameters! Try /SGA PLATFORMS");
	    			return true;}
	    		
	    		// Only 1 param?
	    		if (args.length == 1){
	    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " PLATFORMS <SET>: Toggles platform setup mode.");
	    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " PLATFORMS <SHOW>: Shows number of platforms in memory.");
	    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " PLATFORMS <SAVE>: Saves current memory resident platforms to disk.");
	    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " PLATFORMS <CLEAR>: Clears current platforms in memory.");
	    			return true;}

	    		// ***************************** PLATFORMS SUB COMMAND ****************************
	    	   	switch (SubSubCommand.toSubCommand(args[1].toUpperCase())) {
	    	   		case SET:
	    	    		if (plugin.setPlatforms) {
	    	    			plugin.setPlatforms = false;
	    	    			sender.sendMessage(ChatColor.AQUA + "Platform setup stopped.");
	    	    			plugin.log.info(plugin.prefix + "Platform setup stopped.");}
	    	    		else{
	    	    			plugin.setPlatforms = true;
	    	    			sender.sendMessage(ChatColor.AQUA + "Platform setup started.");
	    	    			plugin.log.info(plugin.prefix + "Platform setup started.");}
	    	    		return true;
	    	   		case SHOW:
	    	   			sender.sendMessage(ChatColor.AQUA +  "Current platforms in memory: [" + plugin.sgaListener.platformList.size() + "]");
	    	   			return true;
	    	   		case SAVE:
	    	   			plugin.customConfig.saveBlocks("platforms",plugin.sgaListener.platformList);
	    	   			sender.sendMessage(ChatColor.AQUA +  "Platforms saved to disk: [" + plugin.sgaListener.platformList.size() + "]");
	    	   			return true;
	    	   		case CLEAR:
	    	   			sender.sendMessage(ChatColor.AQUA +  "Platforms cleared from memory: [" + plugin.sgaListener.platformList.size() + "]");
	    	   			plugin.sgaListener.platformList.clear();
	    	   			return true;
	    	   		case UNKNOWN:
		    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " PLATFORMS <SET>: Toggles platform setup mode.");
		    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " PLATFORMS <SHOW>: Shows number of platforms in memory.");
		    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " PLATFORMS <SAVE>: Saves current memory resident platforms to disk.");
		    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " PLATFORMS <CLEAR>: Clears current platforms in memory.");
		    			return true;
	    	   	}
	    	   	return true;

	    	// ***************************** CHESTS COMMAND ****************************	    		
	    	case CHESTS:
	    		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced CHESTS");
	    		sender.sendMessage(ChatColor.DARK_AQUA + "=================");
	    	
	    		// Check permissions for CHESTS command
    			if (!sender.hasPermission("sga.chests")) {
      				sender.sendMessage(ChatColor.AQUA + "Permissions DENIED.");
      				return true;}
		    		
	    		// Too many params?
	    		if (args.length > 2){
	    			sender.sendMessage(ChatColor.AQUA +  "Too manyparameters! Try /SGA CHESTS");
	    			return true;}
	    		
	    		// Only 1 param?
	    		if (args.length == 1){
	    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CHESTS <SET>: Toggles chest setup mode.");
	    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CHESTS <SHOW>: Shows number of chests in memory.");
	    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CHESTS <SAVE>: Saves current memory resident platforms to disk.");
	    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CHESTS <CLEAR>: Clears current platforms in memory.");
	    			return true;}
	    		
	    		// ***************************** CHESTS SUB COMMAND ****************************
	    	   	switch (SubSubCommand.toSubCommand(args[1].toUpperCase())) {
	    	   		case SET:
	    	   			if (plugin.setChests) {
	    	    			plugin.setChests = false;
	    	    			sender.sendMessage(ChatColor.AQUA + "Chest setup stopped.");
	    	    			plugin.log.info(plugin.prefix + "Chest Setup stopped.");}
	    	    		else{
	    	    			plugin.setChests = true;
	    	    			sender.sendMessage(ChatColor.AQUA + "Chest setup started.");
	    	    			plugin.log.info(plugin.prefix + "Chest Setup started.");}
	    	   			return true;
	    	   		case SHOW:
	    	   			sender.sendMessage(ChatColor.AQUA +  "Current chests in memory: [" + plugin.sgaListener.chestList.size() + "]");
	    	   			return true;
	    	   		case SAVE:
	    	   			plugin.customConfig.saveBlocks("chests",plugin.sgaListener.chestList);
	    	   			sender.sendMessage(ChatColor.AQUA +  "Chests saved to disk: [" + plugin.sgaListener.chestList.size() + "]");
	    	   			return true;
	    	   		case CLEAR:
	    	   			sender.sendMessage(ChatColor.AQUA +  "Chests cleared from memory: [" + plugin.sgaListener.chestList.size() + "]");
	    	   			plugin.sgaListener.chestList.clear();
	    	   			return true;
	    	   		case UNKNOWN:
		    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CHESTS <SET>: Toggles chest setup mode.");
		    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CHESTS <SHOW>: Shows number of chests in memory.");
		    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CHESTS <SAVE>: Saves current memory resident platforms to disk.");
		    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CHESTS <CLEAR>: Clears current platforms in memory.");
		    			return true;
	    	   	}
	    	   	return true;

	    	// ***************************** DEBUG COMMAND ****************************		    		
	    	case DEBUG:
	      		
	      		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced DEBUG");
	      		sender.sendMessage(ChatColor.DARK_AQUA + "================");
	    		
	    		// Check permissions for DEBUG command
    			if (!sender.hasPermission("sga.debug")) {
      				sender.sendMessage(ChatColor.AQUA + "Permissions DENIED.");
      				return true;}
    			
    			// Too many parameters?
	    		if (args.length > 1){
	    			sender.sendMessage(ChatColor.AQUA + "Too manyparameters! Try /SGA DEBUG");
	    			return true;}
	    		
	    		// Toggle debugging
	    		if (plugin.debug) {
	    			plugin.debug = false;
	    			sender.sendMessage(ChatColor.AQUA + "Debugging Disabled!");
	    			if (isPlayer){
		    			player = (Player)sender;
		    			plugin.log.info(plugin.prefix + "Debugging disabled by: " + player.getName());}
	    			else{
	    				plugin.log.info(plugin.prefix + "Debugging disabled by: console");}}
	    		else{
	    			plugin.debug = true;
	    			sender.sendMessage(ChatColor.AQUA + "Debugging Enabled!");
	    			if (isPlayer){
		    			player = (Player)sender;
		    			plugin.log.info(plugin.prefix + "Debugging enabled by: " + player.getName());}
	    			else{
	    				plugin.log.info(plugin.prefix + "Debugging enabled by: console");}}
	    		
	    		// Save debug setting out to config
	    		plugin.saveMainConfig();
	    		return true;
				
	    	// ***************************** RELOAD COMMAND ****************************					
	      	case RELOAD:
	      		
	      		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced RELOAD");
	      		sender.sendMessage(ChatColor.DARK_AQUA + "=================");
	    		
	    		// Check permissions for RELOAD command
    			if (!sender.hasPermission("sga.reload")) {
    				sender.sendMessage(ChatColor.AQUA + "Permissions DENIED.");
    				return true;}	    
    			
    			// Too many params?
	    		if (args.length > 1){
	    			sender.sendMessage(ChatColor.AQUA + "Too manyparameters! Try /SGA RELOAD");
	    			return true;}

	    		// Reload the config from disk
	    		plugin.reloadConfig();
	    		plugin.loadMainConfig();
	    		plugin.log.info(plugin.prefix + "Config reloaded from disk.");
	    		sender.sendMessage(ChatColor.AQUA + "Config reloaded from disk.");
	    		return true;

			// ***************************** TOGGLE COMMAND ****************************	
	      	case TOGGLE:
	      		
	      		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced TOGGLE");
	      		sender.sendMessage(ChatColor.DARK_AQUA + "=================");
	    		
	    		// Check permissions for TOGGLE command
    			if (!sender.hasPermission("sga.toggle")) {
    				sender.sendMessage(ChatColor.AQUA + "Permissions DENIED.");
    				return true;}
    			
    			// Too many params?
	    		if (args.length > 1){
	    			sender.sendMessage(ChatColor.AQUA + "Too manyparameters! Try /SGA TOGGLE");
	    			return true;}
	    		
	    		// Toggle the plugin
	    		if (plugin.pluginEnable) {
	    			plugin.pluginEnable = false;
	    			sender.sendMessage(ChatColor.AQUA + "Plugin Disabled!");
	    			if (isPlayer){
		    			player = (Player)sender;
		    			plugin.log.info(plugin.prefix + "Plugin disabled by: " + player.getName());}
	    			else{
	    				plugin.log.info(plugin.prefix + "Plugin disabled by: console");}}
	    		else{
	    			plugin.pluginEnable = true;
	    			sender.sendMessage(ChatColor.AQUA + "Plugin Enabled!");
	    			if (isPlayer){
		    			player = (Player)sender;
		    			plugin.log.info(plugin.prefix + "Plugin enabled by: " + player.getName());}
	    			else{
	    				plugin.log.info(plugin.prefix + "Plugin enabled by: console");}}
	    		
	    		// Save the new setting to disk
	    		plugin.saveMainConfig();
	    		return true;
	    	
	    	// ***************************** SETUPMODE  COMMAND ****************************	
	      	case SETUPMODE:
	      		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced SETUPMODE");
	      		sender.sendMessage(ChatColor.DARK_AQUA + "====================");
	    		
	    		// Check permissions for SETUPMODE command
    			if (!sender.hasPermission("sga.setupmode")) {
    				sender.sendMessage(ChatColor.AQUA + "Permissions DENIED.");
    				return true;}
    			// Too many params?
	    		if (args.length > 1){
	    			sender.sendMessage(ChatColor.AQUA + "Too manyparameters! Try /SGA SETUPMODE");
	    			return true;}
	    		
	    		// Enable the plugin for SETUP MODE
	    		if (plugin.setupMode) {
	    			plugin.setupMode = false;
	    			sender.sendMessage(ChatColor.AQUA + "Setup Mode Disabled!");
	    			if (isPlayer){
		    			player = (Player)sender;
		    			plugin.log.info(plugin.prefix + "Setup Mode disabled by: " + player.getName());}
	    			else{
	    				plugin.log.info(plugin.prefix + "Setup Mode disabled by: console");}}
	    		else{
	    			plugin.setupMode = true;
	    			sender.sendMessage(ChatColor.AQUA + "Setup Mode Enabled!");
	    			if (isPlayer){
		    			player = (Player)sender;
		    			plugin.log.info(plugin.prefix + "Setup Mode enabled by: " + player.getName());}
	    			else{
	    				plugin.log.info(plugin.prefix + "Setup Mode enabled by: console");}}
	    		
	    		// Save the new setting to disk
	    		plugin.saveMainConfig();
	    		return true;

			// ***************************** CREATEWORLD COMMAND ****************************					
	      	case CREATEWORLD:
	      	  sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced CREATEWORLD");
              sender.sendMessage(ChatColor.DARK_AQUA + "======================");
              
              // Check permissions for CREATEWORLD command
              if (!sender.hasPermission("sga.createworld")) {
                  sender.sendMessage(ChatColor.AQUA + "Permissions DENIED.");
                  return true;}
              
              // Check Params. Should be 5 params + 1 for createworld
              if (args.length != 6){
                  sender.sendMessage(ChatColor.AQUA + "Parameters are incorrect!");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CREATEWORLD <worldname> <seed> <worldtype> <structures> <environment>");
                  sender.sendMessage(ChatColor.AQUA + "worldname: Name of your new world");
                  sender.sendMessage(ChatColor.AQUA + "seed: Any letters/numbers you want to seed your new world");
                  sender.sendMessage(ChatColor.AQUA + "worldtype: valid options are NORMAL, FLAT, VERSION_1_1");
                  sender.sendMessage(ChatColor.AQUA + "structures: valid options are TRUE, FALSE");
                  sender.sendMessage(ChatColor.AQUA + "environment: valid options are NORMAL, NETHER, THE_END");
                  return true;}
              
              plugin.sgaEvents.createWorld(sender, args[1], args[2], args[3], args[4], args[5]);
	      	  return true;
	      	  
	      	// ***************************** WORLDWARP COMMAND ****************************                   
            case WORLDWARP:
              sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced WORLDWARP");
              sender.sendMessage(ChatColor.DARK_AQUA + "====================");
              
              // Check permissions for WORLDWARP command
              if (!sender.hasPermission("sga.worldwarp")) {
                  sender.sendMessage(ChatColor.AQUA + "Permissions DENIED.");
                  return true;}
              
              // Check Params. Should be 2 params for worldwarp
              if (args.length != 2){
                  sender.sendMessage(ChatColor.AQUA + "Incorrect parameters!");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " WORLDWARP <worldname>");
                  sender.sendMessage(ChatColor.AQUA + "worldname: Name of your sga world");
                  return true;}
              
              plugin.sgaEvents.worldWarp(sender, args[1]);
              return true;  
           // ***************************** ADMIN COMMAND ****************************                   
            case ADMIN:
              sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced ADMIN");
              sender.sendMessage(ChatColor.DARK_AQUA + "================");
              
              // Check permissions for WORLDWARP command
              if (!sender.hasPermission("sga.admin")) {
                  sender.sendMessage(ChatColor.AQUA + "Permissions DENIED.");
                  return true;}
              
              // Check Params. Should be 2 params
              if (args.length != 2){
            	  // These are methods in the Config class
                  sender.sendMessage(ChatColor.AQUA + "Incorrect parameters!");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN CONFIG-LOADITEMS");
                 
                  // These are methods in the Event class
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN TELEPORTPLAYERS");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN PLAYERDEATH");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN PLAYERLEAVE");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN STARTMATCH");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN ENDMATCH");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN DEATHMATCH");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN REGENWORLD");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN SPECTATEMATCH");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN FILLCHESTS");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN UNLOADINVENTORY");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " ADMIN LOADINVENTORY");
                  return true;}
              
              if (args[1].equalsIgnoreCase("CONFIG-LOADITEMS")){
            	  plugin.customConfig.loadItems();
            	  return true;
              }

              if (args[1].equalsIgnoreCase("TELEPORTPLAYERS")){
            	  plugin.sgaEvents.teleportPlayers();
            	  return true;
              }
              if (args[1].equalsIgnoreCase("PLAYERDEATH")){
            	  plugin.sgaEvents.playerDeath((Player) sender);
            	  return true;
              }
              if (args[1].equalsIgnoreCase("PLAYERLEAVE")){
            	  plugin.sgaEvents.playerLeave((Player) sender);
            	  return true;
              }
              if (args[1].equalsIgnoreCase("STARTMATCH")){
            	  plugin.sgaEvents.startMatch();
            	  return true;
              }
              if (args[1].equalsIgnoreCase("ENDMATCH")){
            	  plugin.sgaEvents.endMatch();
            	  return true;
              }
              if (args[1].equalsIgnoreCase("DEATHMATCH")){
            	  plugin.sgaEvents.deathMatch();
            	  return true;
              }
              if (args[1].equalsIgnoreCase("REGENWORLD")){
            	  plugin.sgaEvents.regenWorld();
            	  return true;
              }
              if (args[1].equalsIgnoreCase("SPECTATEMATCH")){
            	  plugin.sgaEvents.spectateMatch((Player) sender);
            	  return true;
              }
              if (args[1].equalsIgnoreCase("FILLCHESTS")){
            	  plugin.sgaEvents.fillChests();
            	  return true;
              }
              if (args[1].equalsIgnoreCase("UNLOADINVENTORY")){
            	  plugin.customConfig.unloadInventory((Player) sender);
            	  return true;
              }
              if (args[1].equalsIgnoreCase("LOADINVENTORY")){
            	  plugin.customConfig.loadInventory((Player) sender);
            	  return true;
              }
              
              sender.sendMessage(ChatColor.AQUA + "Incorrect parameters!");
              return true;
            
           // ***************************** JOIN COMMAND ****************************                   
            case JOIN:
              sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced JOIN");
              sender.sendMessage(ChatColor.DARK_AQUA + "===============");
              
              // Check permissions for WORLDWARP command
              if (!sender.hasPermission("sga.join")) {
                  sender.sendMessage(ChatColor.AQUA + "Permissions DENIED.");
                  return true;}
              
              // Check Params. Should be 1
              if (args.length != 1){
                  sender.sendMessage(ChatColor.AQUA + "Incorrect parameters!");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " JOIN");
                  return true;}

              // Check to verify platforms have been set
              if (plugin.sgaListener.platformList == null || plugin.sgaListener.platformList.size() <= 0){ 
                  sender.sendMessage(plugin.prefix + "Platforms have not been set yet. Cancelling your join.");
                  return true;
              } 
              
              // Adding player to aliveplayer list unless there are no more platforms. If there are
              // no more platforms we will add them to the queue.
              if (plugin.alivePlayerList.size() < plugin.sgaListener.platformList.size()){
                  plugin.alivePlayerList.add((Player) sender);
                  sender.sendMessage(plugin.prefix + "You have been added to the survival games. Please wait for the next match to begin.");
              }
              else{
                  plugin.playerQueue.add((Player) sender);
                  sender.sendMessage(plugin.prefix + "The survival games is currently full but you have been added to the queue.");
                  sender.sendMessage(plugin.prefix + "Current Position: " + (plugin.playerQueue.indexOf((Player) sender) + 1));
              }
              
              return true;
              // ***************************** LEAVE COMMAND ****************************                   
            case LEAVE:
              sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced LEAVE");
              sender.sendMessage(ChatColor.DARK_AQUA + "================");
              
              // Check permissions for WORLDWARP command
              if (!sender.hasPermission("sga.leave")) {
                  sender.sendMessage(ChatColor.AQUA + "Permissions DENIED.");
                  return true;}
              
              // Check Params. Should be 1
              if (args.length != 1){
                  sender.sendMessage(ChatColor.AQUA + "Incorrect parameters!");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " LEAVE");
                  return true;}
              
              player = (Player) sender;
              // Checking to see if the player is currently in a list
              if (!plugin.alivePlayerList.contains(player) && !plugin.deadPlayerList.contains(player) && !plugin.playerQueue.contains(player)){ 
                  sender.sendMessage(plugin.prefix + "It's impossible to leave the game when you aren't participating.");
                  return true;
              } 
             
              
              plugin.sgaEvents.playerLeave((Player) sender);
              return true;  
	      	
	      	// ***************************** UNKNOWN COMMAND ****************************
	      	case UNKNOWN:
	    		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced");
	    		sender.sendMessage(ChatColor.DARK_AQUA + "==========");
	    		sender.sendMessage(ChatColor.AQUA +  "Unknown command. Use /sga HELP to list available commands.");
	    		return true;
    	}
    	
		return true;
	
	
	}
	
	
}
