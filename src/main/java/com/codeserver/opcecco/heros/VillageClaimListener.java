package com.codeserver.opcecco.heros;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class VillageClaimListener implements Listener
{
	public Heros plugin;
	
	public VillageClaimListener(Heros plugin)
	{
		this.plugin = plugin;
	}
	
	// Claim a Villager for the specified Player with Metadata
	public void claimVillager(Player player, Villager villager)
	{
		if (plugin.getMetadata(villager, "owner") == null)
		{
			villager.setMetadata("owner", new FixedMetadataValue(plugin, player));
			
			if (plugin.getMetadata(player, "total_villagers") == null) player.setMetadata("total_villagers", new FixedMetadataValue(plugin, new Integer(0)));
			player.setMetadata("total_villagers", new FixedMetadataValue(plugin, (Integer) plugin.getMetadata(player, "total_villagers") + 1));
			
			String newName = "Owner: " + player.getDisplayName();
			villager.setCustomName(newName);
			villager.setCustomNameVisible(true);
			
			plugin.playerMessage(player, ChatColor.GREEN, "You claimed a villager: " + (Integer) plugin.getMetadata(player, "total_villagers"));
		}
	}
	
	// Grab Villagers when player is nearby
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		
		List<Entity> entities = player.getNearbyEntities(3, 3, 3);
		
		/*
		Chunk currentChunk = player.getWorld().getChunkAt(player.getLocation());
		Entity[] entities = currentChunk.getEntities();
		*/
		
		for (Entity entity : entities)
			if (entity instanceof Villager) claimVillager(player, (Villager) entity);
	}
}
