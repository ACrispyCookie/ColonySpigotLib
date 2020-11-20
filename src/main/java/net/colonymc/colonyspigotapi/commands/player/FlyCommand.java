package net.colonymc.colonyspigotapi.commands.player;

import net.colonymc.colonyspigotapi.MainMessages;
import net.colonymc.colonyspigotapi.api.player.ColonyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/fly [player]");
			Player p = (Player) sender;
			if(p.hasPermission("colonyhub.fly") || p.hasPermission("staff.store")) {
				if(args.length == 0) {
					if(ColonyPlayer.getByPlayer(p).isFlying()) {
						p.setFlying(false);
						p.setAllowFlight(true);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have &cdisabled &fyour flight!"));
					}
					else {
						p.setAllowFlight(true);
						p.setFlying(true);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have &aenabled &fyour flight!"));
					}
					ColonyPlayer.getByPlayer(p).togglePlayerFlight();
				}
				else if(args.length == 1) {
					if(Bukkit.getPlayerExact(args[0]) != null) {
						Player pl = Bukkit.getPlayerExact(args[0]);
						if(ColonyPlayer.getByPlayer(pl).isFlying()) {
							pl.setFlying(false);
							pl.setAllowFlight(true);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have &cdisabled &fflight for the player &d" + pl.getName() + "&f!"));
						}
						else {
							pl.setAllowFlight(true);
							pl.setFlying(true);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have &aenabled &fflight for the player &d" + pl.getName() + "&f!"));
						}
						ColonyPlayer.getByPlayer(pl).togglePlayerFlight();
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
					}
				}
				else {
					p.sendMessage(usage);
				}
			}
			else {
				p.sendMessage(MainMessages.noPerm);
			}
		}
		else {
			String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/fly <player>");
			if(args.length == 1) {
				if(Bukkit.getPlayerExact(args[0]) != null) {
					Player pl = Bukkit.getPlayerExact(args[0]);
					if(ColonyPlayer.getByPlayer(pl).isFlying()) {
						pl.setFlying(false);
						pl.setAllowFlight(true);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have &cdisabled &fflight for the player &d" + pl.getName() + "&f!"));
					}
					else {
						pl.setAllowFlight(true);
						pl.setFlying(true);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have &aenabled &fflight for the player &d" + pl.getName() + "&f!"));
					}
					ColonyPlayer.getByPlayer(pl).togglePlayerFlight();
				}
				else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!"));
				}
			}
			else {
				sender.sendMessage(usage);
			}
		}
		return false;
	}

}
