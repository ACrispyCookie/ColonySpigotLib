package net.colonymc.api.book;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public abstract class BookAction {
	
	UUID uuid;
	String input;
	static ArrayList<BookAction> actions = new ArrayList<BookAction>();
	public abstract void perform(Player p);
	
	public BookAction(String a) {
		uuid = generateUuid();
		this.input = a;
		actions.add(this);
	}
	
	public BookAction() {
		uuid = generateUuid();
		actions.add(this);
	}

	public UUID getUuid() {
		return uuid;
	}
	
	public String getInput() {
		return input;
	}
	
	private UUID generateUuid() {
		UUID uuid = UUID.randomUUID();
		while(uuidExists(uuid)) {
			uuid = UUID.randomUUID();
		}
		return uuid;
	}
	
	private boolean uuidExists(UUID uuid) {
		for(BookAction a : actions) {
			if(a.getUuid().equals(uuid)) {
				return true;
			}
		}
		return false;
	}
	
	public static BookAction getAction(UUID uuid) {
		for(BookAction a : actions) {
			if(a.getUuid().toString().equals(uuid.toString())) {
				return a;
			}
		}
		return null;
	}

}
