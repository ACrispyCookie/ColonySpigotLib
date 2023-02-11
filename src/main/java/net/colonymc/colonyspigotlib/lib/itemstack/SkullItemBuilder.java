package net.colonymc.colonyspigotlib.lib.itemstack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.colonymc.colonyspigotlib.lib.utils.UtilFunctions;
import net.colonymc.colonyapi.database.MainDatabase;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class SkullItemBuilder {
	
	Material type = Material.SKULL_ITEM;
	UUID uuid;
	String url;
	int amount = 1;
	String name;
	final List<String> lore = new ArrayList<>();
	final ArrayList<ItemFlag> flags = new ArrayList<>();
	final HashMap<Enchantment, Integer> enchants = new HashMap<>();
	final HashMap<String, NBTBase> tags = new HashMap<>();
	
	
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
	
	public SkullItemBuilder playerUuid(UUID uuid) {
		this.uuid = uuid;
		return this;
	}

	public ItemStack build() {
		ItemStack i;
		SkullMeta meta = null;
		if(url != null) {
			i = UtilFunctions.getSkull(url);
			meta = (SkullMeta) i.getItemMeta();
		}
		else {
			i = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			meta = (SkullMeta) i.getItemMeta();
			if(Bukkit.getPlayer(uuid) != null) {
				meta.setOwner(Bukkit.getPlayer(uuid).getName());
			}
			else {
		        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		        profile.getProperties().put("textures", new Property("textures", new String(getBase64(uuid))));
		        Field profileField = null;
		        try {
		            profileField = meta.getClass().getDeclaredField("profile");
		        } catch (NoSuchFieldException | SecurityException e) {
		            e.printStackTrace();
		        }
		        profileField.setAccessible(true);
		        try {
		            profileField.set(meta, profile);
		        } catch (IllegalArgumentException | IllegalAccessException e) {
		            e.printStackTrace();
		        }
			}
		}
		meta.setDisplayName(name);
		meta.setLore(lore);
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
		finalItem.setAmount(amount);
		return finalItem;
	}
	
	private String getBase64(UUID uuid) {
		ResultSet rs = MainDatabase.getResultSet("SELECT * FROM PlayerInfo WHERE uuid='" + uuid.toString() + "'");
		try {
	        Gson g = new Gson();
			if(rs.next()) {
		        String value = rs.getString("skin");
		        if(value == null) {
			        String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
			        JsonObject obj = g.fromJson(signature, JsonObject.class);
			        value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
		        }
		        return value;
			}
			else {
		        String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
		        JsonObject obj = g.fromJson(signature, JsonObject.class);
				return obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private String getURLContent(String urlStr) {
        URL url;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try{
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8) );
            String str;
            while((str = in.readLine()) != null) {
                sb.append( str );
            }
        } catch (Exception ignored) { }
        finally{
            try{
                if(in!=null) {
                    in.close();
                }
            }catch(IOException ignored) { }
        }
        return sb.toString();
    }

}
