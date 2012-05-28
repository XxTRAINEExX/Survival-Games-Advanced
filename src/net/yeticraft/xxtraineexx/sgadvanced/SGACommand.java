package net.yeticraft.xxtraineexx.sgadvanced;



import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * @author XxTRAINEExX
 * This class holds all of the command structure for the plugin. There is also a reporting
 * method that probably doesn't belong here... but I'm tired so it's going here.
 *
 */
public class SGACommand implements CommandExecutor{

	
	private final SGAdvanced plugin;
	ArrayList<Block> topSpawners;
	
	
	
	public SGACommand(SGAdvanced plugin) {
		this.plugin = plugin;
	}

	// Sub commands
	
	enum SubCommand {
		HELP,
		SETPLATFORMS,
		SAVECHESTS,
		SHOWCHESTS,
		DEBUG,
		RELOAD,
		TOGGLE,
		UNKNOWN;
		
		private static SubCommand toSubCommand(String str) {
			try {
				return valueOf(str.toUpperCase());
			} catch (Exception ex) {
				return UNKNOWN;
			}
		}
	}
	

	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	
	if (!sender.hasPermission("sga.command")) {return true;}	
	
	boolean isPlayer = false;
	if (sender instanceof Player) isPlayer = true;
	
	Player player;
	if (isPlayer){
		player = (Player)sender;
		}
	
	
  	if (args.length == 0) {
  		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced");
  		sender.sendMessage(ChatColor.DARK_AQUA + "===============");
  		sender.sendMessage(ChatColor.AQUA + "Try /" + command.getName() + " HELP");
		return true;
    	}
  	if (args.length > 2) {
  		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced");
  		sender.sendMessage(ChatColor.DARK_AQUA + "===============");
  		sender.sendMessage(ChatColor.AQUA + "Looks like you typed too many parameters.");
		return true;
	}
	 	
  	
    	switch (SubCommand.toSubCommand(args[0].toUpperCase())) {
	    	case HELP:
	    		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced Help");
	    		sender.sendMessage(ChatColor.DARK_AQUA + "====================");
	    		if (args.length > 1)
	    		{
	    			sender.sendMessage(ChatColor.AQUA + "Too manyparameters! Try /SGA HELP");
	    			return true;
	    		}

	    		sender.sendMessage(ChatColor.AQUA + " /" + command.getName() + " HELP: Shows this help page");
	    		if (sender.hasPermission("sga.debug")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " DEBUG: Enables DEBUG mode on the console.");
	    		if (sender.hasPermission("sga.reload")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " RELOAD: Reloads config from disk.");
	    		if (sender.hasPermission("sga.toggle")) sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " TOGGLE: Enables/Disables the plugin.");
	    		break;
	    	case SETPLATFORMS:
	    		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced SETPLATFORMS");
	    		sender.sendMessage(ChatColor.DARK_AQUA + "====================");
	    	
	    		// Check permissions for SET command
	      		if (isPlayer){
	    			player = (Player)sender;
	    			if (!player.hasPermission("sga.setplatforms")) {
	      				sender.sendMessage(ChatColor.DARK_AQUA + "Permissions DENIED.");
	      				return true;
	      			}
	      		}
	    		
	    		// Did they type too many parameters?
	    		if (args.length > 2)
	    		{
	    			sender.sendMessage(ChatColor.AQUA +  "Too manyparameters! Try /SGA SET");
	    			return true;
	    		}
	    		
	    		// Did they only type 1 parameter?
	    		if (args.length == 1)
	    		{
	    			sender.sendMessage(ChatColor.AQUA +  " /" + command.getName() + " SET PLATFORMS: Enables/Disables platform setup. Type this once to begin marking platforms. Type it again to stop marking platforms.");
	    			return true;
	    		}

	    		//TODO: Code for SETPLATFORMS GOES HERE
	    		
	    		break;
	    	case SAVECHESTS:
	    		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced SAVECHESTS");
	    		sender.sendMessage(ChatColor.DARK_AQUA + "====================");
	    	
	    		//TODO Check permissions for SAVECHESTS command
	
	    		// Did they type too many parameters?
	    		if (args.length > 1)
	    		{
	    			sender.sendMessage(ChatColor.AQUA +  "Too manyparameters! Try /SGA SAVECHESTS");
	    			return true;
	    		}
	    		
	    		plugin.customConfig.saveBlocks("chests",plugin.sgaListener.chestList);
	    		break;	    		
	    	case SHOWCHESTS:
	    		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced PRINTCHESTS");
	    		sender.sendMessage(ChatColor.DARK_AQUA + "====================");
	    	
	    		//TODO Check permissions for PRINTCHESTS command
	
	    		// Did they type too many parameters?
	    		if (args.length > 1)
	    		{
	    			sender.sendMessage(ChatColor.AQUA +  "Too manyparameters! Try /SGA PRINTCHESTS");
	    			return true;
	    		}
	    		
