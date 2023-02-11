package net.colonymc.colonyspigotlib.commands.world;

import net.colonymc.colonyspigotlib.MainMessages;
import net.colonymc.colonyspigotlib.lib.primitive.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + label + " <time in ticks> [world]");
			Player p = (Player) sender;
			if(p.hasPermission("colonyhub.time")) {
				if(args.length == 1) {
					if(Numbers.isInt(args[0])) {
						p.getWorld().setTime(Long.parseLong(args[0]));
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
					}
				}
				else if(args.length == 2) {
					if(Numbers.isInt(args[0])) {
						if(Bukkit.getWorld(args[1]) != null) {
							Bukkit.getWorld(args[1]).setTime(Long.parseLong(args[0]));
						}
						else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid world!"));
						}
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
					}
				}
				else {
					p.sendMessage(usage);
				}
			}
			else {
				sender.sendMessage(MainMessages.noPerm);
			}	
		}
		else {
			String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + label + " <time in ticks> <world>");
			if(args.length == 2) {
				if(Numbers.isInt(args[0])) {
					if(Bukkit.getWorld(args[1]) != null) {
						Bukkit.getWorld(args[1]).setTime(Long.parseLong(args[0]));
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid world!"));
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
