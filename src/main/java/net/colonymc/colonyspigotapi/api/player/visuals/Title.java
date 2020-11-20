package net.colonymc.colonyspigotapi.api.player.visuals;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class Title {
	
	String text;
	int fadeIn;
	int duration;
	int fadeOut;
	TitleAction type;
	
	public Title(TitleAction type) {
		this.fadeIn = 20;
		this.duration = 100;
		this.fadeOut = 20;
		this.type = type;
	}
	
	public Title text(String s) {
		this.text = ChatColor.translateAlternateColorCodes('&', s);
		return this;
	}
	
	public Title fadeIn(int s) {
		this.fadeIn = s;
		return this;
	}
	
	public Title duration(int s) {
		this.duration = s;
		return this;
	}
	
	public Title fadeOut(int s) {
		this.fadeOut = s;
		return this;
	}
	
	public Title type(TitleAction s) {
		this.type = s;
		return this;
	}
	
	public void send(Player p) {
		if(type != TitleAction.ACTIOBAR) {
			IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + text + "\"}");
			PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.valueOf(type.name()), chatTitle);
			PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, duration, fadeOut);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
		}
		else {
			IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + text + "\"}");
			PacketPlayOutChat packet = new PacketPlayOutChat(chatTitle, (byte) 2);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

}
