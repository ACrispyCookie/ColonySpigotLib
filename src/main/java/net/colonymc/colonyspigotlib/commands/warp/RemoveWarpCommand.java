package net.colonymc.colonyspigotlib.commands.warp;

import net.colonymc.colonyspigotlib.MainMessages;
import net.colonymc.colonyspigotlib.other.Warp;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RemoveWarpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + label + " <warp>");
		if(sender.hasPermission("colonyhub.modifywarps")) {
			if(args.length == 1) {
				if(Warp.getWarpByName(args[0]) != null) {
					Warp warp = Warp.getWarpByName(args[0]);
					warp.remove();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou just removed the warp &d" + warp.getName() + "!"));
				}
				else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis warp does not exist!"));
				}
			}
			else {
				sender.sendMessage(usage);
			}
		}
		else {
			sender.sendMessage(MainMessages.noPerm);
		}
		return false;
	}

}
