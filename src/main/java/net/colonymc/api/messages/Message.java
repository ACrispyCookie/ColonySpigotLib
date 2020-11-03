package net.colonymc.api.messages;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.colonymc.api.utils.UtilFunctions;

public class Message {
	
	final String text;
	boolean centered;
	final ArrayList<CommandSender> players = new ArrayList<>();
	
	public Message(String text) {
		this.text = ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public Message centered(boolean t) {
		centered = t;
		return this;
	}
	
	public Message addRecipient(CommandSender p) {
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
