package net.colonymc.colonyspigotlib.lib.player;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerInventory {
	
	public static void addItem(ItemStack i, Player p, int amount) {
		if(i.getType() == Material.BOOK_AND_QUILL && !p.hasPermission("*")) {
			i.setType(Material.BOOK);
		}
		int counter = 0;
		boolean hasDropped = false;
		i.setAmount(1);
		while(counter < amount) {
			boolean hasEmpty = false;
			for(ItemStack it : p.getInventory().getContents()) {
				if(it == null) {
					hasEmpty = true;
					break;
				}
				else {
					if(it.getType() == Material.AIR || (it.isSimilar(i) && it.getAmount() < 64)) {
						hasEmpty = true;
						break;
					}
				}
			}
			if(hasEmpty) {
				p.getInventory().addItem(i);
			}
			else {
				hasDropped = true;
				p.getWorld().dropItem(p.getLocation(), i);
			}
			counter++;
		}
		if(hasDropped) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYour inventory was full so some items got dropped!"));
		}
	}
	
	public static void addItems(Collection<? extends ItemStack> items, Player p) {
		boolean hasDropped = false;
		for(ItemStack i : items) {
			if(i.getType() == Material.BOOK_AND_QUILL && !p.hasPermission("*")) {
				i.setType(Material.BOOK);
			}
			int counter = 0;
			int amount = i.getAmount();
			i.setAmount(1);
			while(counter < amount) {
				boolean hasEmpty = false;
				for(ItemStack it : p.getInventory().getContents()) {
					if(it == null) {
						hasEmpty = true;
						break;
					}
					else {
						if(it.getType() == Material.AIR || (it.isSimilar(i) && it.getAmount() < 64)) {
							hasEmpty = true;
							break;
						}
					}
				}
				if(hasEmpty) {
					p.getInventory().addItem(i);
				}
				else {
					hasDropped = true;
					p.getWorld().dropItem(p.getLocation(), i);
				}
				counter++;
			}
		}
		if(hasDropped) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYour inventory was full so some items got dropped!"));
		}
	}

}