	    		Iterator<SGABlockLoc> itr = plugin.sgaListener.chestList.iterator();

	    		while (itr.hasNext()){
	    			SGABlockLoc currentChest = itr.next();
	    		    sender.sendMessage(currentChest.toString());
	    		}
	    		
	    		break;
	    	
	    	case DEBUG:
	      		
	      		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced");
	      		sender.sendMessage(ChatColor.DARK_AQUA + "===============");
	    		
	    		// Check permissions for DEBUG command
	      		if (isPlayer){
	    			player = (Player)sender;
	    			if (!player.hasPermission("sga.debug")) {
	      				sender.sendMessage(ChatColor.DARK_AQUA + "Permissions DENIED.");
	      				return true;
	      			}
	      		}
	      		
	    		if (args.length > 1)
	    		{
	    			sender.sendMessage(ChatColor.AQUA + "Too manyparameters! Try /SGA DEBUG");
	    			return true;
	    		}
	    		
	    		if (plugin.debug) {
	    			plugin.debug = false;
	    			sender.sendMessage(ChatColor.AQUA + "Debugging Disabled!");
	    			if (isPlayer){
		    			player = (Player)sender;
		    			plugin.log.info(plugin.prefix + "Debugging disabled by: " + player.getName());
	    			}
	    			else{
	    				plugin.log.info(plugin.prefix + "Debugging disabled by: console");
	    			}
	    		}
	    		
	    		else{
	    			plugin.debug = true;
	    			sender.sendMessage(ChatColor.AQUA + "Debugging Enabled!");
	    			if (isPlayer){
		    			player = (Player)sender;
		    			plugin.log.info(plugin.prefix + "Debugging enabled by: " + player.getName());
	    			}
	    			else{
	    				plugin.log.info(plugin.prefix + "Debugging enabled by: console");
	    			}
	    		}
	    		plugin.saveMainConfig();
				break;
	      	case RELOAD:
	      		
	      		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced");
	      		sender.sendMessage(ChatColor.DARK_AQUA + "===============");
	    		
	    		// Check permissions for RELOAD command
    			if (isPlayer){
	    			player = (Player)sender;
	    			if (!player.hasPermission("sga.reload")) {
	    				sender.sendMessage(ChatColor.DARK_AQUA + "Permissions DENIED.");
	    				return true;
	      			}
    			}	    
    			
	    		if (args.length > 1)
	    		{
	    			sender.sendMessage(ChatColor.AQUA + "Too manyparameters! Try /SGA RELOAD");
	    			return true;
	    		}
	    		
	    		plugin.reloadConfig();
	    		plugin.loadMainConfig();
	    		if (plugin.debug){ plugin.log.info(plugin.prefix + "Config reloaded from disk.");}
	    		sender.sendMessage(ChatColor.AQUA + "Config reloaded from disk.");
	    			
				break;
				
	      	case TOGGLE:
	      		
	      		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced");
	      		sender.sendMessage(ChatColor.DARK_AQUA + "===============");
	    		
	    		// Check permissions for TOGGLE command
    			if (isPlayer){
	    			player = (Player)sender;
	    			if (!player.hasPermission("sga.toggle")) {
	    				sender.sendMessage(ChatColor.DARK_AQUA + "Permissions DENIED.");
	    				return true;
	    			}
    			}
    			
	    		if (args.length > 1)
	    		{
	    			sender.sendMessage(ChatColor.AQUA + "Too manyparameters! Try /SGA TOGGLE");
	    			return true;
	    		}
	    		
	    		if (plugin.pluginEnable) {
	    			plugin.pluginEnable = false;
	    			sender.sendMessage(ChatColor.AQUA + "Plugin Disabled!");
	    			if (isPlayer){
		    			player = (Player)sender;
		    			plugin.log.info(plugin.prefix + "Plugin disabled by: " + player.getName());
	    			}
	    			else{
	    				plugin.log.info(plugin.prefix + "Plugin disabled by: console");
	    			}
	    		}
	    		else{
	    			plugin.pluginEnable = true;
	    			sender.sendMessage(ChatColor.AQUA + "Plugin Enabled!");
	    			if (isPlayer){
		    			player = (Player)sender;
		    			plugin.log.info(plugin.prefix + "Plugin enabled by: " + player.getName());
	    			}
	    			else{
	    				plugin.log.info(plugin.prefix + "Plugin enabled by: console");
	    			}
	    		}
	    		plugin.saveMainConfig();
				break;
	    	case UNKNOWN:
	    		sender.sendMessage(ChatColor.DARK_AQUA + "SGAdvanced");
	    		sender.sendMessage(ChatColor.DARK_AQUA + "===============");
	    		sender.sendMessage(ChatColor.AQUA +  "Unknown command. Use /sga HELP to list available commands.");
    	}
    	
		return true;
	
	
	}
	
	
	
}
