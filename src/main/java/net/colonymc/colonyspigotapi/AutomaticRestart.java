package net.colonymc.colonyspigotapi;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.colonymc.colonyspigotapi.player.Title;
import net.colonymc.colonyspigotapi.player.TitleAction;
import net.md_5.bungee.api.ChatColor;

public class AutomaticRestart {
	
	final int amount = 200;
	boolean started = false;
	final BukkitTask task;
	BukkitTask restart;
	
	public AutomaticRestart() {
		task = new BukkitRunnable() {
			@Override
			public void run() {
				int maxMem = (int) (Runtime.getRuntime().maxMemory() / 1000000);
				int totalMem = (int) (Runtime.getRuntime().totalMemory() / 1000000);
				if(maxMem - totalMem <= amount) {
					if(!started) {
						started = true;
						scheduleRestart();
						cancel();
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 1);
	}

	private void scheduleRestart() {
		restart = new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				if(i == 0) {
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 2, 1);
						new Title(TitleAction.TITLE).text("&d&lRESTART SCHEDULED!").send(p);
						new Title(TitleAction.SUBTITLE).text("&fServer will restart in 30s").send(p);
					}
				}
				else if(i == 30) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
				}
				else {
					for(Player p : Bukkit.getOnlinePlayers()) {
						new Title(TitleAction.ACTIOBAR).text("&fRestarting in &d" + (30 - i) + "s").send(p);
					}
					if(i >= 20) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThe server will restart in &d" + (30 -i) + "s"));
					}
				}
				i++;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
	
}
