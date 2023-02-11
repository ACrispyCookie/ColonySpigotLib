package net.colonymc.colonyspigotlib.commands.items;

import net.colonymc.colonyspigotlib.MainMessages;
import net.colonymc.colonyspigotlib.lib.primitive.Numbers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("*")) {
				if(p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
					ItemStack item = p.getItemInHand();
					if(args.length > 0) {
						if(Enchantment.getByName(args[0]) != null) {
							Enchantment ench = Enchantment.getByName(args[0]);
							if(args.length == 1) {
								if(!item.getEnchantments().containsKey(ench)) {
									try {
										item.addEnchantment(ench, 1);
										p.updateInventory();
									} catch(IllegalArgumentException e) {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis enchantment is unsafe! &7(Add \"unsafe\" at the end of the command to bypass this)"));
									}
								}
								else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis item already has this enchantment!"));
								}
							}
							else if(args.length == 2) {
								if(Numbers.isInt(args[1]) && Integer.parseInt(args[1]) > 0) {
									int level = Integer.parseInt(args[1]);
									if(!item.getEnchantments().containsKey(ench) || item.getEnchantmentLevel(ench) < level) {
										try {
											item.addEnchantment(ench, level);
											p.updateInventory();
										} catch(IllegalArgumentException e) {
											p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis enchantment is unsafe! &7(Add \"unsafe\" at the end of the command to bypass this)"));
										}
									}
									else {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis item already has this enchantment!"));
									}
								}
								else if(args[1].equalsIgnoreCase("unsafe")) {
									if(!item.getEnchantments().containsKey(ench)) {
										item.addUnsafeEnchantment(ench, 1);
										p.updateInventory();
									}
									else {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis item already has this enchantment!"));
									}
								}
								else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a number bigger than 0!"));
								}
							}
							else if(args.length == 3) {
								if(Numbers.isInt(args[1]) && Integer.parseInt(args[1]) > 0) {
									int level = Integer.parseInt(args[1]);
									if(!item.getEnchantments().containsKey(ench) || item.getEnchantmentLevel(ench) < level) {
										item.addUnsafeEnchantment(ench, level);
										p.updateInventory();
									}
									else {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis item already has this enchantment!"));
									}
								}
								else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a number bigger than 0!"));
								}
							}
						}
						else {
							StringBuilder s = new StringBuilder();
							for(int i = 0; i < Enchantment.values().length; i++) {
								Enchantment ench = Enchantment.values()[i];
								if(i + 1 == Enchantment.values().length) {
									s.append(ench.getName());
								}
								else {
									s.append(ench.getName()).append(", ");
								}
							}
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease enter a valid enchant: &d" + s));
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/enchant <enchant> [level] [unsafe]"));
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

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> matches = new ArrayList<>();
		String search = args[0].toLowerCase();
		for (Enchantment e : Enchantment.values()) {
            if(e.getName().toLowerCase().startsWith(search.toLowerCase())) {
        		matches.add(e.getName());
            }
		}
		return matches;
	}

}
