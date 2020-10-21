package net.colonymc.api.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ImageMapBuilder implements Listener {
	
	Player p;
	Block pos1;
	Block pos2;
	Image img;
	static ArrayList<ImageMapBuilder> maps = new ArrayList<ImageMapBuilder>();
	
	public ImageMapBuilder(Player p, Image img) {
		this.p = p;
		this.img = img;
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease left-click a block to set the &dfirst position &fright-click a block to set the &dsecond position &fand run the command &d/mapimg build"));
		maps.add(this);
	}
	
	public ImageMapBuilder(Location pos1, Location pos2, Image img) {
		this.pos1 = pos1.getBlock();
		this.pos2 = pos2.getBlock();
		this.img = img;
		maps.add(this);
	}
	
	public ImageMapBuilder() {
		
	}
	
	public void setPos1(Block pos1) {
		this.pos1 = pos1;
	}
	
	public void setPos2(Block pos2) {
		this.pos2 = pos2;
	}
	
	public void cancel() {
		maps.remove(this);
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou have cancelled your current map creation!"));
	}
	
	@SuppressWarnings("deprecation")
	public void build() {
		int totalWidth = (Math.max(pos1.getLocation().getBlockX(), pos2.getLocation().getBlockX()) - Math.min(pos1.getLocation().getBlockX(), pos2.getLocation().getBlockX())) * 128 + 128;
		int totalHeight = (Math.max(pos1.getLocation().getBlockY(), pos2.getLocation().getBlockY()) - Math.min(pos1.getLocation().getBlockY(), pos2.getLocation().getBlockY())) * 128 + 128;
		BufferedImage img = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = img.createGraphics();
	    graphics2D.drawImage(this.img.getImg(), 0, 0, totalWidth, totalHeight, null);
	    graphics2D.dispose();
	    int xPixels = 0;
	    int yPixels = 0;
	    for(int x = pos1.getLocation().getBlockX(); x <= pos2.getLocation().getBlockX(); x++) {
	    	for(int y = pos1.getLocation().getBlockY(); y >= pos2.getLocation().getBlockY(); y--) {
	    		BufferedImage finalImg = img.getSubimage(xPixels, yPixels, 128, 128);
	    		Location loc = new Location(pos1.getWorld(), x, y, pos1.getLocation().getBlockZ());
	    		ItemFrame frame = null;
	    		if(exists(loc.clone().add(0, 0, 1)) == null) {
	    			frame = (ItemFrame) loc.getWorld().spawnEntity(loc.add(0, 0, 1), EntityType.ITEM_FRAME);
	    		}
	    		else {
	    			frame = exists(loc.clone().add(0, 0, 1));
	    		}
	    		ImageMap map = new ImageMap(finalImg, pos1.getWorld());
	    		ItemStack item = new ItemStack(Material.MAP);
	    		item.setDurability(map.getView().getId());
	    		frame.setItem(item);
	    		yPixels += 128;
	    	}
		    xPixels += 128;
		    yPixels = 0;
	    }
		maps.remove(this);
	}
	
	private ItemFrame exists(Location loc) {
		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, 0, 1), EntityType.ARMOR_STAND);
		for(Entity e : as.getNearbyEntities(0, 0, 0)) {
			if(e instanceof ItemFrame) {
				as.remove();
				return (ItemFrame) e;
			}
		}
		return null;
	}
	
	public static ImageMapBuilder getByPlayer(Player p) {
		for(ImageMapBuilder map : maps) {
			if(map.p.equals(p)) {
				return map;
			}
		}
		return null;
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if(ImageMapBuilder.getByPlayer(e.getPlayer()) != null) {
			ImageMapBuilder map = ImageMapBuilder.getByPlayer(e.getPlayer());
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				e.setCancelled(true);
				if(map.pos1 == null) {
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have set the second position of the image to &d[" + e.getClickedBlock().getX() + ", " + e.getClickedBlock().getY() + ", " + e.getClickedBlock().getZ() + "]"));
					map.setPos2(e.getClickedBlock());
				}
				else {
					if(e.getClickedBlock().getX() == map.pos1.getX() || e.getClickedBlock().getY() == map.pos1.getY() || e.getClickedBlock().getZ() == map.pos1.getZ()) {
						e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have set the second position of the image to &d[" + e.getClickedBlock().getX() + ", " + e.getClickedBlock().getY() + ", " + e.getClickedBlock().getZ() + "]"));
						map.setPos2(e.getClickedBlock());
						e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour map is ready! Type &d/mapimg build &fto build it!"));
					}
					else {
						e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease select a flat surface!"));
					}
				}
			}
			else if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
				e.setCancelled(true);
				if(map.pos2 == null) {
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have set the first position of the image to &d[" + e.getClickedBlock().getX() + ", " + e.getClickedBlock().getY() + ", " + e.getClickedBlock().getZ() + "]"));
					map.setPos1(e.getClickedBlock());
				}
				else {
					if(map.pos2.getX() == e.getClickedBlock().getX() || map.pos2.getY() == e.getClickedBlock().getY() || map.pos2.getZ() == e.getClickedBlock().getZ()) {
						e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have set the first position of the image to &d[" + e.getClickedBlock().getX() + ", " + e.getClickedBlock().getY() + ", " + e.getClickedBlock().getZ() + "]"));
						map.setPos1(e.getClickedBlock());
						e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour map is ready! Type &d/mapimg build &fto build it!"));
					}
					else {
						e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease select a flat surface!"));
					}
				}
			}
		}
	}

}
