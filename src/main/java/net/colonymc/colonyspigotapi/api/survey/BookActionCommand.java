package net.colonymc.colonyspigotapi.api.survey;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BookActionCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 1) {
				if(BookAction.getAction(UUID.fromString(args[0])) != null) {
					BookAction.getAction(UUID.fromString(args[0])).perform(p);
				}
			}
		}
		else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cOnly players can execute this command!"));
		}
		return false;
	}

}
