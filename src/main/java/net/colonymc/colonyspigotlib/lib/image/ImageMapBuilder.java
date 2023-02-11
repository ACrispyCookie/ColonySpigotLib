package net.colonymc.colonyspigotlib.lib.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.colonymc.colonyspigotlib.Main;
import net.colonymc.colonyspigotlib.lib.itemstack.ItemStackBuilder;
import net.colonymc.colonyspigotlib.lib.itemstack.ItemStackNBT;
import net.colonymc.colonyspigotlib.lib.player.PlayerInventory;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ImageMapBuilder implements Listener {

	public enum Side {
		NORTH(1, 0),
		EAST(0, -1),
		SOUTH(-1, 0),
		WEST(0, 1);

		int toAddX;
		int toAddZ;
		Side(int toAddX, int toAddZ){
			this.toAddX = toAddX;
			this.toAddZ = toAddZ;
		}

		public int getX(){
			return toAddX;
		}

		public int getZ(){
			return toAddZ;
		}

		public static Side get90Degrees(Side side){
			if(side == Side.WEST){
				return Side.NORTH;
			}
			return Side.values()[side.ordinal() + 1];
		}
	}

	public enum COMMAND_TYPE{
		CONSOLE,
		PLAYER;

		public static COMMAND_TYPE getByChar(char c){
			if(c == 'c'){
				return CONSOLE;
			}
			else if(c == 'p'){
				return PLAYER;
			}
			return null;
		}
	}

	Player p;
	String name;
	String command;
	COMMAND_TYPE type;
	Side side;
	Block pos1;
	Block pos2;
	Image img;
	public abstract void onClick(Player p);
	static final ArrayList<ImageMapBuilder> maps = new ArrayList<>();
	
	public ImageMapBuilder(Player p, Image img, String name, String command, COMMAND_TYPE type) {
		this.name = name;
		this.command = command;
		this.type = type;
		this.p = p;
		this.img = img;
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease place the special block at the &dfirst position &fand then at the &dsecond position &fand finally run the command &d/mapimg build"));
		PlayerInventory.addItem(new ItemStackBuilder(Material.BARRIER).name("&cPlace at the corners of the banner...").addTag("mapimg", new NBTTagString(p.getName())).build(), p, 2);
		maps.add(this);
	}

	public ImageMapBuilder(Player p, Image img, String name) {
		this.name = name;
		this.p = p;
		this.img = img;
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease place the special block at the &dfirst position &fand then at the &dsecond position &fand finally run the command &d/mapimg build"));
		PlayerInventory.addItem(new ItemStackBuilder(Material.BARRIER).name("&cPlace at the corners of the banner...").addTag("mapimg", new NBTTagString(p.getName())).build(), p, 2);
		maps.add(this);
	}
	
	public ImageMapBuilder(Location pos1, Location pos2, Image img, String name, String command, COMMAND_TYPE type, Side side) {
		this.name = name;
		this.command = command;
		this.type = type;
		this.pos1 = pos1.getBlock();
		this.pos2 = pos2.getBlock();
		this.img = img;
		this.side = side;
		maps.add(this);
	}
	
	public ImageMapBuilder() {
		
	}
	
	public void setPos1(Block pos1, BlockFace face) {
		this.pos1 = pos1;
		this.side = Side.valueOf(face.name());
	}
	
	public void setPos2(Block pos2) {
		this.pos2 = pos2;
	}

	public Block getPos1(){
		return pos1;
	}

	public Block getPos2(){
		return pos2;
	}

	public COMMAND_TYPE getCommandType(){
		return type;
	}

	public String getCommand(){
		return command;
	}
	
	public void cancel() {
		maps.remove(this);
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou have cancelled your current map creation!"));
	}
	
	@SuppressWarnings("deprecation")
	public BuiltMap build() {
		pos1.setType(Material.AIR);
		pos2.setType(Material.AIR);
	    int xPixels = 0;
	    int yPixels = 0;
		int maxY = Math.max(pos1.getY(), pos2.getY());
		int minY = Math.min(pos1.getY(), pos2.getY());
		int max = pos1.getX() - pos2.getX() != 0 ? Math.max(pos1.getX(), pos2.getX()) : Math.max(pos1.getZ(), pos2.getZ());
		int min = pos1.getX() - pos2.getX() != 0 ? Math.min(pos1.getX(), pos2.getX()) : Math.min(pos1.getZ(), pos2.getZ());
		int totalWidth = (max - min) * 128 + 128;
		int totalHeight = (maxY - minY) * 128 + 128;
		BufferedImage img = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = img.createGraphics();
		graphics2D.drawImage(this.img.getImg(), 0, 0, totalWidth, totalHeight, null);
		graphics2D.dispose();
		ArrayList<ImageMap> imageMaps = new ArrayList<>();
		for(int changing = 0; changing <= max - min; changing++) {
			for(int y = maxY; y >= minY; y--) {
				BufferedImage finalImg = img.getSubimage(xPixels, yPixels, 128, 128);
				Location loc = new Location(
						pos1.getWorld(),
						pos1.getX() - pos2.getX() == 0 ? pos1.getX() : side.getX() < 0 ? min - changing * side.getX() : max - changing * side.getX(),
						y,
						pos1.getZ() - pos2.getZ() == 0 ? pos1.getZ() : side.getZ() < 0 ? max + changing * side.getZ() : min + changing * side.getZ()
				);
				ItemFrame frame = null;
				try{
					frame = (ItemFrame) loc.getWorld().spawnEntity(loc, EntityType.ITEM_FRAME);
				} catch (Exception e1){
					for(Entity e : loc.getChunk().getEntities()){
						if(e instanceof ItemFrame && e.getLocation().getBlockX() == loc.getBlockX() && e.getLocation().getBlockY() == loc.getBlockY() && e.getLocation().getBlockZ() == loc.getBlockZ()){
							frame = (ItemFrame) e;
							break;
						}
					}
				}
				ImageMap map = new ImageMap(finalImg, frame);
				imageMaps.add(map);
				ItemStack item = new ItemStack(Material.MAP);
				item.setDurability(map.getView().getId());
				frame.setItem(item);
				frame.setFacingDirection(BlockFace.valueOf(side.name()), true);
				yPixels += 128;
			}
			xPixels += 128;
			yPixels = 0;
		}
	    BuiltMap map = new BuiltMap(name, this.img, imageMaps, side, command, type) {
			@Override
			public void onClick(Player p) {
				ImageMapBuilder.this.onClick(p);
			}
		};
		maps.remove(this);
		return map;
	}
	
	public static ImageMapBuilder getByPlayer(Player p) {
		for(ImageMapBuilder map : maps) {
			if(map.p.equals(p)) {
				return map;
			}
		}
		return null;
	}

	public static ImageMapBuilder getByName(String name) {
		for (ImageMapBuilder map : maps) {
			if (map.name.equalsIgnoreCase(name)) {
				return map;
			}
		}
		return null;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(ImageMapBuilder.getByPlayer(e.getPlayer()) != null) {
			ImageMapBuilder map = ImageMapBuilder.getByPlayer(e.getPlayer());
			if(ItemStackNBT.hasTag(e.getItemInHand(), "mapimg")) {
				if(map.getPos1() == null) {
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have set the first position of the image to &d[" + e.getBlock().getX() + ", " + e.getBlock().getY() + ", " + e.getBlock().getZ() + "]"));
					map.setPos1(e.getBlock(), e.getBlock().getFace(e.getBlockAgainst()).getOppositeFace());
				}
				else if(map.getPos2() == null) {
					if((map.getPos1().getX() == e.getBlock().getX() || map.getPos1().getZ() == e.getBlock().getZ()) && map.getPos1().getY() != e.getBlock().getY()) {
						e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have set the second position of the image to &d[" + e.getBlock().getX() + ", " + e.getBlock().getY() + ", " + e.getBlock().getZ() + "]"));
						map.setPos2(e.getBlock());
						e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fPlease wait while we initialize your map!"));
						BuiltMap builtMap = map.build();
						Main.getInstance().addMapToConfig(builtMap);
					}
					else {
						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease select a flat vertical surface!"));
					}
				}
			}
		}
	}

}
