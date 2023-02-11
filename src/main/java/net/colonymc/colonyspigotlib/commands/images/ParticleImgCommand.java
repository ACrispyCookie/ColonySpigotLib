package net.colonymc.colonyspigotlib.commands.images;

import net.colonymc.colonyspigotlib.MainMessages;
import net.colonymc.colonyspigotlib.lib.image.Image;
import net.colonymc.colonyspigotlib.lib.particles.ParticleShape;
import net.colonymc.colonyspigotlib.lib.primitive.Numbers;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ParticleImgCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 1) {
				if(Image.getByName(args[0]) != null) {
					if(Image.getByName(args[0]).getImg().getWidth() * Image.getByName(args[0]).getImg().getHeight() <= 64 * 64) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fRendering image &d" + Image.getByName(args[0]).getName() + " &fat a distance of &d10 blocks &ffor &d5 &fseconds!"));
						ParticleShape par = new ParticleShape(p);
						par.playImage(Image.getByName(args[0]), p.getLocation().add(p.getLocation().getDirection().multiply(10)), 100);
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis image is bigger than 64x64!"));
					}
				}
				else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis image doesn't exist!"));
				}
			}
			else if(args.length == 2) {
				if(Image.getByName(args[0]) != null) {
					if(Numbers.isInt(args[1]) && Integer.parseInt(args[1]) >= 0) {
						if(Image.getByName(args[0]).getImg().getWidth() * Image.getByName(args[0]).getImg().getHeight() <= 64 * 64) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fRendering image &d" + Image.getByName(args[0]).getName() + " &fat a distance of &d" + args[1] + " blocks &ffor &d5 &fseconds!"));
							ParticleShape par = new ParticleShape(p);
							par.playImage(Image.getByName(args[0]), p.getLocation().add(p.getLocation().getDirection().multiply(Integer.parseInt(args[1]))), 100);
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis image is bigger than 64x64!"));
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid non-negative number!"));
					}
				}
				else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis image doesn't exist!"));
				}
			}
			else if(args.length == 3) {
				if(Image.getByName(args[0]) != null) {
					if(Numbers.isInt(args[1]) && Integer.parseInt(args[1]) >= 0) {
						if(Numbers.isInt(args[2]) && Integer.parseInt(args[2]) > 0) {
							if(Image.getByName(args[0]).getImg().getWidth() * Image.getByName(args[0]).getImg().getHeight() <= 64 * 64) {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fRendering image &d" + Image.getByName(args[0]).getName() + " &fat a distance of &d" + args[1] + " blocks &ffor &d" + args[2] + " &fseconds!"));
								ParticleShape par = new ParticleShape(p);
								par.playImage(Image.getByName(args[0]), p.getLocation().add(p.getLocation().getDirection().multiply(Integer.parseInt(args[1]))), Integer.parseInt(args[2]) * 20);
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis image is bigger than 64x64!"));
							}
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid non-negative number!"));
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid number bigger than 0!"));
					}
				}
				else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis image doesn't exist!"));
				}
			}
			else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/particleimg <img> [blocks away] [duration]"));
			}
		}
		else {
			sender.sendMessage(MainMessages.noPerm);
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 1) {
			List<String> list = new ArrayList<>();
			String search = args[0].toLowerCase();
			for(Image img : Image.getImgs()) {
				if(img.getName().toLowerCase().startsWith(search)) {
					list.add(img.getName());
				}
			}
			return list;
		}
		return null;
	}

}
