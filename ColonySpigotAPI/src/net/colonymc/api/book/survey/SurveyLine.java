package net.colonymc.api.book.survey;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class SurveyLine {
	
	TextComponent text;
	BookAction action;
	boolean button;
	String key;
	String value;
	
	protected SurveyLine(TextComponent text, boolean finalButton, String key, String value) {
		this.text = text;
		this.button = finalButton;
		this.key = key;
		this.value = value;
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
}
