package net.colonymc.colonyspigotapi.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class ImageMap implements Listener {
	
	BufferedImage img;
	ItemFrame itemFrame;
	MapView map;
	final HashSet<UUID> uuids = new HashSet<>();
	static final ArrayList<ImageMap> maps = new ArrayList<>();
	
	public ImageMap(BufferedImage img, ItemFrame itemFrame) {
		this.itemFrame = itemFrame;
		this.img = img;
		this.map = Bukkit.createMap(itemFrame.getWorld());
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

	public void remove(){
		maps.remove(this);
		itemFrame.setItem(new ItemStack(Material.AIR));
		itemFrame.remove();
	}
	
	public BufferedImage getImage() {
		return img;
	}
	
	public MapView getView() {
		return map;
	}

	public ItemFrame getItemFrame() { return itemFrame; }
	
	public static ImageMap getById(MapView view) {
		for(ImageMap map : maps) {
			if(map.getView().equals(view)) {
				return map;
			}
		}
		return null;
	}

	public static ImageMap getByItemFrame(ItemFrame frame) {
		for(ImageMap map : maps) {
			if(map.getItemFrame().equals(frame)) {
				return map;
			}
		}
		return null;
	}

	public static ArrayList<ImageMap> list(){
		return maps;
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
