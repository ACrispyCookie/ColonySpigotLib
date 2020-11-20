package net.colonymc.colonyspigotapi.commands.player;

import net.colonymc.colonyspigotapi.MainMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GamemodeCommand implements CommandExecutor {
	
	final String[] c = new String[] {"c", "1", "creative"};
	final String[] s = new String[] {"s", "0", "survival"};
	final String[] a = new String[] {"a", "2", "adveture"};
	final String[] sp = new String[] {"sp", "3", "spectator"};
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			String usage;
			if(p.hasPermission("colonyhub.gamemode")) {
				if(cmd.getName().equalsIgnoreCase("gamemode")) {
					usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + cmd.getName() + " <gamemode> [player]");
					if(args.length == 0) {
						p.sendMessage(usage);
					}
					else if(args.length == 1) {
						String identifier = args[0];
						if(Arrays.asList(c).contains(identifier)) {
							p.setGameMode(GameMode.CREATIVE);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your gamemode to &dcreative&f!"));
						}
						else if(Arrays.asList(s).contains(identifier)) {
							p.setGameMode(GameMode.SURVIVAL);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your gamemode to &dsurvival&f!"));
						}
						else if(Arrays.asList(a).contains(identifier)) {
							p.setGameMode(GameMode.ADVENTURE);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your gamemode to &dadveture&f!"));
						}
						else if(Arrays.asList(sp).contains(identifier)) {
							p.setGameMode(GameMode.SPECTATOR);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your gamemode to &dspectator&f!"));
						}
					}
					else if(args.length == 2) {
						String identifier = args[0];
						String playerName = args[1];
						if(Bukkit.getPlayerExact(playerName) != null) {
							if(Arrays.asList(c).contains(identifier)) {
								Bukkit.getPlayerExact(playerName).setGameMode(GameMode.CREATIVE);
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dcreative&f!"));
							}
							else if(Arrays.asList(s).contains(identifier)) {
								Bukkit.getPlayerExact(playerName).setGameMode(GameMode.SURVIVAL);
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dsurvival&f!"));
							}
							else if(Arrays.asList(a).contains(identifier)) {
								Bukkit.getPlayerExact(playerName).setGameMode(GameMode.ADVENTURE);
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dadveture&f!"));
							}
							else if(Arrays.asList(sp).contains(identifier)) {
								Bukkit.getPlayerExact(playerName).setGameMode(GameMode.SPECTATOR);
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dspectator&f!"));
							}
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
						}
					}
					else {
						p.sendMessage(usage);
					}
				}
				else if(cmd.getName().equalsIgnoreCase("gmc")) {
					usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + cmd.getName() + " [player]");
					if(args.length == 0) {
						p.setGameMode(GameMode.CREATIVE);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your gamemode to &dcreative&f!"));
					}
					else if(args.length == 1) {
						String playerName = args[0];
						if(Bukkit.getPlayerExact(playerName) != null) {
							Bukkit.getPlayerExact(playerName).setGameMode(GameMode.CREATIVE);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dcreative&f!"));
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
						}
					}
					else {
						p.sendMessage(usage);
					}
				}
				else if(cmd.getName().equalsIgnoreCase("gms")) {
					usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + cmd.getName() + " [player]");
					if(args.length == 0) {
						p.setGameMode(GameMode.SURVIVAL);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your gamemode to &dsurvival&f!"));
					}
					else if(args.length == 1) {
						String playerName = args[0];
						if(Bukkit.getPlayerExact(playerName) != null) {
							Bukkit.getPlayerExact(playerName).setGameMode(GameMode.SURVIVAL);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dsurvival&f!"));
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
						}
					}
					else {
						p.sendMessage(usage);
					}
				}
				else if(cmd.getName().equalsIgnoreCase("gma")) {
					usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + cmd.getName() + " [player]");
					if(args.length == 0) {
						p.setGameMode(GameMode.ADVENTURE);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your gamemode to &dadveture&f!"));
					}
					else if(args.length == 1) {
						String playerName = args[0];
						if(Bukkit.getPlayerExact(playerName) != null) {
							Bukkit.getPlayerExact(playerName).setGameMode(GameMode.ADVENTURE);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dadventure&f!"));
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
						}
					}
					else {
						p.sendMessage(usage);
					}
				}
				else if(cmd.getName().equalsIgnoreCase("gmsp")) {
					usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + cmd.getName() + " [player]");
					if(args.length == 0) {
						p.setGameMode(GameMode.SPECTATOR);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set your gamemode to &dspectator&f!"));
					}
					else if(args.length == 1) {
						String playerName = args[0];
						if(Bukkit.getPlayerExact(playerName) != null) {
							Bukkit.getPlayerExact(playerName).setGameMode(GameMode.SPECTATOR);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dspectator&f!"));
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
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
			String usage;
			if(cmd.getName().equalsIgnoreCase("gamemode")) {
				usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + cmd.getName() + " <gamemode> <player>");
				if(args.length == 0) {
					sender.sendMessage(usage);
				}
				else if(args.length == 2) {
					String identifier = args[0];
					String playerName = args[1];
					if(Bukkit.getPlayerExact(playerName) != null) {
						if(Arrays.asList(c).contains(identifier)) {
							Bukkit.getPlayerExact(playerName).setGameMode(GameMode.CREATIVE);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dcreative&f!"));
						}
						else if(Arrays.asList(s).contains(identifier)) {
							Bukkit.getPlayerExact(playerName).setGameMode(GameMode.SURVIVAL);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dsurvival&f!"));
						}
						else if(Arrays.asList(a).contains(identifier)) {
							Bukkit.getPlayerExact(playerName).setGameMode(GameMode.ADVENTURE);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dadveture&f!"));
						}
						else if(Arrays.asList(sp).contains(identifier)) {
							Bukkit.getPlayerExact(playerName).setGameMode(GameMode.SPECTATOR);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dspectator&f!"));
						}
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
				}
				else {
					sender.sendMessage(usage);
				}
			}
			else if(cmd.getName().equalsIgnoreCase("gmc")) {
				usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + cmd.getName() + " <player>");
				if(args.length == 1) {
					String playerName = args[0];
					if(Bukkit.getPlayerExact(playerName) != null) {
						Bukkit.getPlayerExact(playerName).setGameMode(GameMode.CREATIVE);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dcreative&f!"));
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
				}
				else {
					sender.sendMessage(usage);
				}
			}
			else if(cmd.getName().equalsIgnoreCase("gms")) {
				usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + cmd.getName() + " <player>");
				if(args.length == 1) {
					String playerName = args[0];
					if(Bukkit.getPlayerExact(playerName) != null) {
						Bukkit.getPlayerExact(playerName).setGameMode(GameMode.SURVIVAL);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dsurvival&f!"));
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
				}
				else {
					sender.sendMessage(usage);
				}
			}
			else if(cmd.getName().equalsIgnoreCase("gma")) {
				usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + cmd.getName() + " <player>");
				if(args.length == 1) {
					String playerName = args[0];
					if(Bukkit.getPlayerExact(playerName) != null) {
						Bukkit.getPlayerExact(playerName).setGameMode(GameMode.ADVENTURE);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dadventure&f!"));
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
				}
				else {
					sender.sendMessage(usage);
				}
			}
			else if(cmd.getName().equalsIgnoreCase("gmsp")) {
				usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/" + cmd.getName() + " <player>");
				if(args.length == 1) {
					String playerName = args[0];
					if(Bukkit.getPlayerExact(playerName) != null) {
						Bukkit.getPlayerExact(playerName).setGameMode(GameMode.SPECTATOR);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + Bukkit.getPlayerExact(playerName).getName() + "'s &fgamemode to &dspectator&f!"));
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
				}
				else {
					sender.sendMessage(usage);
				}
			}
		}
		return false;
	}
	
	

}
