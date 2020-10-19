package net.colonymc.api.image;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapView;

public class ImageMap implements Listener {
	
	@EventHandler
	public void onInitialize(MapInitializeEvent e) {
		MapView m = e.getMap();
		
	}

}
