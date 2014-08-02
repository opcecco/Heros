package com.codeserver.opcecco.heros;

import org.bukkit.ChatColor;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class ZombieInvasionListener implements Listener
{
	public Heros plugin;
	
	public ZombieInvasionListener(Heros plugin)
	{
		this.plugin = plugin;
	}
	
	// Mark Monsters who target a Player's Villager with Metadata
	public void markRival(Player player, Monster monster)
	{
		if (plugin.getMetadata(monster, "rival") == null)
		{
			monster.setMetadata("rival", new FixedMetadataValue(plugin, player));
			
			String newName = "Rival: " + player.getDisplayName();
			monster.setCustomName(newName);
			monster.setCustomNameVisible(true);
		}
	}
	
	// Grab Monster when it targets a Villager owned by a Player
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event)
	{
		if (event.getTarget() instanceof Villager && event.getEntity() instanceof Monster)
		{
			Villager villager = (Villager) event.getTarget();
			Monster monster = (Monster) event.getEntity();
			Player owner = (Player) plugin.getMetadata(villager, "owner");
			
			if (owner != null) markRival(owner, monster);
		}
	}
	
	// TODO Give points to player when they protect their Villager
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
	{
		if (event.getEntity() instanceof Monster && event.getEntity().getKiller() instanceof Player)
		{
			Monster monster = (Monster) event.getEntity();
			Player player = (Player) event.getEntity().getKiller();
			Player rival = (Player) plugin.getMetadata(monster, "rival");
			
			if (rival == player) plugin.playerMessage(player, ChatColor.AQUA, "You killed a rival");
			else if (rival != null)
			{
				plugin.playerMessage(player, ChatColor.GOLD, "Assisted " + rival.getDisplayName() + " in killing a rival");
				plugin.playerMessage(rival, ChatColor.GOLD, player.getDisplayName() + " assisted you in killing a rival");
			}
		}
		
		else if (event.getEntity() instanceof Villager)
		{
			Villager villager = (Villager) event.getEntity();
			Player owner = (Player) plugin.getMetadata(villager, "owner");
			
			if (owner != null)
			{
				owner.setMetadata("total_villagers", new FixedMetadataValue(plugin, (Integer) plugin.getMetadata(owner, "total_villagers") - 1));
				plugin.playerMessage(owner, ChatColor.RED, "Your villager was killed: " + (Integer) plugin.getMetadata(owner, "total_villagers"));
			}
		}
	}
}
