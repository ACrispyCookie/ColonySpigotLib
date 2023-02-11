package net.colonymc.colonyspigotlib.commands.warp;

import net.colonymc.colonyspigotlib.MainMessages;
import net.colonymc.colonyspigotlib.other.Warp;
import net.colonymc.colonyspigotlib.lib.player.visuals.ChatMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpsCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if((p.hasPermission("colonyhub.modifywarps") || p.hasPermission("staff.store") ? Warp.getWarps().size() : Warp.getPublicWarps().size()) > 0) {
				new ChatMessage("&5&lWarp List").addRecipient(p).centered(true).send();
				for(Warp w : (p.hasPermission("colonyhub.modifywarps") || p.hasPermission("staff.store") ? Warp.getWarps() : Warp.getPublicWarps())) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + w.getName() + " &fat the location &d[" + w.getLocation().getBlockX() + ", " + w.getLocation().getBlockY() + ", " + w.getLocation().getBlockZ() + "]" + 
							(w.isPublic() ? " (Public)" : "")));
				}
			}
			else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThere are no available warps at this moment!"));
			}
		}
		else {
			sender.sendMessage(MainMessages.onlyPlayers);
		}
		return false;
	}

}
