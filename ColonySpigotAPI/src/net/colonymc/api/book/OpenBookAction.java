package net.colonymc.api.book;

import java.util.UUID;

import org.bukkit.entity.Player;

public class OpenBookAction extends BookAction {

	public OpenBookAction(String a) {
		super(a);
	}

	@Override
	public void perform(Player p) {
		Book.getByUuid(UUID.fromString(input)).open(p);
		actions.remove(this);
	}

}
