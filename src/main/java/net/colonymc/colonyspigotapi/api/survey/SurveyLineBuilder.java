package net.colonymc.colonyspigotapi.api.survey;

import org.bukkit.ChatColor;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class SurveyLineBuilder {
	
	final String text;
	String hoverText = null;
	String value = null;
	String key = null;
	String[] commands = null;
	String[] consoleCommands = null;
	int goTo = -1;
	boolean isButton = false;
	
	public SurveyLineBuilder(String text) {
		this.text = text;
	}
	
	public SurveyLineBuilder commands(String[] cmd) {
		this.commands = cmd;
		return this;
	}
	
	public SurveyLineBuilder consoleCommands(String[] cmd) {
		this.consoleCommands = cmd;
		return this;
	}
	
	public SurveyLineBuilder goTo(int i) {
		this.goTo = i;
		return this;
	}
	
	public SurveyLineBuilder value(String k) {
		this.value= k;
		return this;
	}
	
	public SurveyLineBuilder key(String k) {
		this.key = k;
		return this;
	}
	
	public SurveyLineBuilder hoverText(String b) {
		this.hoverText = b;
		return this;
	}
	
	public SurveyLineBuilder button(boolean b) {
		this.isButton = b;
		return this;
	}
	
	public SurveyLine build() {
		TextComponent t = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
		if(hoverText != null) {
			t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {new TextComponent(hoverText)}));
		}
        return new SurveyLine(t, isButton, key, value, commands, consoleCommands, goTo);
	}

}
