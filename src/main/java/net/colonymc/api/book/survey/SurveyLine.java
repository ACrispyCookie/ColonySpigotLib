package net.colonymc.api.book.survey;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class SurveyLine {
	
	final TextComponent text;
	BookAction action;
	final boolean button;
	int goTo = -1;
	final String[] commands;
	final String[] consoleCommands;
	final String key;
	final String value;
	
	protected SurveyLine(TextComponent text, boolean finalButton, String key, String value, String[] commands, String[] consoleCommands, int goTo) {
		this.text = text;
		this.button = finalButton;
		this.key = key;
		this.value = value;
		this.commands = commands;
		this.consoleCommands = consoleCommands;
		this.goTo = goTo;
	}
	
	protected void setBookAction(BookAction act) {
		if(isButton()) {
			this.action = act;
			this.text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bookaction " + act.getUuid()));
		}
	}
	
	protected BookAction getAction() {
		return action;
	}
	
	public TextComponent getText() {
		return text;
	}
	
	public boolean isButton() {
		return button;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public String[] getCommands() {
		return commands;
	}
	
	public String[] getConsoleCommands() {
		return consoleCommands;
	}
	
	public int getGoTo() {
		return goTo;
	}
}
