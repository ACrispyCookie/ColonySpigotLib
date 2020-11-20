package net.colonymc.colonyspigotapi.commands.hologram;

import net.colonymc.colonyspigotapi.Main;
import net.colonymc.colonyspigotapi.MainMessages;
import net.colonymc.colonyspigotapi.api.holograms.PublicHologram;
import net.colonymc.colonyspigotapi.api.player.visuals.ChatMessage;
import net.colonymc.colonyspigotapi.api.primitive.Numbers;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HologramCommand implements CommandExecutor {

	final String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/holo create/addline/removeline/setline/list <name> <text/index> [text]");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("colonyhub.hologram")) {
				if(args.length == 0) {
					p.sendMessage(usage);
				}
				else if(args.length == 1) {
					String arg = args[0];
					if(arg.equalsIgnoreCase("list")) {
						if(PublicHologram.holos.size() > 0) {
							new ChatMessage("&5&lHologram List").addRecipient(p).centered(true).send();
							for(PublicHologram h : PublicHologram.holos) {
								if(h.getName() != null) {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + h.getName() + " &fat the location &d[" + h.getLocation().getBlockX() + ", " + h.getLocation().getBlockY() + ", " + h.getLocation().getBlockZ() + "]"));
								}
							}
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThere are no holograms currently on this server!"));
						}
					}
					else {
						p.sendMessage(usage);
					}
				}
				else {
					String arg = args[0];
					if(arg.equalsIgnoreCase("remove")) {
						if(args.length == 2){
							if(PublicHologram.getByName(args[1]) != null) {
								PublicHologram holo = PublicHologram.getByName(args[1]);
								PublicHologram.getByName(args[1]).destroy();
								Main.getInstance().removeHologramFromConfig(holo);
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou removed the hologram &d" + args[1] + "&f!"));
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis hologram doesn't exist!"));
							}
						}
						else {
							p.sendMessage(usage);
						}
					}
					else if(arg.equalsIgnoreCase("movehere")) {
						if(args.length == 2){
							if(PublicHologram.getByName(args[1]) != null) {
								PublicHologram holo = PublicHologram.getByName(args[1]);
								holo.setLocation(p.getLocation());
								Main.getInstance().updateHologramOnConfig(holo);
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou moved the hologram &d" + args[1] + " &fto your location!"));
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis hologram doesn't exist!"));
							}
						}
						else {
							p.sendMessage(usage);
						}
					}
					else if(arg.equalsIgnoreCase("create")) {
						if(args.length >= 3){
							if(PublicHologram.getByName(args[1]) == null) {
								PublicHologram holo = null;
								if(!args[2].equals("{empty}")){
									StringBuilder text = new StringBuilder();
									for(int i = 2; i < args.length; i++){
										text.append(args[i]).append(" ");
									}
									text.deleteCharAt(text.length() - 1);
									holo = new PublicHologram(text.toString(), p.getLocation(), args[1]);
								}
								else{
									holo = new PublicHologram(" ", p.getLocation(), args[1]);
								}
								holo.show();
								Main.getInstance().updateHologramOnConfig(holo);
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou created a hologram with the name &d" + args[1] + "&f!"));
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis hologram already exists!"));
							}
						}
						else {
							p.sendMessage(usage);
						}
					}
					else if(arg.equalsIgnoreCase("addline")) {
						if(args.length >= 3){
							int startingIndex = Numbers.isInt(args[2]) ? 3 : 2;
							if(PublicHologram.getByName(args[1]) != null) {
								String finalText = "";
								if(!args[startingIndex].equals("{empty}")){
									StringBuilder text = new StringBuilder();
									for(int i = startingIndex; i < args.length; i++){
										text.append(args[i]).append(" ");
									}
									text.deleteCharAt(text.length() - 1);
									finalText = text.toString();
								}
								else{
									finalText = " ";
								}
								if(startingIndex == 3){
									if(Integer.parseInt(args[2]) < PublicHologram.getByName(args[1]).getLineCount()) {
										PublicHologram.getByName(args[1]).addLine(Integer.parseInt(args[2]), finalText);
									} else {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis line doesn't exist!"));
									}
								}
								else{
									PublicHologram.getByName(args[1]).addLine(finalText);
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou added a line on the hologram &d" + args[1] + "&f!"));
								}
								Main.getInstance().updateHologramOnConfig(PublicHologram.getByName(args[1]));
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis hologram doesn't exist!"));
							}
						}
						else {
							p.sendMessage(usage);
						}
					}
					else if(arg.equalsIgnoreCase("removeline")) {
						if(args.length == 3) {
							if(PublicHologram.getByName(args[1]) != null) {
								if(Numbers.isInt(args[2])) {
									if(Integer.parseInt(args[2]) >= 0 && Integer.parseInt(args[2]) < PublicHologram.getByName(args[1]).getLineCount()) {
										PublicHologram.getByName(args[1]).removeLine(Integer.parseInt(args[2]));
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou removed the line &d" + args[2] + " &ffrom the hologram &d" + args[1] + "&f!"));
										Main.getInstance().updateHologramOnConfig(PublicHologram.getByName(args[1]));
									}
									else {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid index!"));
									}
								}
								else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
								}
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis hologram doesn't exist!"));
							}
						}
						else {
							p.sendMessage(usage);
						}
					}
					else if(arg.equalsIgnoreCase("setline")) {
						if(PublicHologram.getByName(args[1]) != null) {
							if(Numbers.isInt(args[2])) {
								if(Integer.parseInt(args[2]) >= 0 && Integer.parseInt(args[2]) < PublicHologram.getByName(args[1]).getLineCount()) {
									if(!args[3].equals("{empty}")){
										StringBuilder text = new StringBuilder();
										for(int i = 3; i < args.length; i++){
											text.append(args[i]).append(" ");
										}
										text.deleteCharAt(text.length() - 1);
										PublicHologram.getByName(args[1]).setLine(Integer.parseInt(args[2]), text.toString());
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set the line &d" + args[2] + " &fto &d" + text.toString() + " &fon the hologram &d" + args[1] + "&f!"));
									}
									else{
										PublicHologram.getByName(args[1]).setLine(Integer.parseInt(args[2]), " ");
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set the line &d" + args[2] + " &fto &d{empty} &fon the hologram &d" + args[1] + "&f!"));
									}
									Main.getInstance().updateHologramOnConfig(PublicHologram.getByName(args[1]));
								}
								else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid index!"));
								}
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number!"));
							}
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis hologram doesn't exist!"));
						}
					}
					else {
						p.sendMessage(usage);
					}
				}
			}
			else {
				p.sendMessage(MainMessages.noPerm);
			}
		}
		else {
			sender.sendMessage(MainMessages.onlyPlayers);
		}
		return false;
	}
	

}
