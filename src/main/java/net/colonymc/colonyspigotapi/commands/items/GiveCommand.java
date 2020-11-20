package net.colonymc.colonyspigotapi.commands.items;

import net.colonymc.colonyspigotapi.MainMessages;
import net.colonymc.colonyspigotapi.api.itemstack.ItemStackBuilder;
import net.colonymc.colonyspigotapi.api.player.PlayerInventory;
import net.colonymc.colonyspigotapi.api.primitive.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + label + " [player] <item id/item name> [amount] [data]");
			Player p = (Player) sender;
			if(p.hasPermission("colonyhub.give")) {
				if(args.length == 1) {
					String materialArg = args[0];
					Material mat = searchMat(materialArg);
					if(mat != null) {
						PlayerInventory.addItem(new ItemStack(mat), p, 1);
					}
					else if(Bukkit.getPlayerExact(materialArg) != null) {
						p.sendMessage(usage);
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid item ID or name!"));
					}
				}
				else if(args.length == 2) {
					if(Bukkit.getPlayerExact(args[0]) != null) {
						String materialArg = args[1];
						Material mat = searchMat(materialArg);
						if(mat != null) {
							PlayerInventory.addItem(new ItemStack(mat), Bukkit.getPlayerExact(args[0]), 1);
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid item ID or name!"));
						}
					}
					else if(Bukkit.getPlayerExact(args[0]) == null && searchMat(args[0]) == null) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
					else if(Numbers.isInt(args[1])) {
						String materialArg = args[0];
						Material mat = searchMat(materialArg);
						if(mat != null) {
							PlayerInventory.addItem(new ItemStack(mat), p, Integer.parseInt(args[1]));
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid item ID or name!"));
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
					}
				}
				else if(args.length == 3) {
					if(Bukkit.getPlayerExact(args[0]) != null) {
						if(Numbers.isInt(args[2])) {
							String materialArg = args[1];
							Material mat = searchMat(materialArg);
							if(mat != null) {
								PlayerInventory.addItem(new ItemStack(mat), Bukkit.getPlayerExact(args[0]), Integer.parseInt(args[2]));
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid item ID or name!"));
							}
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
						}
					}
					else if(Bukkit.getPlayerExact(args[0]) == null && searchMat(args[0]) == null) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
					else if(Numbers.isInt(args[1])) {
						if(Numbers.isInt(args[2])) {
							String materialArg = args[0];
							Material mat = searchMat(materialArg);
							if(mat != null) {
								PlayerInventory.addItem(new ItemStackBuilder(mat).durability(Short.parseShort(args[2])).build(), p, Integer.parseInt(args[1]));
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid item ID or name!"));
							}
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
					}
				}
				else if(args.length == 4) {
					if(Bukkit.getPlayerExact(args[0]) != null) {
						if(Numbers.isInt(args[2]) && Numbers.isInt(args[3])) {
							String materialArg = args[1];
							Material mat = searchMat(materialArg);
							if(mat != null) {
								PlayerInventory.addItem(new ItemStackBuilder(mat).durability(Short.parseShort(args[3])).build(), Bukkit.getPlayerExact(args[0]), Integer.parseInt(args[2]));
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid item ID or name!"));
							}
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
						}
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
				p.sendMessage(MainMessages.noPerm);
			}
		}
		else {
			String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + label + " <player> <item id/item name> [amount] [data]");
			if(args.length == 2) {
				if(Bukkit.getPlayerExact(args[0]) != null) {
					String materialArg = args[1];
					Material mat = searchMat(materialArg);
					if(mat != null) {
						PlayerInventory.addItem(new ItemStack(mat), Bukkit.getPlayerExact(args[0]), 1);
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid item ID or name!"));
					}
				}
				else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
				}
			}
			else if(args.length == 3) {
				if(Bukkit.getPlayerExact(args[0]) != null) {
					if(Numbers.isInt(args[2])) {
						String materialArg = args[1];
						Material mat = searchMat(materialArg);
						if(mat != null) {
							PlayerInventory.addItem(new ItemStack(mat), Bukkit.getPlayerExact(args[0]), Integer.parseInt(args[2]));
						}
						else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid item ID or name!"));
						}
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
					}
				}
				else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
				}
			}
			else if(args.length == 4) {
				if(Bukkit.getPlayerExact(args[0]) != null) {
					if(Numbers.isInt(args[2]) && Numbers.isInt(args[3])) {
						String materialArg = args[1];
						Material mat = searchMat(materialArg);
						if(mat != null) {
							PlayerInventory.addItem(new ItemStackBuilder(mat).durability(Short.parseShort(args[3])).build(), Bukkit.getPlayerExact(args[0]), Integer.parseInt(args[2]));
						}
						else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid item ID or name!"));
						}
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
					}
				}
				else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
				}
			}
			else {
				sender.sendMessage(usage);
			}
		}
		return false;
	}
	
	public Material searchMat(String name) {
		if(Material.getMaterial(name.toUpperCase()) != null) {
			return Material.getMaterial(name.toUpperCase());
		}
		else if(Numbers.isInt(name) && Material.getMaterial(Integer.parseInt(name)) != null) {
			return Material.getMaterial(Integer.parseInt(name));
		}
		return null;
	}

}
