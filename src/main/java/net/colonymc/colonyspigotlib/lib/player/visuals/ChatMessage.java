package net.colonymc.colonyspigotlib.lib.player.visuals;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.colonymc.colonyspigotlib.lib.utils.UtilFunctions;

public class ChatMessage {
	
	final String text;
	boolean centered;
	final ArrayList<CommandSender> players = new ArrayList<>();
	
	public ChatMessage(String text) {
		this.text = ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public ChatMessage centered(boolean t) {
		centered = t;
		return this;
	}
	
	public ChatMessage addRecipient(CommandSender p) {
		players.add(p);
		return this;
	}
	
	public void send() {
		for(CommandSender p : players) {
			if(centered) {
				p.sendMessage(UtilFunctions.getCenteredMessage(text));
			}
			else {
				p.sendMessage(text);
			}
		}
	}
	

}
