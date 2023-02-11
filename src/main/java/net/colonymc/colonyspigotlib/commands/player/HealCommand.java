package net.colonymc.colonyspigotlib.commands.player;

import net.colonymc.colonyspigotlib.MainMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class HealCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + label + " [player]");
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("colonyhub.heal")) {
				if(cmd.getName().equalsIgnoreCase("heal")) {
					if(args.length == 0) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have been healed!"));
						p.setHealth(20);
						p.setFoodLevel(20);
						p.setFireTicks(0);
						p.removePotionEffect(PotionEffectType.BLINDNESS);
						p.removePotionEffect(PotionEffectType.CONFUSION);
						p.removePotionEffect(PotionEffectType.HUNGER);
						p.removePotionEffect(PotionEffectType.POISON);
						p.removePotionEffect(PotionEffectType.SLOW);
						p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
						p.removePotionEffect(PotionEffectType.WITHER);
						p.removePotionEffect(PotionEffectType.WEAKNESS);
						p.removePotionEffect(PotionEffectType.HARM);
					}
					else if(args.length == 1) {
						if(Bukkit.getPlayerExact(args[0]) != null) {
							Player pl = Bukkit.getPlayerExact(args[0]);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou healed &d" + pl.getName() + "&f!"));
							pl.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have been healed!"));
							pl.setHealth(20);
							pl.setFoodLevel(20);
							pl.setFireTicks(0);
							pl.removePotionEffect(PotionEffectType.BLINDNESS);
							pl.removePotionEffect(PotionEffectType.CONFUSION);
							pl.removePotionEffect(PotionEffectType.HUNGER);
							pl.removePotionEffect(PotionEffectType.POISON);
							pl.removePotionEffect(PotionEffectType.SLOW);
							pl.removePotionEffect(PotionEffectType.SLOW_DIGGING);
							pl.removePotionEffect(PotionEffectType.WITHER);
							pl.removePotionEffect(PotionEffectType.WEAKNESS);
							pl.removePotionEffect(PotionEffectType.HARM);
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
						}
					}
					else {
						p.sendMessage(usage);
					}
				}
				else if(cmd.getName().equalsIgnoreCase("feed")) {
					if(args.length == 0) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have been fed!"));
						p.setFoodLevel(20);
					}
					else if(args.length == 1) {
						if(Bukkit.getPlayerExact(args[0]) != null) {
							Player pl = Bukkit.getPlayerExact(args[0]);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou fed &d" + pl.getName() + "&f!"));
							pl.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have been fed!"));
							pl.setFoodLevel(20);
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
						}
					}
					else {
						p.sendMessage(usage);
					}
				}
			}
			else {
				p.sendMessage(MainMessages.noPerm);
			}
		}
		else {
			if(cmd.getName().equalsIgnoreCase("heal")) {
				if(args.length == 1) {
					if(Bukkit.getPlayerExact(args[0]) != null) {
						Player pl = Bukkit.getPlayerExact(args[0]);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou healed &d" + pl.getName() + "&f!"));
						pl.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have been healed!"));
						pl.setHealth(20);
						pl.setFoodLevel(20);
						pl.setFireTicks(0);
						pl.removePotionEffect(PotionEffectType.BLINDNESS);
						pl.removePotionEffect(PotionEffectType.CONFUSION);
						pl.removePotionEffect(PotionEffectType.HUNGER);
						pl.removePotionEffect(PotionEffectType.POISON);
						pl.removePotionEffect(PotionEffectType.SLOW);
						pl.removePotionEffect(PotionEffectType.SLOW_DIGGING);
						pl.removePotionEffect(PotionEffectType.WITHER);
						pl.removePotionEffect(PotionEffectType.WEAKNESS);
						pl.removePotionEffect(PotionEffectType.HARM);
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
				}
				else {
					sender.sendMessage(usage);
				}
			}
			else if(cmd.getName().equalsIgnoreCase("feed")) {
				if(args.length == 1) {
					if(Bukkit.getPlayerExact(args[0]) != null) {
						Player pl = Bukkit.getPlayerExact(args[0]);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou fed &d" + pl.getName() + "&f!"));
						pl.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have been fed!"));
						pl.setFoodLevel(20);
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
				}
				else {
					sender.sendMessage(usage);
				}
			}
		}
		return false;
	}

}
