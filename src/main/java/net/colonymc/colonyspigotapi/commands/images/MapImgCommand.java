package net.colonymc.colonyspigotapi.commands.images;

import net.colonymc.colonyspigotapi.Main;
import net.colonymc.colonyspigotapi.MainMessages;
import net.colonymc.colonyspigotapi.api.image.BuiltMap;
import net.colonymc.colonyspigotapi.api.image.Image;
import net.colonymc.colonyspigotapi.api.image.ImageMapBuilder;
import net.colonymc.colonyspigotapi.api.itemstack.ItemStackBuilder;
import net.colonymc.colonyspigotapi.api.itemstack.ItemStackNBT;
import net.colonymc.colonyspigotapi.api.player.PlayerInventory;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class MapImgCommand implements CommandExecutor, TabCompleter, Listener {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("cancel")) {
					if(ImageMapBuilder.getByPlayer(p) != null) {
						ImageMapBuilder.getByPlayer(p).cancel();
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou currently aren't creating any map! Type /mapimg <img> to start a new creation!"));
					}
				}
				else if(args[0].equalsIgnoreCase("reset")) {
					if(ImageMapBuilder.getByPlayer(p) != null) {
						ImageMapBuilder.getByPlayer(p).setPos1(null, null);
						ImageMapBuilder.getByPlayer(p).setPos2(null);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou have reset your map selection! Please make a new one!"));
						PlayerInventory.addItem(new ItemStackBuilder(Material.BARRIER).name("&cPlace at the corners of the banner...").addTag("mapimg", new NBTTagString(p.getName())).build(), p, 2);
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou currently aren't creating any map! Type /mapimg <img> to start a new creation!"));
					}
				}
				else if(args[0].equalsIgnoreCase("list")){
					StringBuilder s = new StringBuilder(" &5&l» &cThere are no available maps right now!");
					for(int i = 0; i < BuiltMap.list().size(); i++){
						BuiltMap map = BuiltMap.list().get(i);
						if(i == 0){
							s = new StringBuilder("                             &5&lAvailable Maps");
						}
						Location pos1 = map.getPos1();
						Location pos2 = map.getPos2();
						s.append("\n &5&l» &d").append(map.getName()).append(" &fat &d[").append(pos1.getBlockX()).append(", ").append(pos1.getBlockY()).append(", ").append(pos1.getBlockZ()).append("] &fwith the image &d").append(map.getImg().getName()).append(" &fand a size of &d[").append(Math.abs(pos1.getBlockY() - pos2.getBlockY()) + 1).append(", ").append(pos1.getBlockX() - pos2.getBlockX() == 0 ? Math.abs(pos1.getBlockZ() - pos2.getBlockZ()) + 1 : Math.abs(pos1.getBlockX() - pos2.getBlockX()) + 1).append("]");
					}
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.toString()));
				}
				else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/mapimg create/build/cancel/list/remove [img] <name> -c(onsole)/-p(player) [command]"));
				}
			}
			else if(args.length == 2){
				if(args[0].equalsIgnoreCase("remove")){
					if(BuiltMap.getByName(args[1]) != null){
						Main.getInstance().removeMapFromConfig(BuiltMap.getByName(args[1]));
						BuiltMap.getByName(args[1]).remove();
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have removed the map &d" + args[1] + "&f!"));
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis map doesn't exist!"));
					}
				}
				else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/mapimg create/build/cancel/list/remove [img] <name> -c(onsole)/-p(player) [command]"));
				}
			}
			else if(args.length == 3){
				if(args[0].equalsIgnoreCase("create")) {
					if(Image.getByName(args[1]) != null) {
						if(ImageMapBuilder.getByPlayer(p) == null) {
							if(BuiltMap.getByName(args[2]) == null && ImageMapBuilder.getByName(args[2]) == null){
								new ImageMapBuilder(p, Image.getByName(args[1]), args[2]) {
									@Override
									public void onClick(Player p) {

									}
								};
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis map already exists or it's being created!"));
							}
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease finish with your current map! If you want to cancel your current map type /mapimg cancel."));
						}
					}
				}
				else{
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/mapimg create/build/cancel/list/remove [img] <name> -c(onsole)/-p(player) [command]"));
				}
			}
			else if(args.length > 4){
				if(args[0].equalsIgnoreCase("create")){
					if(Image.getByName(args[1]) != null) {
						if(ImageMapBuilder.getByPlayer(p) == null) {
							if(BuiltMap.getByName(args[2]) == null && ImageMapBuilder.getByName(args[2]) == null){
								if(args[3].equalsIgnoreCase("-c")){
									StringBuilder s = new StringBuilder(args[4]);
									for(int i = 5; i < args.length; i++){
										s.append(" ").append(args[i]);
									}
									new ImageMapBuilder(p, Image.getByName(args[1]), args[2], s.toString().replaceAll("%name%", p.getName()), ImageMapBuilder.COMMAND_TYPE.CONSOLE) {
										@Override
										public void onClick(Player p) {
											Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.toString().replaceAll("%name%", p.getName()));
										}
									};
								}
								else if(args[3].equalsIgnoreCase("-p")){
									StringBuilder s = new StringBuilder(args[4]);
									for(int i = 4; i < args.length; i++){
										s.append(" ").append(args[i]);
									}
									new ImageMapBuilder(p, Image.getByName(args[1]), args[2], s.toString().replaceAll("%name%", p.getName()), ImageMapBuilder.COMMAND_TYPE.PLAYER) {
										@Override
										public void onClick(Player p) {
											Bukkit.dispatchCommand(p, s.toString().replaceAll("%name%", p.getName()));
										}
									};
								}
								else{
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/mapimg create/build/cancel/list/remove [img] <name> -c(onsole)/-p(player) [command]"));
								}
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis map already exists or it's being created!"));
							}
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease finish with your current map! If you want to cancel your current map type /mapimg cancel."));
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &cThis image doesn't exist"));
					}
				}
				else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/mapimg create/build/cancel/list/remove [img] <name> -c(onsole)/-p(player) [command]"));
				}
			}
			else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/mapimg create/build/cancel/list/remove [img] <name> -c(onsole)/-p(player) [command]"));
			}
		}
		else {
			sender.sendMessage(MainMessages.noPerm);
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("remove")){
				List<String> list = new ArrayList<>();
				String search = args[1].toLowerCase();
				for(BuiltMap map : BuiltMap.list()) {
					if(map.getName().toLowerCase().startsWith(search)) {
						list.add(map.getName());
					}
				}
				return list;
			}
			else{
				List<String> list = new ArrayList<>();
				String search = args[1].toLowerCase();
				for(Image img : Image.getImgs()) {
					if(img.getName().toLowerCase().startsWith(search)) {
						list.add(img.getName());
					}
				}
				return list;
			}
		}
		return null;
	}
}
