package net.colonymc.api.itemstacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.colonymc.api.utils.UtilFunctions;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class SkullItemBuilder {
	
	Material type = Material.SKULL_ITEM;
	String playerName;
	String url;
	int amount = 1;
	String name;
	List<String> lore = new ArrayList<String>();
	ArrayList<ItemFlag> flags = new ArrayList<ItemFlag>();
	HashMap<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
	HashMap<String, NBTBase> tags = new HashMap<String, NBTBase>();
	
	
	public SkullItemBuilder() {
	}
	
	public SkullItemBuilder name(String s) {
		name = ChatColor.translateAlternateColorCodes('&', s);
		return this;
	}
	
	public SkullItemBuilder amount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public SkullItemBuilder lore(String s) {
		String[] list = s.split("\n");
		for(String ss : list) {
			lore.add(ChatColor.translateAlternateColorCodes('&', ss));
		}
		return this;
	}
	
	public SkullItemBuilder addEnchant(Enchantment ench, int level) {
		enchants.put(ench, level);
		return this;
	}
	
	public SkullItemBuilder addTag(String identifier, NBTBase value) {
		tags.put(identifier, value);
		return this;
	}
	
	public SkullItemBuilder addFlag(ItemFlag flag) {
		flags.add(flag);
		return this;
	}
	
	public SkullItemBuilder url(String url) {
		this.url = url;
		return this;
	}
	
	public SkullItemBuilder playerName(String playerName) {
		this.playerName = playerName;
		return this;
	}

	
	public ItemStack build() {
		ItemStack i;
		if(url != null) {
			i = UtilFunctions.getSkull(url);
		}
		else {
			i = new ItemStack(type, amount, (short) SkullType.PLAYER.ordinal());
		}
		SkullMeta meta = (SkullMeta) i.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		if(playerName != null) {
			meta.setOwner(playerName);
		}
		for(ItemFlag f : flags) {
			meta.addItemFlags(f);
		}
		for(Enchantment ench : enchants.keySet()) {
			meta.addEnchant(ench, enchants.get(ench), true);
		}
		i.setItemMeta(meta);
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(i);
		NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
		for(String identifier : tags.keySet()) {
			tag.set(identifier, tags.get(identifier));
		}
		nmsItem.setTag(tag);
		ItemStack finalItem = CraftItemStack.asBukkitCopy(nmsItem);
		return finalItem;
	}

}
