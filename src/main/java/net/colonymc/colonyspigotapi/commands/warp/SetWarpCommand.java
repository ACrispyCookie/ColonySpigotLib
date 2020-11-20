package net.colonymc.colonyspigotapi.commands.warp;

import net.colonymc.colonyspigotapi.MainMessages;
import net.colonymc.colonyspigotapi.other.Warp;
import net.colonymc.colonyspigotapi.api.primitive.Numbers;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/setwarp <name> [true for a public warp]");
			Player p = (Player) sender;
			if(p.hasPermission("colonyhub.modifywarps")) {
				if(args.length == 2) {
					if(Warp.getWarpByName(args[0]) == null) {
						if(Numbers.isBoolean(args[1])) {
							new Warp(args[0], p.getLocation(), Boolean.parseBoolean(args[1]), true);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou created a" + (Boolean.parseBoolean(args[1]) ? " &dpublic &f" : "n ") +"warp named &d" + args[0] + " &fon your location!"));
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter either true or false!"));
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis warp already exists!"));
					}
				}
				else if(args.length == 1) {
					if(Warp.getWarpByName(args[0]) == null) {
						new Warp(args[0], p.getLocation(), false, true);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou created an warp named &d" + args[0] + " &fon your location!"));
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis warp already exists!"));
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
			sender.sendMessage(MainMessages.onlyPlayers);
		}
		return false;
	}
	
	

}
