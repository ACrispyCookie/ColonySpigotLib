package net.colonymc.colonyspigotlib.commands.items;

import net.colonymc.colonyspigotlib.MainMessages;
import net.colonymc.colonyspigotlib.lib.primitive.Numbers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LoreCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("*")) {
				if(p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
					if(args.length > 0) {
						if(args[0].equalsIgnoreCase("set")) {
							if(args.length > 2) {
								if(Numbers.isInt(args[1]) && Integer.parseInt(args[1]) >= 0) {
									int index = Integer.parseInt(args[1]);
									StringBuilder s = new StringBuilder();
									for(int i = 0; i < args.length; i++) {
										if(i > 1) {
											s.append(args[i]).append(" ");
										}
									}
									s = new StringBuilder(s.substring(0, s.length() - 1));
									ItemStack i = p.getItemInHand();
									ItemMeta meta = i.getItemMeta();
									List<String> lore = (meta.getLore() == null ? new ArrayList<>() : meta.getLore());
									if(index > lore.size() - 1) {
										for(int c = lore.size() - 1; c < index; c++) {
											lore.add("");
										}
									}
									lore.set(index, ChatColor.translateAlternateColorCodes('&', s.toString()));
									meta.setLore(lore);
									i.setItemMeta(meta);
									p.setItemInHand(i);
								}
								else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a positive number!"));
								}
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/lore set <index> <text>"));
							}
						}
						else if(args[0].equalsIgnoreCase("add")) {
							if(args.length > 1) {
								if(Numbers.isInt(args[1]) && Integer.parseInt(args[1]) >= 0) {
									int index = Integer.parseInt(args[1]);
									StringBuilder s = new StringBuilder();
									for(int i = 0; i < args.length; i++) {
										if(i > 1) {
											s.append(args[i]).append(" ");
										}
									}
									s = new StringBuilder(s.substring(0, s.length() - 1));
									ItemStack i = p.getItemInHand();
									ItemMeta meta = i.getItemMeta();
									List<String> lore = (meta.getLore() == null ? new ArrayList<>() : meta.getLore());
									if(index > lore.size() - 1) {
										for(int c = lore.size() - 1; c < index - 1; c++) {
											lore.add("");
										}
									}
									lore.add(index, ChatColor.translateAlternateColorCodes('&', s.toString()));
									meta.setLore(lore);
									i.setItemMeta(meta);
									p.setItemInHand(i);
								}
								else {
									StringBuilder s = new StringBuilder();
									for(int i = 0; i < args.length; i++) {
										if(i > 0) {
											s.append(args[i]).append(" ");
										}
									}
									s = new StringBuilder(s.substring(0, s.length() - 1));
									ItemStack i = p.getItemInHand();
									ItemMeta meta = i.getItemMeta();
									List<String> lore = (meta.getLore() == null ? new ArrayList<>() : meta.getLore());
									lore.add(ChatColor.translateAlternateColorCodes('&', s.toString()));
									meta.setLore(lore);
									i.setItemMeta(meta);
									p.setItemInHand(i);
								}
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/lore add [index] <text>"));
							}
						}
						else if(args[0].equalsIgnoreCase("remove")) {
							if(args.length == 2) {
								if(Numbers.isInt(args[1]) && Integer.parseInt(args[1]) >= 0) {
									int index = Integer.parseInt(args[1]);
									ItemStack i = p.getItemInHand();
									ItemMeta meta = i.getItemMeta();
									if(meta.getLore().size() > index) {
										List<String> lore = meta.getLore();
										lore.remove(index);
										meta.setLore(lore);
										i.setItemMeta(meta);
										p.setItemInHand(i);
									}
									else {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis line doesn't exist!"));
									}
								}
								else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a positive number!"));
								}
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/lore remove <index>"));
							}
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/lore set/add/remove [index] <text>"));
						}
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
