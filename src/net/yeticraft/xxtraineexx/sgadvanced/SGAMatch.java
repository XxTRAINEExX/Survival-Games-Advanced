package net.yeticraft.xxtraineexx.sgadvanced;

import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


public class SGAMatch{

	public static SGAdvanced plugin;
	public HashSet<Block> platforms = new HashSet<Block>(); //Platforms of starting area
	public HashMap<Player, Boolean> playerAlive = new HashMap<Player, Boolean>(); // True if Alive - False if dead
	
	public SGAMatch(SGAdvanced plugin) {
		SGAMatch.plugin = plugin;
	}
	
	
}
