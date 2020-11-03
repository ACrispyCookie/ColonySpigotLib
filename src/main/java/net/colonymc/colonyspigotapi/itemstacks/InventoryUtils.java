package net.colonymc.colonyspigotapi.itemstacks;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
	
	public static void fillInventory(InventoryHolder h, ItemStack item) {
		for(int i = 0; i < h.getInventory().getSize(); i++) {
			h.getInventory().setItem(i, item);
		}
	}

}
