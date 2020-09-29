package net.colonymc.api.book;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Line {
	
	TextComponent text;
	BookAction action;
	
	public Line(TextComponent text, BookAction act) {
		this.text = text;
		this.action = act;
		this.text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bookaction " + act.getUuid().toString()));
	}
	
	public Line(TextComponent text) {
		this.text = text;
		this.action = null;
	}
	
	public TextComponent getText() {
		return text;
	}
	
	public BookAction getAction() {
		return action;
	}
	
	public boolean hasAction() {
		return action != null;
	}

}
