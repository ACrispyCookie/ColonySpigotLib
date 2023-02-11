package net.colonymc.colonyspigotlib.commands.items;

import net.colonymc.colonyspigotlib.MainMessages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("*")) {
				if(p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
					if(args.length > 0) {
						StringBuilder s = new StringBuilder();
						for(String ss : args) {
							s.append(ss).append(" ");
						}
						s = new StringBuilder(s.substring(0, s.length() - 1));
						ItemStack i = p.getItemInHand();
						ItemMeta meta = i.getItemMeta();
						meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', s.toString()));
						i.setItemMeta(meta);
						p.setItemInHand(i);
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease specify the new name of the item!"));
					}
				}
				else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou are not holding any items!"));
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
