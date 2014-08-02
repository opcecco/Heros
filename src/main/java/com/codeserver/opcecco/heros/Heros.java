package com.codeserver.opcecco.heros;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.java.JavaPlugin;

public class Heros extends JavaPlugin implements Listener
{
	public ArrayList<Integer> targetList = new ArrayList<Integer>();
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new VillageClaimListener(this), this);
		getServer().getPluginManager().registerEvents(new ZombieInvasionListener(this), this);
	}
	
	@Override
	public void onDisable()
	{
		HandlerList.unregisterAll();
	}
	
	// Custom format for server broadcast message
	public void serverMessage(ChatColor color, String message)
	{
		getServer().broadcastMessage(color + "[Heros] " + message);
	}
	
	// Custom format for player message
	public void playerMessage(Player player, ChatColor color, String message)
	{
		player.sendMessage(color + "[Heros] " + message);
	}
	
	/*
	 * // Get the Player who owns the specified Villager
	 * public Player ownerOf(Villager villager)
	 * {
	 * List<Player> owners = new ArrayList<Player>();
	 * List<MetadataValue> metadataValues = villager.getMetadata("owner");
	 * 
	 * for (MetadataValue metadataValue : metadataValues)
	 * if (metadataValue.getOwningPlugin() == this) owners.add((Player)
	 * metadataValue.value());
	 * 
	 * if (owners.size() > 1)
	 * getLogger().warning("More than one value for owner of Villager");
	 * 
	 * if (owners.size() < 1) return null;
	 * else return owners.get(0);
	 * }
	 * 
	 * // Get the Player who's rival is the specified Monster
	 * public Player rivalOf(Monster monster)
	 * {
	 * List<Player> rivals = new ArrayList<Player>();
	 * List<MetadataValue> metadataValues = monster.getMetadata("rival");
	 * 
	 * for (MetadataValue metadataValue : metadataValues)
	 * if (metadataValue.getOwningPlugin() == this) rivals.add((Player)
	 * metadataValue.value());
	 * 
	 * if (rivals.size() > 1)
	 * getLogger().warning("More than one value for rival of Monster");
	 * 
	 * if (rivals.size() < 1) return null;
	 * else return rivals.get(0);
	 * }
	 */
	
	// Get Object out of Metadata ... such as owner of Villager or rival of Monster
	public Object getMetadata(Metadatable metadatable, String key)
	{
		List<Object> values = new ArrayList<Object>();
		List<MetadataValue> metadataValues = metadatable.getMetadata(key);
		
		for (MetadataValue metadataValue : metadataValues)
			if (metadataValue.getOwningPlugin() == this) values.add(metadataValue.value());
		
		if (values.size() > 1) getLogger().warning("More than one value in Metadata");
		
		if (values.size() < 1) return null;
		else return values.get(0);
	}
}
