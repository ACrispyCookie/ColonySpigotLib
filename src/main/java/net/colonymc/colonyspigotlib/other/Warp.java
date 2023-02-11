package net.colonymc.colonyspigotlib.other;

import net.colonymc.colonyspigotlib.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Warp {
	
	final Location loc;
	final String name;
	final boolean isPublic;
	static final ArrayList<Warp> loadedWarps = new ArrayList<>();
	static final ArrayList<Warp> publicWarps = new ArrayList<>();
	
	public Warp(String name, Location loc, boolean isPublic, boolean isNew) {
		this.loc = loc;
		this.name = name;
		this.isPublic = isPublic;
		if(isPublic) {
			publicWarps.add(this);
		}
		if(isNew) {
			Main.getInstance().addWarpToConfig(this);
		}
		loadedWarps.add(this);
	}
	
	public void sendPlayer(Player p) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fTeleporting you to &d" + name + "!"));
		p.teleport(loc.clone());
	}
	
	public void remove() {
		Main.getInstance().removeWarpFromConfig(this);
		loadedWarps.remove(this);
		publicWarps.remove(this);
	}
	
	public String getName() {
		return name;
	}
	
	public Location getLocation() {
		return loc.clone();
	}
	
	public boolean isPublic() {
		return isPublic;
	}
	
	
	public static Warp getWarpByName(String name) {
		for(Warp w : loadedWarps) {
			if(w.name.equalsIgnoreCase(name)) {
				return w;
			}
		}
		return null;
	}
	
	public static ArrayList<Warp> getWarps() {
		return loadedWarps;
	}
	
	public static ArrayList<Warp> getPublicWarps() {
		return publicWarps;
	}
}
