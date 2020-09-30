package net.colonymc.api.book.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class Survey {
	
	UUID uuid;
	int currentIndex = 0;
	ArrayList<SurveyBook> pages = new ArrayList<SurveyBook>();
	HashMap<String, String> values = new HashMap<String, String>();
	static ArrayList<Survey> surveys = new ArrayList<Survey>();
	public abstract void onComplete(HashMap<String, String> values, Player p);
	
	public Survey(int pages) {
		uuid = UUID.randomUUID();
		while(uuidExists()) {
			this.uuid = UUID.randomUUID();
		}
	}
	
	public void addBook(SurveyBook book) {
		pages.add(book);
	}
	
	public void open(Player p) {
		for(int i = 0; i < pages.size(); i++) {
			SurveyBook b = pages.get(i);
			for(SurveyLine l : b.getLines()) {
				if(l.isButton()) {
					if(i + 1 < pages.size()) {
						l.setBookAction(new BookAction() {
							@Override
							public void perform(Player p) {
								if(l.getKey() != null) {
									values.put(l.getKey(), l.getValue());
								}
								if(l.getCommands() != null) {
									for(String s : l.getCommands()) {
										p.chat(s);
									}
								}
								if(l.getConsoleCommands() != null) {
									for(String s : l.getConsoleCommands()) {
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
									}
								}
								if(l.getGoTo() != -1) {
									currentIndex = l.getGoTo();
								}
								else {
									currentIndex++;
								}
								Survey.this.pages.get(currentIndex).open(p);
								if(currentIndex == pages.size() - 1) {
									onComplete(Survey.this.getValues(), p);
								}
							}
						});
					}
				}
			}
		}
		pages.get(0).open(p);
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public ArrayList<SurveyBook> getPages() {
		return pages;
	}
	
	public HashMap<String, String> getValues() {
		return values;
	}
	
	private boolean uuidExists() {
		for(SurveyBook a : pages) {
			if(a.getUuid().equals(uuid)) {
				return true;
			}
		}
		return false;
	}
	
	public static Survey getByUuid(UUID uuid) {
		for(Survey s : surveys) {
			if(s.getUuid().equals(uuid)) {
				return s;
			}
		}
		return null;
	}

}
