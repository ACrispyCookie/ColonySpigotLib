package net.colonymc.api.book;

import org.bukkit.entity.Player;

public class CommandAction extends BookAction {

	public CommandAction(String a) {
		super(a);
	}

	@Override
	public void perform(Player p) {
		p.chat(input);
		actions.remove(this);
	}

}
