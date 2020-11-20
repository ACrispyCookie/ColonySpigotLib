package net.colonymc.colonyspigotapi.commands.player;

import net.colonymc.colonyspigotapi.MainMessages;
import net.colonymc.colonyspigotapi.api.primitive.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/speed <speed> [walk/fly] [player]");
			Player p = (Player) sender;
			if(p.hasPermission("colonyhub.speed")) {
				if(args.length == 1) {
					if(Numbers.isDouble(args[0])) {
						if(p.isFlying()) {
							p.setFlySpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your &dflying &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
						}
						else {
							p.setWalkSpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your &dwalking &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
					}
				}
				else if(args.length == 2) {
					if(Numbers.isDouble(args[0])) {
						if(args[1].equalsIgnoreCase("walk")) {
							p.setWalkSpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your &dwalking &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
						}
						else if(args[1].equalsIgnoreCase("fly")) {
							p.setFlySpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your &dflying &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
						}
						else if(Bukkit.getPlayerExact(args[1]) != null) {
							Player pl = Bukkit.getPlayerExact(args[1]);
							if(pl.isFlying()) {
								pl.setFlySpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + pl.getName() + "'s flying &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
							}
							else {
								pl.setWalkSpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + pl.getName() + "'s walking &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
							}
						}
						else {
							p.sendMessage(usage);
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
					}
				}
				else if(args.length == 3) {
					if(Numbers.isDouble(args[0])) {
						if(args[1].equalsIgnoreCase("walk")) {
							if(Bukkit.getPlayerExact(args[2]) != null) {
								Player pl = Bukkit.getPlayerExact(args[0]);
								pl.setWalkSpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + pl.getName() + "'s walking &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
							}
						}
						else if(args[1].equalsIgnoreCase("fly")) {
							if(Bukkit.getPlayerExact(args[2]) != null) {
								Player pl = Bukkit.getPlayerExact(args[0]);
								pl.setFlySpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + pl.getName() + "'s flying &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
							}
						}
						else {
							p.sendMessage(usage);
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
					}
				}
				else {
					p.sendMessage(usage);
				}
			}
			else {
				p.sendMessage(MainMessages.noPerm);
			}
		}
		else {
			String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/speed <speed> <player> [walk/fly]");
			if(args.length == 2) {
				if(Numbers.isDouble(args[0])) {
					if(Bukkit.getPlayerExact(args[1]) != null) {
						Player pl = Bukkit.getPlayerExact(args[0]);
						if(pl.isFlying()) {
							pl.setFlySpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + pl.getName() + "'s flying &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
						}
						else {
							pl.setWalkSpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + pl.getName() + "'s walking &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
						}
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
				}
				else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
				}
			}
			else if(args.length == 3) {
				if(Numbers.isDouble(args[0])) {
					if(Bukkit.getPlayerExact(args[1]) != null) {
						Player pl = Bukkit.getPlayerExact(args[0]);
						if(args[2].equalsIgnoreCase("fly")) {
							pl.setFlySpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + pl.getName() + "'s flying &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
						}
						else if(args[2].equalsIgnoreCase("walk")){
							pl.setWalkSpeed(Float.parseFloat(args[0]) > 10 ? 1 : Float.parseFloat(args[0])/10);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + pl.getName() + "'s walking &fspeed to &d" + (Float.parseFloat(args[0]) > 10 ? 10 : Float.parseFloat(args[0])) + "!"));
						}
						else {
							sender.sendMessage(usage);
						}
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
				}
				else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
				}
			}
			else {
				sender.sendMessage(usage);
			}
		}
		return false;
	}

}
