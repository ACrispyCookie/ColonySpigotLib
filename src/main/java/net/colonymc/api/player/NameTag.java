package net.colonymc.api.player;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class NameTag {
	
	final Player p;
	String prefix;
	String suffix;
	String skinUrl;
	
	public NameTag(Player p) {
		this.p = p;
	}
	
	public NameTag prefix(String s) {
		String prefix = s;
		if(s.length() > 16) {
			prefix = s.substring(0, 16);
		}
		this.prefix = prefix;
		return this;
	}
	
	public NameTag suffix(String s) {
		String suffix = s;
		if(s.length() > 16) {
			suffix = s.substring(0, 16);
		}
		this.suffix = suffix;
		return this;
	}
	
	public NameTag skinUrl(String s) {
		this.skinUrl = s;
		return this;
	}
	
	public void send() {
		PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) p).getHandle());
		PacketPlayOutPlayerInfo add = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) p).getHandle());
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(((CraftPlayer) p).getHandle().getId());
		PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) p).getHandle());
		for(Player p : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(remove);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(add);
			if(!p.equals(this.p)) {
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(destroy);
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn);
			}
		}
	}

}
