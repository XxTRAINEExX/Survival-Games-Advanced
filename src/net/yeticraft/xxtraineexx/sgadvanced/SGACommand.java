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
	    		if (sender.hasPermission("sga.paltforms")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " PLATFORMS: Interact with platforms.");
	    		if (sender.hasPermission("sga.chests")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CHESTS: Interact with chests.");
	    		if (sender.hasPermission("sga.debug")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " DEBUG: Enables DEBUG mode on the console.");
	    		if (sender.hasPermission("sga.reload")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " RELOAD: Reloads config from disk.");
	    		if (sender.hasPermission("sga.toggle")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " TOGGLE: Enables/Disables the plugin.");
	    		if (sender.hasPermission("sga.setupmode")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " SETUPMODE: Swaps SGA to config mode.");
                if (sender.hasPermission("sga.createworld")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " CREATEWORLD: Creates a new world for SGA.");
                if (sender.hasPermission("sga.worldwarp")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " WORLDWARP: Teleports you to the SGA world.");
                
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
              
              // Check Params. Should be 5 params + 1 for createworld
              if (args.length != 2){
                  sender.sendMessage(ChatColor.AQUA + "Incorrect parameters!");
                  sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " WORLDWARP <worldname>");
                  sender.sendMessage(ChatColor.AQUA + "worldname: Name of your sga world");
                  return true;}
              
              plugin.sgaEvents.worldWarp(sender, args[1]);
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
