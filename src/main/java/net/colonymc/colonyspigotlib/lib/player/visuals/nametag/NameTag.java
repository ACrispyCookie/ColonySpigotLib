package net.colonymc.colonyspigotlib.lib.player.visuals.nametag;

import net.colonymc.colonyspigotlib.Main;
import net.colonymc.colonyspigotlib.lib.player.ColonyPlayer;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


public abstract class NameTag {

	ColonyPlayer p;
	private String prefix = "";
	private String suffix = "";
	private BukkitTask update;
	private int period;
	protected abstract String updatePrefix(ColonyPlayer p);
	protected abstract String updateSuffix(ColonyPlayer p);
	
	public NameTag(ColonyPlayer p, int period) {
		this.p = p;
		this.period = period;
		startUpdating();
	}

	private void startUpdating(){
		update = new BukkitRunnable() {
			@Override
			public void run(){
				prefix(updatePrefix(p));
				suffix(updateSuffix(p));
			}
		}.runTaskTimer(Main.getInstance(), 0, period);
	}

	protected void stopUpdating(){
		update.cancel();
	}
	
	protected void prefix(String s) {
		this.prefix = ChatColor.translateAlternateColorCodes('&', s);
	}

	protected void suffix(String s) {
		this.suffix = ChatColor.translateAlternateColorCodes('&', s);;
	}

	public String getPrefix(){
		return prefix;
	}

	public String getSuffix(){
		return suffix;
	}
}
