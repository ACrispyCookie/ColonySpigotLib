package net.colonymc.colonyspigotlib.commands.images;

import net.colonymc.colonyspigotlib.Main;
import net.colonymc.colonyspigotlib.lib.image.Image;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ReloadImagesCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("*")) {
			File imgs = Main.getInstance().getImgs();
			if(imgs != null) {
				Image.clearList();
				for(File f : imgs.listFiles()) {
					try {
						BufferedImage img = ImageIO.read(f);
						if(img != null) {
							new Image(FilenameUtils.removeExtension(f.getName()), img);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fAll images have been reloaded!"));
		}
		return false;
	}

}
