package net.colonymc.api.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class ImageMap implements Listener {
	
	BufferedImage img;
	MapView map;
	final HashSet<UUID> uuids = new HashSet<>();
	static final ArrayList<ImageMap> maps = new ArrayList<>();
	
	public ImageMap(BufferedImage img, World w) {
		this.img = img;
		this.map = Bukkit.createMap(w);
		map.getRenderers().forEach((a) -> map.removeRenderer(a));
		map.addRenderer(new MapRenderer() {
			@Override
			public void render(MapView map, MapCanvas c, Player p) {
				if(!uuids.contains(p.getUniqueId())) {
					c.drawImage(0, 0, getImage());
					uuids.add(p.getUniqueId());
				}
			}
		});
		maps.add(this);
	}
	
	public ImageMap() {
	}
	
	public BufferedImage getImage() {
		return img;
	}
	
	public MapView getView() {
		return map;
	}
	
	public static ImageMap getById(MapView view) {
		for(ImageMap map : maps) {
			if(map.getView().equals(view)) {
				return map;
			}
		}
		return null;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		ImageMap.maps.forEach((a) -> a.uuids.remove(e.getPlayer().getUniqueId()));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerKickEvent e) {
		ImageMap.maps.forEach((a) -> a.uuids.remove(e.getPlayer().getUniqueId()));
	}

}
